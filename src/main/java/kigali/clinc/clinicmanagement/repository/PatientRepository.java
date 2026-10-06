package kigali.clinc.clinicmanagement.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.clinc.clinicmanagement.domain.Patient;


public interface PatientRepository extends JpaRepository<Patient, UUID> {

    // Find patients by last name, ignoring case,
    // and sort by first name from A to Z.
    List<Patient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    // -B4 Gind each patient who has at least one appointment with the specified doctor.
    @Query("""
        SELECT DISTINCT a.patient
        FROM Appointment a
        WHERE a.doctor.id = :doctorId
    """)
    List<Patient> findPatientsOfDoctor(
            @Param("doctorId") UUID doctorId);


            // C2 - Frequent Patients
    @Query("""
        SELECT a.patient
        FROM Appointment a
        GROUP BY a.patient
        HAVING COUNT(a) >= :min
        ORDER BY COUNT(a) DESC
    """)
    List<Patient> findFrequentPatients(@Param("min") long min);
}
