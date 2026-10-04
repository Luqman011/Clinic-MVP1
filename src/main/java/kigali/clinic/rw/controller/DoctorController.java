package kigali.clinic.rw.controller;

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

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveDoctor(
            @RequestBody Doctor doctor,
            @RequestParam(value = "officeNumber", required = false) Integer officeNumber) {

        String msg;
        if (officeNumber != null) {
            msg = doctorService.saveDoctor(doctor, officeNumber);
        } else {
            msg = doctorService.saveDoctor(doctor, 0);
        }
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
}