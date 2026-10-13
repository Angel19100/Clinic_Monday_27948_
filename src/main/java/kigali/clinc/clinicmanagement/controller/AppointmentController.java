package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.service.AppointmentService;

@RestController
@RequestMapping({"/api/appointment", "/api/appointments"})
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Save an appointment
    @PostMapping("/save")
    public ResponseEntity<?> saveAppointment(
            @RequestBody Appointment appointment,
            @RequestParam UUID doctorId,
            @RequestParam UUID patientId) {

        Object result = appointmentService.saveAppointment(
                appointment, doctorId, patientId);

        if (result instanceof String) {
            if (result.equals("Doctor is already booked on that date")) {
                return ResponseEntity.status(409).body(result);
            }

            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    // Get all appointments
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(
                appointmentService.getAllAppointments());
    }

    // Get appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @PathVariable UUID id) {

        Appointment appointment =
                appointmentService.getAppointmentById(id);

        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(appointment);
    }

    // Update an appointment
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable UUID id,
            @RequestBody Appointment appointment,
            @RequestParam UUID doctorId,
            @RequestParam UUID patientId) {

        Appointment updatedAppointment =
                appointmentService.updateAppointment(
                        id, appointment, doctorId, patientId);

        if (updatedAppointment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedAppointment);
    }

    // Delete an appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(
            @PathVariable UUID id) {

        boolean deleted = appointmentService.deleteAppointment(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Appointment deleted successfully");
    }

    // A2: Get appointments by status
    @GetMapping("/by-status")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(
            @RequestParam AppointmentStatus status) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByStatus(status));
    }

    // A3: Get appointments between two dates
    @GetMapping("/between")
    public ResponseEntity<List<Appointment>> getAppointmentsBetween(
            @RequestParam String start,
            @RequestParam String end) {

        java.sql.Date startDate = java.sql.Date.valueOf(start);
        java.sql.Date endDate = java.sql.Date.valueOf(end);

        return ResponseEntity.ok(
                appointmentService.getAppointmentsBetween(
                        startDate, endDate));
    }

    // C1: Appointment statistics by status
    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getAppointmentStatsByStatus() {

        return ResponseEntity.ok(
                appointmentService.getAppointmentStatsByStatus());
    }

    // C4: Cancel appointments for a doctor on a date
    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelAppointmentsForDoctorOnDate(
            @RequestParam UUID doctorId,
            @RequestParam java.sql.Date date) {

        int cancelled =
                appointmentService.cancelAppointmentsForDoctorOnDate(
                        doctorId, date);

        return ResponseEntity.ok(
                cancelled + " appointments cancelled");
    }

    // Bonus 1: Pagination and sorting
    @GetMapping("/page")
    public ResponseEntity<Page<Appointment>> getAppointmentsPage(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort) {

        String[] sortParts = sort.split(",");
        String property = sortParts[0];

        Sort.Direction direction = Sort.Direction.ASC;

        if (sortParts.length > 1
                && sortParts[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, property));

        return ResponseEntity.ok(
                appointmentService.getAppointmentsPage(pageable));
    }

    // Bonus 2: Delete cancelled appointments before a date
    @DeleteMapping("/cancelled-before")
    public ResponseEntity<String> deleteCancelledBefore(
            @RequestParam java.sql.Date date) {

        int deleted =
                appointmentService.deleteCancelledBefore(date);

        return ResponseEntity.ok(
                deleted + " appointments deleted");
    }

    // Update appointment status
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateAppointmentStatus(
            @PathVariable UUID id,
            @RequestParam AppointmentStatus status) {

        String result =
                appointmentService.updateAppointmentStatus(id, status);

        if (result.equals(
                "The appointment with that id does not exist")) {
            return ResponseEntity.status(404).body(result);

        } else if (result.startsWith("Cannot change status from")) {
            return ResponseEntity.badRequest().body(result);

        } else {
            return ResponseEntity.ok(result);
        }
    }

    // Reschedule an appointment
    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<String> rescheduleAppointment(
            @PathVariable UUID id,
            @RequestParam java.sql.Date newDate) {

        String result =
                appointmentService.rescheduleAppointment(id, newDate);

        if (result.equals(
                "The appointment with that id does not exist")) {
            return ResponseEntity.status(404).body(result);

        } else if (result.equals(
                "Doctor is already booked on that date")) {
            return ResponseEntity.status(409).body(result);

        } else if (result.equals(
                "Appointment rescheduled successfully")) {
            return ResponseEntity.ok(result);

        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}