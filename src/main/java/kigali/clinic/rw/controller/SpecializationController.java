package kigali.clinic.rw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.service.SpecializationService;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    @GetMapping("/unused")
    public ResponseEntity<List<Specialization>> findUnusedSpecializations() {

        List<Specialization> specializations =
                specializationService.findUnusedSpecializations();

        return ResponseEntity.ok(specializations);
    }
}
