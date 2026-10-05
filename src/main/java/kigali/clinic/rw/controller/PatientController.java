package kigali.clinic.rw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.service.PatientService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> savePatient(@RequestBody Patient patient) {
        String msg = patientService.savePatient(patient);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/by-last-name")
    public ResponseEntity<List<Patient>> findPatientsByLastName(
        @RequestParam String lastName) {

    List<Patient> patients =
            patientService.findPatientsByLastName(lastName);

    return ResponseEntity.ok(patients);
}
@GetMapping("/of-doctor/{doctorId}")
public ResponseEntity<?> findPatientsOfDoctor(
        @PathVariable UUID doctorId) {

    Optional<List<Patient>> result =patientService.findPatientsOfDoctor(doctorId);

    if (result.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The doctor with that id does not exist");
    }

    return ResponseEntity.ok(result.get());
}

}