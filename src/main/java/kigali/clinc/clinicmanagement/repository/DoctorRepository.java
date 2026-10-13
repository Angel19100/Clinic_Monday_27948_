package kigali.clinc.clinicmanagement.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.domain.Office;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
  
     //- B1
    // Find doctors who have a specific specialization,
    // ignoring uppercase/lowercase.
    @Query("""
        SELECT d
        FROM Doctor d
        JOIN d.specializations s
        WHERE LOWER(s.name) = LOWER(:name)
    """)
    List<Doctor> findDoctorsBySpecialization(
            @Param("name") String name);

            //B2
            //Find docotrs who do not have an office.
            @Query("""
                    Select d
                    FROM Doctor d
                    WHERE d.office IS NULL
                    """)
            List<Doctor> findDoctorsWithoutOffice();

 
            
         boolean existsByOffice_OfficeNumberAndIdNot(
                int officeNumber, UUID doctorId);

             // D3: Search doctors by first name or last name
         @Query("""
            SELECT d FROM Doctor d WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY d.lastName ASC
            """)
          List<Doctor> searchDoctorsByName(@Param("name") String name);   

   }