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
        String patientSSNumber,
        String doctorFirstName,
        String doctorLastName) {

    Optional<Patient> patient =
            patientRepository.findBySsNumber(patientSSNumber);

    if (patient.isEmpty()) {
        return "Patient not found with SS number: " + patientSSNumber;
    }

    Optional<Doctor> doctor =
            doctorRepository.findByFirstNameAndLastName(
                    doctorFirstName,
                    doctorLastName);

    if (doctor.isEmpty()) {
        return "Doctor not found: "
                + doctorFirstName + " "
                + doctorLastName;
    }

    appointment.setPatient(patient.get());
    appointment.setDoctor(doctor.get());

    appointmentRepository.save(appointment);

    return "Appointment saved successfully";
}

}