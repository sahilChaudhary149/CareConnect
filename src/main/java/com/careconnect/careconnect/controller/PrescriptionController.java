package com.careconnect.careconnect.controller;

import com.careconnect.careconnect.model.Prescription;
import com.careconnect.careconnect.service.PrescriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    // Show prescription form
    @GetMapping("/add-prescription")
    public String addPrescriptionPage(Model model) {
        model.addAttribute("prescription", new Prescription());
        return "add-prescription";
    }

    // Save prescription
    @PostMapping("/add-prescription")
    public String savePrescription(
            @ModelAttribute Prescription prescription) {

        prescriptionService.savePrescription(prescription);

        return "redirect:/prescriptions";
    }

    // Show all prescriptions
    @GetMapping("/prescriptions")
    public String prescriptions(Model model) {

        model.addAttribute(
                "prescriptions",
                prescriptionService.getAllPrescriptions()
        );

        return "prescriptions";
    }
}