package kigali.clinc.clinicmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.domain.Office;
import kigali.clinc.clinicmanagement.domain.Specialization;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;
import kigali.clinc.clinicmanagement.repository.OfficeRepository;
import kigali.clinc.clinicmanagement.repository.SpecializationRepository;
import java.sql.Date;
import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private SpecializationRepository specializationRepo;

    @Autowired
    private OfficeRepository officeRepo;

    @Autowired
private AppointmentRepository appointmentRepo;

    // CREATE
    public Doctor saveDoctor(Doctor doctor) {

        // Load the real Office
        if (doctor.getOffice() != null &&
            doctor.getOffice().getId() != null) {

            UUID officeId = doctor.getOffice().getId();

            Office office = officeRepo.findById(officeId)
                    .orElseThrow(() ->
                            new RuntimeException("Office not found"));

            doctor.setOffice(office);
        }

        // Load the real Specializations
        if (doctor.getSpecializations() != null &&
            !doctor.getSpecializations().isEmpty()) {

            List<Specialization> existingSpecializations =
                    new ArrayList<>();

            for (Specialization specialization :
                    doctor.getSpecializations()) {

                Specialization existing =
                        specializationRepo
                                .findById(specialization.getId())
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Specialization not found"));

                existingSpecializations.add(existing);
            }

            doctor.setSpecializations(existingSpecializations);
        }

        return doctorRepo.save(doctor);
    }

    // READ ALL
    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    // READ ONE
    public Doctor getDoctorById(UUID id) {
        return doctorRepo.findById(id).orElse(null);
    }

    // UPDATE
    public Doctor updateDoctor(UUID id, Doctor doctor) {

        Doctor existingDoctor =
                doctorRepo.findById(id).orElse(null);

        if (existingDoctor == null) {
            return null;
        }

        existingDoctor.setFirstName(
                doctor.getFirstName());

        existingDoctor.setLastName(
                doctor.getLastName());

        existingDoctor.setDateOfBirth(
                doctor.getDateOfBirth());

        // Load the real Office
        if (doctor.getOffice() != null &&
            doctor.getOffice().getId() != null) {

            UUID officeId = doctor.getOffice().getId();

            Office office = officeRepo.findById(officeId)
                    .orElseThrow(() ->
                            new RuntimeException("Office not found"));

            existingDoctor.setOffice(office);
        } else {

            //No office so this doctor has no office
            existingDoctor.setOffice(null);
        }

        // Load the real Specializations
        if (doctor.getSpecializations() != null) {

            List<Specialization> existingSpecializations =
                    new ArrayList<>();

            for (Specialization specialization :
                    doctor.getSpecializations()) {

                Specialization existing =
                        specializationRepo
                                .findById(specialization.getId())
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Specialization not found"));

                existingSpecializations.add(existing);
            }

            existingDoctor.setSpecializations(
                    existingSpecializations);
        }

        return doctorRepo.save(existingDoctor);
    }

    // DELETE
    public boolean deleteDoctor(UUID id) {

        if (!doctorRepo.existsById(id)) {
            return false;
        }

        doctorRepo.deleteById(id);
        return true;
    }

    //  - B1
    // Find doctors by specialization, ignoring letter case.
    public List<Doctor> getDoctorsBySpecialization(
            String name) {

        return doctorRepo.findDoctorsBySpecialization(name);
    }

    // B2
// Find doctors who do not have an office.
public List<Doctor> getDoctorsWithoutOffice() {

    return doctorRepo.findDoctorsWithoutOffice();
}

// A1 Add one specialization to a doctor
public String addSpecializationToDoctor(UUID doctorId, UUID specializationId)
    {// check if doctor exists
        Doctor doctor= doctorRepo.findById(doctorId).orElse(null);
        if(doctor == null){
            return "The doctor with that id does not exist";

        }
        // check whether the specialization exists
        Specialization specialization = specializationRepo.findById(specializationId).orElse(null);
        if(specialization == null){
            return "That specialization does not exist";

        }
         //Check whether the doctoe alread has the specialization
         if(doctor.getSpecializations().contains(specialization)){
            return "The doctoe already has this specialization";
         }

         //adding without removing the existing specializations
         doctor.getSpecializations().add(specialization);
         //save doctor
         doctorRepo.save(doctor);
         return"Specialization added to the doctor";

    }

    //A2 remove one specialization from a doctor
     
    public String removeSpecializationFromDoctor( UUID doctorId, UUID specializationId) {

    Doctor doctor = doctorRepo.findById(doctorId).orElse(null);

    if (doctor == null) {
        return "The doctor with that id does not exist";
    }

    Specialization specialization =
            specializationRepo.findById(specializationId).orElse(null);

    if (specialization == null) {
        return "That specialization does not exist";
    }

    if (!doctor.getSpecializations().contains(specialization)) {
        return "The doctor does not have that specialization";
    }

    doctor.getSpecializations().remove(specialization);
    doctorRepo.save(doctor);

    return "Specialization removed from the doctor";
}
// A4 Move doctor to another office

public String moveDoctorToOffice(UUID doctorId, int officeNumber){
    Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
    
    if(doctor == null) {
        return "The doctor with that id does not exist";
    }

    Office office= officeRepo.findByOfficeNumber(officeNumber);

    if(office == null){
        return "The office with taht office number does not exist";
    }

    boolean taken = doctorRepo.existsByOffice_OfficeNumberAndIdNot(officeNumber, doctorId);

    if (taken){
        return "That office is taken by other doctor";
    }

    doctor.setOffice(office);
    doctorRepo.save(doctor);

    return "Office changed successfully";


}
 // D1: Get a doctor's agenda for a specific date
public List<Appointment> getDoctorAgenda(UUID doctorId, Date date) {
    return appointmentRepo.findByDoctor_IdAndAppointmentDateAndStatusNot(
            doctorId,
            date,
            AppointmentStatus.CANCELLED);
}

// D3 Search doctors by name
public List<Doctor> searchDoctorsByName(String name) {
    return doctorRepo.searchDoctorsByName(name);
}
// bonus 2 doctor worload method

public List<Object[]> getDoctorWorkload(java.sql.Date start, java.sql.Date end) {
    return appointmentRepo.findDoctorWorkload(start, end);
}

}