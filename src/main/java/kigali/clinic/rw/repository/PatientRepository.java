package kigali.clinic.rw.repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsBySsNumber(String ssNumber);
    Optional<Patient> findBySsNumber(String ssNumber);
    Optional<Patient> findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(
        String firstName,
        String lastName,
        Date dateOfBirth
);
}