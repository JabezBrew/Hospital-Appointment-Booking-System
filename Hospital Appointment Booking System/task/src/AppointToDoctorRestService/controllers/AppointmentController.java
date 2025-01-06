package AppointToDoctorRestService.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import AppointToDoctorRestService.service.AppointmentServices;
import AppointToDoctorRestService.entities.Appointment;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class AppointmentController {
    private final AppointmentServices appointmentServices;

    public AppointmentController(AppointmentServices appointmentServices) {
        this.appointmentServices = appointmentServices;
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> getAppointmentList() {
        return appointmentServices.getAllAppointments();
    }

    @PostMapping("/setAppointment")
    public Appointment setAppointment(@RequestBody @Valid Appointment appointment) {
        return appointmentServices.setAppointment(appointment);
    }

    @DeleteMapping("/deleteAppointment")
    public ResponseEntity<?> deleteAppointment(@RequestParam long id) {
        return appointmentServices.deleteAppointmentById(id);
    }

    @GetMapping("/statisticsDay")
    public ResponseEntity<List<Map<String, Long>>> getStatisticsPerDay() {
        return appointmentServices.getAppointmentCountPerDay();
    }

    @GetMapping("/statisticsDoc")
    public ResponseEntity<List<Map<String, Long>>> getStatisticsPerDoctor() {
        return appointmentServices.getAppointmentCountPerDoctor();
    }

}

