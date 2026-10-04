package kigali.clinic.rw.repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;


@Repository 
public interface DoctorRepository extends JpaRepository<Doctor,UUID> {
    

    Boolean existsByOffice(Office office);
    Optional<Doctor> findByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, Date dateOfBirth);


    
}
