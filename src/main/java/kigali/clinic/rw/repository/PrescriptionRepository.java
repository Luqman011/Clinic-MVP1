package kigali.clinic.rw.repository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import kigali.clinic.rw.domain.Prescription;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, UUID> {
}