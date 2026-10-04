package kigali.clinic.rw.repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {


    List<Appointment> searchByReason(@Param("kw") UUID keyword);
    List<Appointment> findByReasonContainingIgnoreCaseOrderByAppointmentDate(String reason);
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByAppointmentDateBetween(Date start, Date end);
    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);
    boolean existsByDoctorAndAppointmentDateAndStatusNot(Doctor doctor, Date appointmentDate, AppointmentStatus status);

}