package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.PatientRepository;
import kigali.clinic.rw.repository.DoctorRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    @Autowired
    private DoctorRepository doctorRepository;

    public String savePatient(Patient patient) {
    if (patient == null ||
        patient.getFirstName() == null ||
        patient.getLastName() == null ||
        patient.getDateOfBirth() == null) {

        return "Invalid patient data: firstName/lastName/dateOfBirth required";
    }

    Optional<Patient> existingPatient =
        patientRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(
            patient.getFirstName(),
            patient.getLastName(),
            patient.getDateOfBirth()
        );

    if (existingPatient.isPresent()) {
        return "Patient already exists for the given name and date of birth.";
    }

    patientRepository.save(patient);
    return "Patient saved successfully.";
}

public Optional<List<Patient>> findPatientsOfDoctor(UUID doctorId) {

    if (doctorRepository.findById(doctorId).isEmpty()) {
        return Optional.empty();
    }

    return Optional.of(
            patientRepository.findPatientsOfDoctor(doctorId)
    );
}

    public List<Patient> findPatientsByLastName(String lastName) {
    return patientRepository.findByLastNameIgnoreCase(lastName);
}

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(UUID id) {
        return patientRepository.findById(id);
    }

    public String updatePatient(UUID id, Patient patient) {
        if (!patientRepository.existsById(id)) {
            return "Patient not found with id: " + id;
        }
        patient.setId(id);
        patientRepository.save(patient);
        return "Patient updated successfully";
    }

    public String deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            return "Patient not found with id: " + id;
        }
        patientRepository.deleteById(id);
        return "Patient deleted successfully";
    }

    // lookup by names for appointment controller/service
    public Optional<Patient> findByFirstAndLastName(String firstName, String lastName) {
        return patientRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(firstName, lastName, null);
    }

    public List<Patient> findFrequentPatients(long min) {
    return patientRepository.findFrequentPatients(min);
    }

}