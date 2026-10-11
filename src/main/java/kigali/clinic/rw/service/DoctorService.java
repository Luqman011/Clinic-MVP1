package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired 
    private SpecializationRepository specializationRepository;

    public String saveDoctor(Doctor doctor, int officeNumber) {

    Optional<Office> getOffice = officeRepository.findByOfficeNumber(officeNumber);

    if (getOffice.isPresent()) {

        Boolean CheckIfOfficeIsAssigned =
                doctorRepository.existsByOffice(getOffice.get());

        if (!CheckIfOfficeIsAssigned) {

            doctor.setOffice(getOffice.get());
            doctorRepository.save(doctor);

            return "Doctor saved successfully";

        } else {
            return "Office is already assigned to another doctor";
        }

    } else {
        return "Office not found with office number: " + officeNumber;
    }
}
public Optional<Doctor> getDoctorById(UUID id) {
    return doctorRepository.findById(id);
}

public List<Doctor> findDoctorsBySpecialization(String name) {
    return doctorRepository.findDoctorsBySpecialization(name);
}

public List<Doctor> findDoctorsWithoutOffice() {
    return doctorRepository.findDoctorsWithoutOffice();
}

public String addSpecialization(UUID doctorId, UUID specializationId) {

    Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);

    if (doctorOptional.isEmpty()) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "The doctor with that id does not exist");
    }

    Optional<Specialization> specializationOptional =
            specializationRepository.findById(specializationId);

    if (specializationOptional.isEmpty()) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "That specialization does not exist");
    }

    Doctor doctor = doctorOptional.get();
    Specialization specialization = specializationOptional.get();

    if (doctor.getSpecializations().contains(specialization)) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "The doctor already has that specialization");
    }

    doctor.getSpecializations().add(specialization);
    doctorRepository.save(doctor);

    return "Specialization added to the doctor";
}



}