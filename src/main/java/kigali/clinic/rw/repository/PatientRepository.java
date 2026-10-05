package kigali.clinic.rw.repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
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
    List<Patient> findByLastNameIgnoreCase(String lastName);

    @Query("""
        SELECT DISTINCT p
        FROM Patient p
        JOIN p.appointments a
        WHERE a.doctor.id = :doctorId
        """)
    List<Patient> findPatientsOfDoctor(@Param("doctorId") UUID doctorId);

    @Query("""
        SELECT p
        FROM Patient p
        JOIN p.appointments a
        GROUP BY p
        HAVING COUNT(a) >= :min
        ORDER BY COUNT(a) DESC
        """)
    List<Patient> findFrequentPatients(@Param("min") long min);

}