package kigali.clinc.clinicmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Specialization;
import kigali.clinc.clinicmanagement.repository.SpecializationRepository;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepo;

    // CREATE
    public Specialization saveSpecialization(Specialization specialization) {
        return specializationRepo.save(specialization);
    }

    // READ ALL
    public List<Specialization> getAllSpecializations() {
        return specializationRepo.findAll();
    }

    // READ ONE
    public Specialization getSpecializationById(UUID id) {
        return specializationRepo.findById(id).orElse(null);
    }

    // UPDATE
    public Specialization updateSpecialization(
            UUID id,
            Specialization specialization) {

        Specialization existing =
                specializationRepo.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setName(specialization.getName());

        return specializationRepo.save(existing);
    }

    // DELETE
    public boolean deleteSpecialization(UUID id) {

        if (!specializationRepo.existsById(id)) {
            return false;
        }

        specializationRepo.deleteById(id);
        return true;
    }

    // B3
// Find specializations that nobody offers.
public List<Specialization> getUnusedSpecializations() {

    return specializationRepo.findUnusedSpecializations();
}
}