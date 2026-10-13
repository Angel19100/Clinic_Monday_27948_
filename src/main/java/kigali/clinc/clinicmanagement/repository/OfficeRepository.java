package kigali.clinc.clinicmanagement.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Office;
import java.util.List;



@Repository 
public interface OfficeRepository extends  JpaRepository<Office,UUID >{
    
    Office findByOfficeNumber(int officeNumber);

    Optional<Office> findByNameAndOfficeNumber(String name, int officeNumber);

    Boolean existsByOfficeNumber(int officeNumber);

    // C3 - Busiest Office
@Query("""
    SELECT o.name, o.officeNumber, COUNT(a)
    FROM Appointment a
    JOIN a.doctor d
    JOIN d.office o
    GROUP BY o.id, o.name, o.officeNumber
    ORDER BY COUNT(a) DESC
""")
List<Object[]> findBusiestOffice();

@Query("""
    SELECT o FROM Office o WHERE NOT EXISTS (SELECT d FROM Doctor d WHERE d.office = o) ORDER BY o.officeNumber ASC
       """)
    List<Office> findFreeOffices();
    
@Query("SELECT COUNT(o) FROM Office o WHERE o.doctor IS NULL")
long countFreeOffices();
    

}

