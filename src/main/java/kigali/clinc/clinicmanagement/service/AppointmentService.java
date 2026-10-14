package kigali.clinc.clinicmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.domain.Patient;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;
import kigali.clinc.clinicmanagement.repository.PatientRepository;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import kigali.clinc.clinicmanagement.repository.PrescriptionRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    
// CREATE
public Object saveAppointment(
        Appointment appointment,UUID doctorId, UUID patientId) {

       //Reject an Appointment date in the past 
       if (appointment.getAppointmentDate() != null) {
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

            if (appointment.getAppointmentDate().before(today)) {
                return "Appointment date cannot be in the past";
            }
        }    
      // checking if doctor exists
      Doctor doctor = doctorRepo.findById(doctorId).orElse(null);

      if (doctor == null) {
        return "Doctor not found";
    }
      // if patient exists
      Patient patient = patientRepo.findById(patientId).orElse(null);

    if (patient == null) {
        return "Patient not found";
    }

    

    // Assignment 2 - A4
    boolean alreadyBooked =
        appointmentRepo
                .existsByDoctor_IdAndAppointmentDateAndStatusNot(
                        doctorId,
                        appointment.getAppointmentDate(),
                        AppointmentStatus.CANCELLED);

    if (alreadyBooked) {
        return "Doctor is already booked on that date";
    }
    // connecting the apppointment to the doctoe and patient
    appointment.setDoctor(doctor);
    appointment.setPatient(patient);

    //B1 every new appointment starts as SCHEDULED
        appointment.setStatus(AppointmentStatus.SCHEDULED);


    // save the appointement
    return appointmentRepo.save(appointment);
}

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public Appointment getAppointmentById(UUID id) {
        return appointmentRepo.findById(id).orElse(null);
    }

    public Appointment updateAppointment(
            UUID id,
            Appointment appointment,
            UUID doctorId,
            UUID patientId) {

        Appointment existingAppointment =
                appointmentRepo.findById(id).orElse(null);

        if (existingAppointment == null) {
            return null;
        }

        Doctor doctor =
                doctorRepo.findById(doctorId).orElse(null);

        Patient patient =
                patientRepo.findById(patientId).orElse(null);

        if (doctor == null) {
            throw new RuntimeException("Doctor not found");
        }

        if (patient == null) {
            throw new RuntimeException("Patient not found");
        }

        existingAppointment.setAppointmentDate(
                appointment.getAppointmentDate());

        existingAppointment.setReason(
                appointment.getReason());

        // (Removing line to prevent bypassing the status rules using the genral Put endpoint)
        //existingAppointment.setStatus(
                //appointment.getStatus());

        existingAppointment.setDoctor(doctor);
        existingAppointment.setPatient(patient);

        return appointmentRepo.save(existingAppointment);
    }
  // C4 preventing deleting appontments that have a prescription
    public String deleteAppointment(UUID id) {

        if (!appointmentRepo.existsById(id)) {
            return "NOT_FOUND";
        }

        
        if (prescriptionRepo.existsByAppointment_Id(id)) {
            return "That appointment has prescriptions, delete them first";
        }

        appointmentRepo.deleteById(id);
             return "Appointment deleted successfully";
    }

    // - A2
    public List<Appointment> getAppointmentsByStatus(
        AppointmentStatus status) {

     return appointmentRepo
            .findByStatusOrderByAppointmentDateAsc(status);
}


//  -A3
    public List<Appointment> getAppointmentsBetween(
        java.sql.Date start,
        java.sql.Date end) {

    return appointmentRepo
            .findByAppointmentDateBetweenOrderByAppointmentDateAsc(
                    start, end);
}

// - C1
// Get appointment count for each status.
public List<Object[]> getAppointmentStatsByStatus() {

    return appointmentRepo.countAppointmentsByStatus();
}

// C4 - Doctor is absent
public int cancelAppointmentsForDoctorOnDate(
        UUID doctorId,
        java.sql.Date date) {

    return appointmentRepo.cancelAppointmentsForDoctorOnDate(
            doctorId,
            date
    );
}

// BONUS 1 - Pagination
public Page<Appointment> getAppointmentsPage(Pageable pageable) {
    return appointmentRepo.findAll(pageable);
}

// BONUS 2 - Clean old cancellations
public int deleteCancelledBefore(java.sql.Date date) {
    return appointmentRepo.deleteCancelledBefore(date);
}


public String updateAppointmentStatus( UUID id, AppointmentStatus newStatus) {

    Appointment appointment = appointmentRepo.findById(id).orElse(null);

    if (appointment == null) {
        return "The appointment with that id does not exist";
    }

    AppointmentStatus currentStatus = appointment.getStatus();

    boolean allowed = false;

    if (currentStatus == AppointmentStatus.SCHEDULED) {
        if (newStatus == AppointmentStatus.CONFIRMED ||
                newStatus == AppointmentStatus.CANCELLED) {
            allowed = true;
        }
    } else if (currentStatus == AppointmentStatus.CONFIRMED) {
        if (newStatus == AppointmentStatus.COMPLETED ||
                newStatus == AppointmentStatus.CANCELLED) {
            allowed = true;
        }
    }

    if (!allowed) {
        return "Cannot change status from "
                + currentStatus.name()
                + " to "
                + newStatus.name();
    }

    appointment.setStatus(newStatus);
    appointmentRepo.save(appointment);

    return "Appointment status updated successfully";
}


public String rescheduleAppointment(
        UUID id,
        java.sql.Date newDate) {

    Appointment appointment =
            appointmentRepo.findById(id).orElse(null);

    if (appointment == null) {
        return "The appointment with that id does not exist";
    }

    if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
        return "A COMPLETED appointment cannot be rescheduled";
    }

    if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
        return "A CANCELLED appointment cannot be rescheduled";
    }

    java.sql.Date today = java.sql.Date.valueOf(
            java.time.LocalDate.now());

    if (newDate.before(today)) {
        return "Appointment date cannot be in the past";
    }

    boolean alreadyBooked =
            appointmentRepo
                    .existsByDoctor_IdAndAppointmentDateAndStatusNot(
                            appointment.getDoctor().getId(),
                            newDate,
                            AppointmentStatus.CANCELLED);

    if (alreadyBooked) {
        return "Doctor is already booked on that date";
    }

    appointment.setAppointmentDate(newDate);
    appointment.setStatus(AppointmentStatus.SCHEDULED);

    appointmentRepo.save(appointment);

    return "Appointment rescheduled successfully";

 }
 public boolean existsById(UUID id) {
    return appointmentRepo.existsById(id);
}

   
}