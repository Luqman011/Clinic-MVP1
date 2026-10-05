package kigali.clinic.rw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;
import java.sql.Date;
import org.springframework.web.bind.annotation.*;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.service.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

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

        if (message.equals("Doctor is already booked on that date")) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }


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

    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> countAppointmentsByStatus() {
    List<Object[]> stats = appointmentService.countAppointmentsByStatus();
    return ResponseEntity.ok(stats);
}


@PatchMapping("/cancel-day")
public ResponseEntity<String> cancelAppointmentsByDoctorAndDate(
        @RequestParam UUID doctorId,
        @RequestParam String date) {
        Date appointmentDate = Date.valueOf(date);
        int cancelled = appointmentService.cancelAppointmentsByDoctorAndDate(doctorId, appointmentDate);
    return ResponseEntity.ok(
            cancelled + " appointments cancelled");
}

    @GetMapping("/page")
public ResponseEntity<Page<Appointment>> findAppointmentsPage(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam String sort) {
        String[] sortParts = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParts[1]);
        Sort sorting = Sort.by(direction, sortParts[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);

    Page<Appointment> appointments = appointmentService.findAppointmentsPage(pageable);
    return ResponseEntity.ok(appointments);
}

}