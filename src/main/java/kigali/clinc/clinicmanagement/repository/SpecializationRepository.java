package kigali.clinc.clinicmanagement.repository;

import java.util.UUID;
import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kigali.clinc.clinicmanagement.domain.Specialization;

public interface SpecializationRepository
        extends JpaRepository<Specialization, UUID> {
                
                // B3 Find specializations that no doctor holds.
    @Query("""
        SELECT s
        FROM Specialization s
        WHERE s.doctors IS EMPTY
    """)
    List<Specialization> findUnusedSpecializations();
}

