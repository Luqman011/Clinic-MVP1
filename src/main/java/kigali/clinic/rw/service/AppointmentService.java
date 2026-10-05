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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
private PatientRepository patientRepository;

@Autowired
private DoctorRepository doctorRepository;

public String saveAppointment(
        Appointment appointment,
        String patientSSNumber,
        String doctorFirstName,
        String doctorLastName) {

    Optional<Patient> patient =
            patientRepository.findBySsNumber(patientSSNumber);

    if (patient.isEmpty()) {
        return "Patient not found with SS number: " + patientSSNumber;
    }

    Optional<Doctor> doctor = doctorRepository.findByFirstNameAndLastName(doctorFirstName, doctorLastName);

    if (doctor.isEmpty()) {
        return "Doctor not found: "
                + doctorFirstName + " "
                + doctorLastName;
    }

    boolean doctorAlreadyBooked = appointmentRepository.existsByDoctorAndAppointmentDateAndStatusNot( doctor.get(), appointment.getAppointmentDate(), AppointmentStatus.CANCELLED);

    if (doctorAlreadyBooked) {
        return "Doctor is already booked on that date";
    }

    appointment.setPatient(patient.get());
    appointment.setDoctor(doctor.get());

    appointmentRepository.save(appointment);

    return "Appointment saved successfully";
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

}