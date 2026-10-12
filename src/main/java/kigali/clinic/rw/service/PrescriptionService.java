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
import java.util.List;

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

    public List<Prescription> findPrescriptionsByAppointment(UUID appointmentId) {

    //Check that the appointment exists
    if (!appointmentRepository.existsById(appointmentId)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The appointment with that id does not exist");
    }

    //Return prescriptions linked to the appointment
    return prescriptionRepository.findByAppointment_Id(appointmentId);
}


    public List<Prescription> findPrescriptionHistoryByPatient(String ssNumber) {

    return prescriptionRepository.findPrescriptionHistoryByPatient(ssNumber);
}
    public String updatePrescription(
        UUID id,
        Prescription updatedPrescription) {

    // Find the prescription
    Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The prescription with that id does not exist"));

    // Update only the three allowed fields
    prescription.setMedicineName(updatedPrescription.getMedicineName());
    prescription.setDosage(updatedPrescription.getDosage());
    prescription.setDurationDays(updatedPrescription.getDurationDays());

    prescriptionRepository.save(prescription);

    return "Prescription updated successfully";
}

public String deletePrescription(UUID id) {

    // Find the prescription
    Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The prescription with that id does not exist"));

    // Delete it
    prescriptionRepository.delete(prescription);

    return "Prescription deleted successfully";
}

    public List<Object[]> getTopPrescribedMedicines() {
    return prescriptionRepository.findTopPrescribedMedicines();
}

}