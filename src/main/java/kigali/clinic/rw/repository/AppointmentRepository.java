package kigali.clinic.rw.repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Query("""
        SELECT a.status, COUNT(a)
        FROM Appointment a
        GROUP BY a.status
        """)
    List<Object[]> countAppointmentsByStatus();


    @Modifying
    @Query("""
        UPDATE Appointment a
        SET a.status = :cancelledStatus
        WHERE a.doctor.id = :doctorId
        AND a.appointmentDate = :date
        AND a.status <> :completedStatus
        """)
    int cancelAppointmentsByDoctorAndDate(
        @Param("doctorId") UUID doctorId,
        @Param("date") Date date,
        @Param("cancelledStatus") AppointmentStatus cancelledStatus,
        @Param("completedStatus") AppointmentStatus completedStatus);


        @Modifying
        @Query("""
        DELETE FROM Appointment a
        WHERE a.status = :status
        AND a.appointmentDate < :date
        """)
        int deleteCancelledBefore(
        @Param("status") AppointmentStatus status,
        @Param("date") Date date);

}