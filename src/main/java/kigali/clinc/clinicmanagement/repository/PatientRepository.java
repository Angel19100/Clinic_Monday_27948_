
package kigali.clinc.clinicmanagement.repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.clinc.clinicmanagement.domain.Patient;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    // Find patients by last name, ignoring case, sorted by first name.
    List<Patient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    // B4: Find patients who have at least one appointment with a doctor.
    @Query("""
        SELECT DISTINCT a.patient
        FROM Appointment a
        WHERE a.doctor.id = :doctorId
    """)
    List<Patient> findPatientsOfDoctor(
            @Param("doctorId") UUID doctorId);

    // C2: Find frequent patients.
    @Query("""
        SELECT a.patient
        FROM Appointment a
        GROUP BY a.patient
        HAVING COUNT(a) >= :min
        ORDER BY COUNT(a) DESC
    """)
    List<Patient> findFrequentPatients(@Param("min") long min);

    // C3: Find a patient by SS number.
    Optional<Patient> findBySsNumber(String ssNumber);
}