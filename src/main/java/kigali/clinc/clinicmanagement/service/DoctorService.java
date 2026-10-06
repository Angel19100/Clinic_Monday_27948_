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

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private SpecializationRepository specializationRepo;

    @Autowired
    private OfficeRepository officeRepo;

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

}