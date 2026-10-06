package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinc.clinicmanagement.domain.Patient;
import kigali.clinc.clinicmanagement.service.PatientService;

@RestController
@RequestMapping({"/api/patient", "/api/patients"})
public class PatientController {

    @Autowired
    private PatientService patientService;

    // CREATE
    @PostMapping("/save")
    public ResponseEntity<Patient> savePatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.savePatient(patient));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable UUID id) {

        Patient patient = patientService.getPatientById(id);

        if (patient == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(patient);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable UUID id,
            @RequestBody Patient patient) {

        Patient updatedPatient =
                patientService.updatePatient(id, patient);

        if (updatedPatient == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedPatient);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable UUID id) {

        boolean deleted = patientService.deletePatient(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Patient deleted successfully");
    }
    //A1
    // Find patients by last name, case insensitive,
    // sorted by first name A-Z.
    @GetMapping("/by-last-name")
    public ResponseEntity<List<Patient>> getPatientsByLastName(
            @RequestParam String lastName) {

        return ResponseEntity.ok(
                patientService.getPatientsByLastName(lastName)
        );
    }

    // Assignment 2 - B4
@GetMapping("/of-doctor/{doctorId}")
public ResponseEntity<?> getPatientsOfDoctor(
        @PathVariable UUID doctorId) {

    if (!patientService.doctorExists(doctorId)) {
        return ResponseEntity
                .status(404)
                .body("The doctor with that id does not exist");
    }

    return ResponseEntity.ok(
            patientService.getPatientsOfDoctor(doctorId)
    );
}

// C2 - Frequent Patients
@GetMapping("/frequent")
public ResponseEntity<List<Patient>> getFrequentPatients(
        @RequestParam long min) {

    return ResponseEntity.ok(
            patientService.getFrequentPatients(min)
    );
}
}
