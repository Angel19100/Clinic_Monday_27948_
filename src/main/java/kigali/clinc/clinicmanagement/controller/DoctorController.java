package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.service.DoctorService;

@RestController
@RequestMapping({"/api/doctor", "/api/doctors"})
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // CREATE
    @PostMapping("/save")
    public ResponseEntity<Doctor> saveDoctor(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.saveDoctor(doctor));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable UUID id) {

        Doctor doctor = doctorService.getDoctorById(id);

        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(doctor);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable UUID id,
            @RequestBody Doctor doctor) {

        Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);

        if (updatedDoctor == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedDoctor);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable UUID id) {

        boolean deleted = doctorService.deleteDoctor(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Doctor deleted successfully");
    }
    
     // - B1
    @GetMapping("/by-specialization")
    public ResponseEntity<List<Doctor>>
            getDoctorsBySpecialization(
                    @RequestParam String name) {

        return ResponseEntity.ok(
                doctorService.getDoctorsBySpecialization(name));
    }

    // B2
@GetMapping("/without-office")
public ResponseEntity<List<Doctor>> getDoctorsWithoutOffice() {

    return ResponseEntity.ok(
            doctorService.getDoctorsWithoutOffice()
    );
}

}