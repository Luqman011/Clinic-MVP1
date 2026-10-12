package kigali.clinic.rw.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired 
    private PrescriptionRepository prescriptionRepository;

public String saveAppointment(Appointment appointment, String patientSSNumber, String doctorFirstName, String doctorLastName) {

    // 1. Reject appointment dates in the past
    if (appointment.getAppointmentDate().toLocalDate().isBefore(LocalDate.now())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Appointment date cannot be in the past");
    }

    // 2. Find the patient
    Optional<Patient> patient = patientRepository.findBySsNumber(patientSSNumber);

    if (patient.isEmpty()) {
        return "Patient not found with SS number: " + patientSSNumber;
    }

    // 3. Find the doctor
    Optional<Doctor> doctor = doctorRepository.findByFirstNameAndLastName(
                    doctorFirstName, doctorLastName);

    if (doctor.isEmpty()) {
        return "Doctor not found: " + doctorFirstName + " " + doctorLastName;
    }

    // 4. Check whether the doctor is already booked
    boolean doctorAlreadyBooked = appointmentRepository.existsByDoctorAndAppointmentDateAndStatusNot(doctor.get(),appointment.getAppointmentDate(),AppointmentStatus.CANCELLED);

    if (doctorAlreadyBooked) {
        return "Doctor is already booked on that date";
    }

    // 5. Assign the patient and doctor
    appointment.setPatient(patient.get());
    appointment.setDoctor(doctor.get());

    // 6. Every new appointment must start as SCHEDULED
    appointment.setStatus(AppointmentStatus.SCHEDULED);

    // 7. Save the appointment
    appointmentRepository.save(appointment);

    return "Appointment saved successfully";
}

public String updateAppointmentStatus(
        UUID appointmentId,
        AppointmentStatus newStatus) {

    //Find the appointment
    Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The appointment with that id does not exist"));

    AppointmentStatus currentStatus = appointment.getStatus();

    //Check whether the requested transition is allowed
    boolean allowed = false;

    if (currentStatus == AppointmentStatus.SCHEDULED) {
        allowed = newStatus == AppointmentStatus.CONFIRMED || newStatus == AppointmentStatus.CANCELLED;
    } else if (currentStatus == AppointmentStatus.CONFIRMED) {
        allowed = newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELLED;
    }

    //Reject invalid transitions
    if (!allowed) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot change status from " + currentStatus + " to " + newStatus);
    }

    //Update and save the status
    appointment.setStatus(newStatus);
    appointmentRepository.save(appointment);

    return "Appointment status updated successfully";
}

public String rescheduleAppointment(
        UUID appointmentId,
        Date newDate) {

    //Find the appointment
    Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The appointment with that id does not exist"));

    //Prevent rescheduling completed appointments
    if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A COMPLETED appointment cannot be rescheduled");
    }

    //Prevent rescheduling cancelled appointments
    if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A CANCELLED appointment cannot be rescheduled");
    }

    //Reject dates in the past
    if (newDate.toLocalDate().isBefore(LocalDate.now())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment date cannot be in the past");
    }

    //Check whether the doctor is already booked
    boolean doctorAlreadyBooked = appointmentRepository.existsByDoctorAndAppointmentDateAndStatusNot(appointment.getDoctor(), newDate, AppointmentStatus.CANCELLED);

    // Ignore the appointment being rescheduled itself
    if (doctorAlreadyBooked && !newDate.equals(appointment.getAppointmentDate())) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Doctor is already booked on that date");
    }

    //Update the date and reset the status
    appointment.setAppointmentDate(newDate);
    appointment.setStatus(AppointmentStatus.SCHEDULED);

    //Save the changes
    appointmentRepository.save(appointment);

    return "Appointment rescheduled successfully";
}


public List<Appointment> findAppointmentsByStatus(
        AppointmentStatus status) {

    return appointmentRepository.findByStatusOrderByAppointmentDateAsc(status);
}

public List<Appointment> findAppointmentsBetween(Date start, Date end) {

    return appointmentRepository.findByAppointmentDateBetween(start, end);
}

public List<Object[]> countAppointmentsByStatus() {
    return appointmentRepository.countAppointmentsByStatus();
}

@Transactional
public int cancelAppointmentsByDoctorAndDate(UUID doctorId, Date date) {

    return appointmentRepository.cancelAppointmentsByDoctorAndDate(doctorId,date,AppointmentStatus.CANCELLED,AppointmentStatus.COMPLETED);
}

public Page<Appointment> findAppointmentsPage(Pageable pageable) {
    return appointmentRepository.findAll(pageable);
}

@Transactional
public int deleteCancelledBefore(Date date) {
    return appointmentRepository.deleteCancelledBefore(AppointmentStatus.CANCELLED, date);
}

public String deleteAppointment(UUID appointmentId) {

    //Check whether the appointment exists
    Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The appointment with that id does not exist"));

    //Prevent deletion if the appointment has prescriptions
    if (prescriptionRepository.existsByAppointment_Id(appointmentId)) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "That appointment has prescriptions, delete them first");
    }

    //Delete the appointment
    appointmentRepository.delete(appointment);

    return "Appointment deleted successfully";
}


}