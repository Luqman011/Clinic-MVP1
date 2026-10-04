package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;

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
        UUID patientId,
        UUID doctorId) {

    Optional<Patient> patient =
            patientRepository.findById(patientId);

    if (patient.isEmpty()) {
        return "Patient not found";
    }

    Optional<Doctor> doctor =
            doctorRepository.findById(doctorId);

    if (doctor.isEmpty()) {
        return "Doctor not found";
    }

    appointment.setPatient(patient.get());
    appointment.setDoctor(doctor.get());

    appointmentRepository.save(appointment);

    return "Appointment saved successfully";
}

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(UUID id) {
        return appointmentRepository.findById(id);
    }

    public String deleteAppointment(UUID id) {
        if (!appointmentRepository.existsById(id)) {
            return "Appointment not found with id: " + id;
        }
        appointmentRepository.deleteById(id);
        return "Appointment deleted successfully";
    }
}