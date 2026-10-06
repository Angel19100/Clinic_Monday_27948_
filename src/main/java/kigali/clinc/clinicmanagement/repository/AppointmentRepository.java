package kigali.clinc.clinicmanagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
public interface AppointmentRepository
        extends JpaRepository<Appointment, UUID> {

   //  - A2
   List<Appointment> findByStatusOrderByAppointmentDateAsc(
        AppointmentStatus status);
 
        
        //  - A3
  List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(
        LocalDate start,
        LocalDate end);

        // - A3
List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(
        java.sql.Date start,
        java.sql.Date end);

    // Assignment 2 - A4
boolean existsByDoctorIdAndAppointmentDateAndStatusNot(
        UUID doctorId,
        java.sql.Date appointmentDate,
        AppointmentStatus status);
        
        
     //  C1
// Count appointments for each status.
@Query("""
    SELECT a.status, COUNT(a)
    FROM Appointment a
    GROUP BY a.status
""")
List<Object[]> countAppointmentsByStatus();   

// C4 - Cancel all appointments for a doctor on a specific date
@Modifying
@Transactional
@Query("""
    UPDATE Appointment a
    SET a.status = kigali.clinc.clinicmanagement.domain.AppointmentStatus.CANCELLED
    WHERE a.doctor.id = :doctorId
    AND a.appointmentDate = :date
    AND a.status <> kigali.clinc.clinicmanagement.domain.AppointmentStatus.COMPLETED
""")
int cancelAppointmentsForDoctorOnDate(
        @Param("doctorId") UUID doctorId,
        @Param("date") java.sql.Date date);

 
Page<Appointment> findAll(Pageable pageable);        

// BONUS 2 - Clean old cancellations
@Modifying
@Transactional
@Query("""
    DELETE FROM Appointment a
    WHERE a.status = kigali.clinc.clinicmanagement.domain.AppointmentStatus.CANCELLED
    AND a.appointmentDate < :date
""")
int deleteCancelledBefore(@Param("date") java.sql.Date date);

}

