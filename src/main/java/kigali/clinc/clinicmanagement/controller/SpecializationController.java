package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinc.clinicmanagement.domain.Specialization;
import kigali.clinc.clinicmanagement.service.SpecializationService;

@RestController
@RequestMapping({"/api/specialization", "/api/specializations"})
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    // CREATE
    @PostMapping("/save")
    public ResponseEntity<Specialization> saveSpecialization(
            @RequestBody Specialization specialization) {

        return ResponseEntity.ok(
                specializationService.saveSpecialization(specialization));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Specialization>> getAllSpecializations() {

        return ResponseEntity.ok(
                specializationService.getAllSpecializations());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Specialization> getSpecializationById(
            @PathVariable UUID id) {

        Specialization specialization =
                specializationService.getSpecializationById(id);

        if (specialization == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(specialization);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Specialization> updateSpecialization(
            @PathVariable UUID id,
            @RequestBody Specialization specialization) {

        Specialization updated =
                specializationService.updateSpecialization(
                        id,
                        specialization);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSpecialization(
            @PathVariable UUID id) {

        boolean deleted =
                specializationService.deleteSpecialization(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Specialization deleted successfully");
    }
 // B3
@GetMapping("/unused")
public ResponseEntity<List<Specialization>> getUnusedSpecializations() {

    return ResponseEntity.ok(
            specializationService.getUnusedSpecializations()
    );
}

}