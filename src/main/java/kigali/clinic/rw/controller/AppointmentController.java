package kigali.clinic.rw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.service.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping(
            value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> saveAppointment(
            @RequestBody Appointment appointment,
            @RequestParam String patientSSNumber,
            @RequestParam String doctorFirstName,
            @RequestParam String doctorLastName) {

        String message = appointmentService.saveAppointment(
                appointment,
                patientSSNumber,
                doctorFirstName,
                doctorLastName
        );

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}