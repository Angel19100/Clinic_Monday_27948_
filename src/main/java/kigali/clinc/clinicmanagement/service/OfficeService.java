package kigali.clinc.clinicmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Office;
import kigali.clinc.clinicmanagement.repository.OfficeRepository;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepo;

    // CREATE
    public String saveOffice(Office office) {

        if (officeRepo.findByOfficeNumber(office.getOfficeNumber()) != null) {
            return "Office with that number " + office.getOfficeNumber() + " already exists";
        }

        officeRepo.save(office);

        return "Office is saved Successfully";
    }

    // READ ALL
    public List<Office> getAllOffices() {
        return officeRepo.findAll();
    }

    // READ ONE
    public Office getOfficeById(UUID id) {
        return officeRepo.findById(id).orElse(null);
    }

    // UPDATE
    public Office updateOffice(UUID id, Office office) {

        Office existingOffice = officeRepo.findById(id).orElse(null);

        if (existingOffice == null) {
            return null;
        }

        existingOffice.setName(office.getName());
        existingOffice.setOfficeNumber(office.getOfficeNumber());

        return officeRepo.save(existingOffice);
    }

    // DELETE
    public boolean deleteOffice(UUID id) {

        if (!officeRepo.existsById(id)) {
            return false;
        }

        officeRepo.deleteById(id);
        return true;
    }

// C3 - Busiest Office
public Object[] getBusiestOffice() {

    List<Object[]> results = officeRepo.findBusiestOffice();

    if (results.isEmpty()) {
        return new Object[]{"No appointments yet"};
    }

    return results.get(0);
}
// A3 get all free offices
  public List<Office> getFreeOffices(){
    return officeRepo.findFreeOffices();
}

}