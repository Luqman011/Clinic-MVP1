package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public String savePatient(Patient patient) {
        if (patient == null || patient.getFirstName() == null || patient.getLastName() == null || patient.getDateOfBirth() == null) {
            return "Invalid patient data: firstName/lastName/dateOfBirth required";
        }

        boolean exists = patientRepository.findAll().stream()
            .anyMatch(p -> patient.getFirstName().equals(p.getFirstName())
                         && patient.getLastName().equals(p.getLastName())
                         && patient.getDateOfBirth().equals(p.getDateOfBirth()));

        if (!exists) {
            patientRepository.save(patient);
            return "Patient saved successfully.";
        } else {
            return "Patient already exists for the given name and date of birth.";
        }
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
        return patientRepository.findByFirstNameAndLastNameAndDateOfBirth(firstName, lastName, null);
    }
}