package kigali.clinic.rw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.sql.Date;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
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

    @GetMapping("/by-status")
public ResponseEntity<List<Appointment>> findAppointmentsByStatus(
        @RequestParam AppointmentStatus status) {

    List<Appointment> appointments =
            appointmentService.findAppointmentsByStatus(status);

    return ResponseEntity.ok(appointments);
}
    @GetMapping("/between")
    public ResponseEntity<List<Appointment>> findAppointmentsBetween(
            @RequestParam String start,
            @RequestParam String end) {

        Date startDate = Date.valueOf(start);
        Date endDate = Date.valueOf(end);

        List<Appointment> appointments = appointmentService.findAppointmentsBetween(startDate, endDate);

        return ResponseEntity.ok(appointments);
    }

}