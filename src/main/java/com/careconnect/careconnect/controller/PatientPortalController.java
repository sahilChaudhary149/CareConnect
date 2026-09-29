package com.careconnect.careconnect.controller;

import com.careconnect.careconnect.model.User;
import com.careconnect.careconnect.service.AppointmentService;
import com.careconnect.careconnect.service.MedicalRecordService;
import com.careconnect.careconnect.service.PrescriptionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatientPortalController {

    private final AppointmentService appointmentService;
    private final MedicalRecordService medicalRecordService;
    private final PrescriptionService prescriptionService;

    public PatientPortalController(
            AppointmentService appointmentService,
            MedicalRecordService medicalRecordService,
            PrescriptionService prescriptionService) {

        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/patient-portal")
    public String patientPortal(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        // Check if user is logged in
        if (user == null) {
            return "redirect:/login";
        }

        // Allow only patients
        if (!"PATIENT".equals(user.getRole())) {
            return "redirect:/doctor-dashboard";
        }

        Long patientId = user.getId();

        model.addAttribute(
                "appointments",
                appointmentService.getPatientAppointments(patientId)
        );

        model.addAttribute(
                "medicalRecords",
                medicalRecordService.getPatientRecords(patientId)
        );

        model.addAttribute(
                "prescriptions",
                prescriptionService.getPatientPrescriptions(patientId)
        );

        model.addAttribute("patientId", patientId);
        model.addAttribute("patientName", user.getName());

        return "patient-portal";
    }
}