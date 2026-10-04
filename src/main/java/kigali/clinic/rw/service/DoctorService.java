package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private OfficeRepository officeRepository;

    public String saveDoctor(Doctor doctor, int officeNumber) {


        Optional<Office> getOffice = officeRepository.findByOfficeNumber(officeNumber);
        if (getOffice.isPresent()) {
            Boolean CheckIfOfficeIsAssigned = doctorRepository.existsByOffice(getOffice.get());
            if (!CheckIfOfficeIsAssigned) {
                doctorRepository.save(doctor);
                return "Doctor saved successfully";
            } else {
                return "Office is already assigned to another doctor";
            }
        } else {
            return "Office not found with office number: " + officeNumber;
        }
        
    }
}