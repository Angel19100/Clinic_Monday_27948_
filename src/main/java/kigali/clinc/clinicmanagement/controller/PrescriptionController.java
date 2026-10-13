
package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinc.clinicmanagement.domain.Prescription;
import kigali.clinc.clinicmanagement.service.PrescriptionService;
import kigali.clinc.clinicmanagement.service.AppointmentService;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private AppointmentService appointmentService;

    // C1: Create a prescription
    @PostMapping("/save")
    public ResponseEntity<String> savePrescription(
            @RequestBody Prescription prescription,
            @RequestParam UUID appointmentId) {

        String result = prescriptionService.savePrescription(
                prescription, appointmentId);

        if (result.equals("The appointment with that id does not exist")) {
            return ResponseEntity.status(404).body(result);
        } else if (result.equals(
                "Prescriptions are only for COMPLETED appointments")) {
            return ResponseEntity.badRequest().body(result);
        } else {
            return ResponseEntity.status(201).body(result);
        }
    }

    // C2: Get prescriptions by appointment
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getPrescriptionsByAppointment(
            @PathVariable UUID appointmentId) {

        if (!appointmentService.existsById(appointmentId)) {
            return ResponseEntity.status(404)
                    .body("The appointment with that id does not exist");
        }

        return ResponseEntity.ok(
                prescriptionService.getPrescriptionsByAppointment(
                        appointmentId));
    }

    // C3: Get prescriptions by patient
    @GetMapping("/patient/{ssNumber}")
    public ResponseEntity<?> getPrescriptionsByPatient(
            @PathVariable String ssNumber) {

        List<Prescription> prescriptions =
                prescriptionService.getPrescriptionsByPatient(ssNumber);

        if (prescriptions == null) {
            return ResponseEntity.status(404)
                    .body("That patient with that ssnumber does not exist");
        }

        return ResponseEntity.ok(prescriptions);
    }

    // C4: Update a prescription
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePrescription(
            @PathVariable UUID id,
            @RequestBody Prescription prescription) {

        String result = prescriptionService.updatePrescription(
                id, prescription);

        if (result.equals(
                "The prescription with that id does not exist")) {
            return ResponseEntity.status(404).body(result);
        }

        return ResponseEntity.ok(result);
    }

    // C4: Delete a prescription
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePrescription(
            @PathVariable UUID id) {

        String result = prescriptionService.deletePrescription(id);

        if (result.equals(
                "The prescription with that id does not exist")) {
            return ResponseEntity.status(404).body(result);
        }

        return ResponseEntity.ok(result);
    }
}