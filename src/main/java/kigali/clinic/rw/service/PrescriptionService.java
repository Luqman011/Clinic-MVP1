package kigali.clinic.rw.service;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    public Prescription savePrescription(Prescription prescription, UUID appointmentId) {

        //Find the appointment
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The appointment with that id does not exist"));

        //Check that the appointment is completed
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prescriptions are only for COMPLETED appointments");
        }

        //Set the issue date and appointment
        prescription.setIssuedDate(LocalDate.now());
        prescription.setAppointment(appointment);

        //Save the prescription
        return prescriptionRepository.save(prescription);
    }
}