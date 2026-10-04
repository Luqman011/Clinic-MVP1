package kigali.clinic.rw.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping(
        value = "/save",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> saveDoctor(
            @RequestBody Doctor doctor,
            @RequestParam(value = "officeNumber", required = false) Integer officeNumber) {

        if (officeNumber == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Office number is required");
        }

        String msg =
                doctorService.saveDoctor(doctor, officeNumber);

        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping(
        value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getDoctorById(
            @PathVariable UUID id) {

        Optional<Doctor> doctor =
                doctorService.getDoctorById(id);

        if (doctor.isPresent()) {
            return ResponseEntity.ok(doctor.get());
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Doctor not found with id: " + id);
    }
}