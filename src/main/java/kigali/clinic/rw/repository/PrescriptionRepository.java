package kigali.clinic.rw.repository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.clinic.rw.domain.Prescription;
import java.util.List;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, UUID> {
            List<Prescription> findByAppointment_Id(UUID appointmentId);


    @Query("""
    SELECT p
    FROM Prescription p
    WHERE p.appointment.patient.ssNumber = :ssNumber
    ORDER BY p.issuedDate DESC
    """)
    List<Prescription> findPrescriptionHistoryByPatient(
        @Param("ssNumber") String ssNumber);


    boolean existsByAppointment_Id(UUID appointmentId);  
    
    
    long count();

    @Query("""
    SELECT p.medicineName, COUNT(p)
    FROM Prescription p
    GROUP BY p.medicineName
    ORDER BY COUNT(p) DESC
    """)
    List<Object[]> findTopPrescribedMedicines();
}