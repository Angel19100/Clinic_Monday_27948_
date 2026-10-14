
package kigali.clinc.clinicmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.domain.Prescription;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;
import kigali.clinc.clinicmanagement.repository.PrescriptionRepository;
import kigali.clinc.clinicmanagement.repository.PatientRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    // C1: Save a prescription.
    public String savePrescription(
            Prescription prescription, UUID appointmentId) {

        Appointment appointment =
                appointmentRepo.findById(appointmentId).orElse(null);

        if (appointment == null) {
            return "The appointment with that id does not exist";
        }

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return "Prescriptions are only for COMPLETED appointments";
        }

        prescription.setAppointment(appointment);
        prescription.setIssuedDate(LocalDate.now());
        prescriptionRepo.save(prescription);

        return "Prescription saved successfully";
    }

    // C2 Get prescriptions by appointment.
    public List<Prescription> getPrescriptionsByAppointment(
            UUID appointmentId) {

        return prescriptionRepo
                .findByAppointment_IdOrderByIssuedDateDesc(appointmentId);
    }

    // C3: Get prescriptions by patient SS number.
    public List<Prescription> getPrescriptionsByPatient(String ssNumber) {

        if (patientRepo.findBySsNumber(ssNumber).isEmpty()) {
            return null;
        }

        return prescriptionRepo.findByPatientSsNumber(ssNumber);
    }

    // C4: Update a prescription.
    public String updatePrescription(UUID id, Prescription updated) {

        Prescription existing =
                prescriptionRepo.findById(id).orElse(null);

        if (existing == null) {
            return "The prescription with that id does not exist";
        }

        existing.setMedicineName(updated.getMedicineName());
        existing.setDosage(updated.getDosage());
        existing.setDurationDays(updated.getDurationDays());

        prescriptionRepo.save(existing);

        return "Prescription updated successfully";
    }

    // C4 Delete a prescription.
    public String deletePrescription(UUID id) {

        Prescription existing =
                prescriptionRepo.findById(id).orElse(null);

        if (existing == null) {
            return "The prescription with that id does not exist";
        }

        prescriptionRepo.delete(existing);

        return "Prescription deleted successfully";
    }

    //bonus1 top medicines
    public List<Object[]> getTopMedicines(){
        return prescriptionRepo.findTopMedicines();
    }
}