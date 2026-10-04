package kigali.clinic.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {


    List<Appointment> searchByReason(@Param("kw") UUID keyword);
    List<Appointment> findByReasonContainingIgnoreCaseOrderByAppointmentDate(String reason);

}