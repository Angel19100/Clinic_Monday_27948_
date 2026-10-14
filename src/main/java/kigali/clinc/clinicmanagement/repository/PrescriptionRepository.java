
package kigali.clinc.clinicmanagement.repository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;

import kigali.clinc.clinicmanagement.domain.Prescription;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, UUID> {

    // C2 Find prescriptions belonging to an appointment
    List<Prescription> findByAppointment_IdOrderByIssuedDateDesc(
            UUID appointmentId);

    // C4 Check whether an appointment has prescriptions
    boolean existsByAppointment_Id(UUID appointmentId);
 //C3 QUERY
 
    @Query("""
         SELECT p FROM Prescription p WHERE p.appointment.patient.ssNumber = :ssNumber ORDER BY p.issuedDate DESC
    """)
    List<Prescription> findByPatientSsNumber(
            @Param("ssNumber") String ssNumber);

//Bonus1 most prescribed medicines first

@Query("""
         SELECT p.medicineName, COUNT(p) FROM Prescription p GROUP BY p.medicineName ORDER BY COUNT(p) DESC
        """)
        List<Object[]> findTopMedicines();



}