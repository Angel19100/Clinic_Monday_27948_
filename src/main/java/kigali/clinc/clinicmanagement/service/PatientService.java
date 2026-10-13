package kigali.clinc.clinicmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Patient;
import kigali.clinc.clinicmanagement.repository.PatientRepository;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;


@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
private DoctorRepository doctorRepo;
    public boolean doctorExists(UUID doctorId) {

    return doctorRepo.existsById(doctorId);
}

    // CREATE
    public Patient savePatient(Patient patient) {
        return patientRepo.save(patient);
    }

    // READ ALL
    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    // READ ONE
    public Patient getPatientById(UUID id) {
        return patientRepo.findById(id).orElse(null);
    }

    // UPDATE
    public Patient updatePatient(UUID id, Patient patient) {

        Patient existingPatient = patientRepo.findById(id).orElse(null);

        if (existingPatient == null) {
            return null;
        }

        existingPatient.setFirstName(patient.getFirstName());
        existingPatient.setLastName(patient.getLastName());
        existingPatient.setDateOfBirth(patient.getDateOfBirth());
        existingPatient.setSsNumber(patient.getSsNumber());

        return patientRepo.save(existingPatient);
    }

    // DELETE
    public boolean deletePatient(UUID id) {

        if (!patientRepo.existsById(id)) {
            return false;
        }

        patientRepo.deleteById(id);
        return true;
    }

    //   A1
    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
}

// -B4
// Find patients who have appointments with a doctor.
public List<Patient> getPatientsOfDoctor(UUID doctorId) {

    return patientRepo.findPatientsOfDoctor(doctorId);
}

// C2 - Frequent Patients
public List<Patient> getFrequentPatients(long min) {
    return patientRepo.findFrequentPatients(min);
}
}