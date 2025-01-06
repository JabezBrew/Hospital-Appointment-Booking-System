package AppointToDoctorRestService.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import AppointToDoctorRestService.dto.DoctorDTO;
import AppointToDoctorRestService.entities.AvailableDates;
import AppointToDoctorRestService.entities.Doctor;
import AppointToDoctorRestService.service.DoctorService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/newDoctor")
    public DoctorDTO addNewDoctor(@RequestBody @Valid Doctor doctor) {
        return doctorService.addNewDoctor(doctor);
    }

    @GetMapping("/allDoctorslist")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/availableDatesByDoctor")
    public ResponseEntity<List<AvailableDates>> getAllAvailableDateForDoctor(@RequestParam String doc) {
        return doctorService.getAvailableDatesForADoctor(doc.toLowerCase());
    }

    @DeleteMapping("/deleteDoctor")
    public DoctorDTO deleteDoctor(@RequestParam String doc) {
        return doctorService.deleteDoctor(doc);
    }

}
