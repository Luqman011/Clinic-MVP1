package kigali.clinic.rw.controller;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.service.PrescriptionService;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/save")
    public ResponseEntity<String> savePrescription(
            @RequestParam UUID appointmentId,
            @RequestBody Prescription prescription) {

        prescriptionService.savePrescription(prescription, appointmentId);

        return ResponseEntity.status(HttpStatus.CREATED).body("Prescription saved successfully");
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByAppointment(
        @PathVariable UUID appointmentId) {

        List<Prescription> prescriptions = prescriptionService.findPrescriptionsByAppointment(appointmentId);

            return ResponseEntity.ok(prescriptions);
}


    @GetMapping("/patient/{ssNumber}")
    public ResponseEntity<List<Prescription>> getPrescriptionHistory(@PathVariable String ssNumber) {

    List<Prescription> prescriptions = prescriptionService.findPrescriptionHistoryByPatient(ssNumber);

    return ResponseEntity.ok(prescriptions);
}

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePrescription(
        @PathVariable UUID id,
        @RequestBody Prescription prescription) {

    String response = prescriptionService.updatePrescription(id, prescription);

    return ResponseEntity.ok(response);
}

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePrescription(
        @PathVariable UUID id) {

    String response = prescriptionService.deletePrescription(id);

    return ResponseEntity.ok(response);
}
}