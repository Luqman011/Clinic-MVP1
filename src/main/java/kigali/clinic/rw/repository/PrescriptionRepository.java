package kigali.clinic.rw.repository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import kigali.clinic.rw.domain.Prescription;
import java.util.List;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, UUID> {
            List<Prescription> findByAppointment_Id(UUID appointmentId);
}