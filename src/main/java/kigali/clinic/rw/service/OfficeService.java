package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.repository.OfficeRepository;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepository;

    public String saveOffice(Office office) {
        officeRepository.save(office);
        return "Office saved successfully";
    }

    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

    public Optional<Office> getOfficeById(UUID id) {
        return officeRepository.findById(id);
    }

    public String updateOffice(UUID id, Office office) {
        if (!officeRepository.existsById(id)) {
            return "Office not found with id: " + id;
        }

        office.setId(id);
        officeRepository.save(office);
        return "Office updated successfully";
    }

    public String deleteOffice(UUID id) {
        if (!officeRepository.existsById(id)) {
            return "Office not found with id: " + id;
        }

        officeRepository.deleteById(id);
        return "Office deleted successfully";
    }
}