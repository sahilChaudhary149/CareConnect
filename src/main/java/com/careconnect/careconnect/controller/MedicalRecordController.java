package com.careconnect.careconnect.controller;

import com.careconnect.careconnect.model.MedicalRecord;
import com.careconnect.careconnect.service.MedicalRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(
            MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/medical-records")
    public String medicalRecords(Model model) {

        model.addAttribute(
                "records",
                medicalRecordService.getAllRecords()
        );

        return "medical-records";
    }

    @GetMapping("/add-medical-record")
    public String addMedicalRecordPage(Model model) {

        model.addAttribute(
                "medicalRecord",
                new MedicalRecord()
        );

        return "add-medical-record";
    }

    @PostMapping("/add-medical-record")
    public String saveMedicalRecord(
            @ModelAttribute MedicalRecord medicalRecord) {

        medicalRecordService.saveRecord(medicalRecord);

        return "redirect:/medical-records";
    }
}