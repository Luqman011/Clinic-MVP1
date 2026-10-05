package kigali.clinic.rw.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.OfficeService;

@RestController
@RequestMapping(value = "/api/office")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @PostMapping(
        value = "/save",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> saveOffice(@RequestBody Office office) {
        String returnedMessage = officeService.saveOffice(office);
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    @GetMapping(
        value = "/all",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Office>> getAllOffices() {
        return new ResponseEntity<>(officeService.getAllOffices(), HttpStatus.OK);
    }

    @GetMapping(
        value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getOfficeById(@PathVariable UUID id) {
        Office office = officeService.getOfficeById(id)
                .orElse(null);

        if (office == null) {
            return new ResponseEntity<>("Office not found with id: " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(office, HttpStatus.OK);
    }

    @PutMapping(
        value = "/update/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> updateOffice(@PathVariable UUID id, @RequestBody Office office) {
        String returnedMessage = officeService.updateOffice(id, office);

        if (returnedMessage.contains("not found")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @DeleteMapping(
        value = "/delete/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> deleteOffice(@PathVariable UUID id) {
        String returnedMessage = officeService.deleteOffice(id);

        if (returnedMessage.contains("not found")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @GetMapping("/busiest")
public ResponseEntity<?> findBusiestOffice() {

    Optional<Object[]> result =
            officeService.findBusiestOffice();

    if (result.isEmpty()) {
        return ResponseEntity.status(HttpStatus.OK).body("No appointments yet");
    }

        return ResponseEntity.ok(result.get());
}
}