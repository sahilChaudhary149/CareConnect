package com.careconnect.careconnect.controller;

import com.careconnect.careconnect.model.Appointment;
import com.careconnect.careconnect.model.User;
import com.careconnect.careconnect.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Show appointment booking page
    @GetMapping("/book-appointment")
    public String bookAppointmentPage(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        if (!"PATIENT".equals(user.getRole())) {
            return "redirect:/doctor-dashboard";
        }

        Appointment appointment = new Appointment();

        // Automatically set logged-in patient's ID
        appointment.setPatientId(user.getId());

        model.addAttribute("appointment", appointment);

        return "book-appointment";
    }

    // Save appointment
    @PostMapping("/book-appointment")
    public String bookAppointment(
            @ModelAttribute Appointment appointment,
            HttpSession session) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        // Always use logged-in patient's ID
        appointment.setPatientId(user.getId());

        appointmentService.saveAppointment(appointment);

        return "redirect:/appointments";
    }

    // Show all appointments
    @GetMapping("/appointments")
    public String appointments(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        if ("DOCTOR".equals(user.getRole())) {

            model.addAttribute(
                    "appointments",
                    appointmentService.getDoctorAppointments(user.getId())
            );

            model.addAttribute("isDoctor", true);

        } else {

            model.addAttribute(
                    "appointments",
                    appointmentService.getPatientAppointments(user.getId())
            );

            model.addAttribute("isDoctor", false);
        }

        return "appointments";
    }

    // Confirm appointment
    @GetMapping("/appointment/confirm/{id}")
    public String confirmAppointment(@PathVariable Long id) {

        appointmentService.updateStatus(id, "CONFIRMED");

        return "redirect:/appointments";
    }

    // Reject appointment
    @GetMapping("/appointment/reject/{id}")
    public String rejectAppointment(@PathVariable Long id) {

        appointmentService.updateStatus(id, "REJECTED");

        return "redirect:/appointments";
    }
}