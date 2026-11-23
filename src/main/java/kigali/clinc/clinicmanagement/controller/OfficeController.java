package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import kigali.clinc.clinicmanagement.domain.Office;
import kigali.clinc.clinicmanagement.service.OfficeService;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping({"/api/office" , "/api/offices"})
public class OfficeController {

    @Autowired
    private OfficeService officeServes;

    // CREATE
    @PostMapping(
        value = "/save",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> saveOffice(@RequestBody Office office) {

        String returnedMessage = officeServes.saveOffice(office);

        if (returnedMessage.equalsIgnoreCase("Office is saved Successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Office>> getAllOffices() {
        return ResponseEntity.ok(officeServes.getAllOffices());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Office> getOfficeById(@PathVariable UUID id) {

        Office office = officeServes.getOfficeById(id);

        if (office == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(office);
    }

    // UPDATE
    @PutMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Office> updateOffice(
            @PathVariable UUID id,
            @RequestBody Office office) {

        Office updatedOffice = officeServes.updateOffice(id, office);

        if (updatedOffice == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedOffice);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOffice(@PathVariable UUID id) {

        boolean deleted = officeServes.deleteOffice(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Office deleted successfully");
    }

    // C3 - Busiest Office
    @GetMapping("/busiest")
   public ResponseEntity<Object[]> getBusiestOffice() {

    return ResponseEntity.ok(
            officeServes.getBusiestOffice()
    );
  }

  //A3 free Offices
   @GetMapping("/free")
   public ResponseEntity<List<Office>>getFreeOffices() {
    return ResponseEntity.ok(officeServes.getFreeOffices());
    
   }
}
