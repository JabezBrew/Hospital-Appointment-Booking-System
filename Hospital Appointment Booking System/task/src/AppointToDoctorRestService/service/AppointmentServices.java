package AppointToDoctorRestService.service;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import AppointToDoctorRestService.entities.Appointment;
import AppointToDoctorRestService.entities.AvailableDates;
import AppointToDoctorRestService.entities.Doctor;
import AppointToDoctorRestService.entities.User;
import AppointToDoctorRestService.errors.BadRequestException;
import AppointToDoctorRestService.repo.AppointmentRepository;
import AppointToDoctorRestService.repo.DoctorRepository;
import AppointToDoctorRestService.repo.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AppointmentServices {

    private final AppointmentRepository appointmentRepo;
    private final DoctorRepository doctorRepo;
    private final UserRepository userRepository;

    public ResponseEntity<?> getAllAppointments() {
        if (isRepositoryEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointmentRepo.findAll(Sort.by(Sort.Direction.ASC, "idApp")));
    }

    public boolean isRepositoryEmpty() {
        return appointmentRepo.count() == 0;
    }

    public Appointment setAppointment(Appointment appointment) {
        // Find doctor by username
        Doctor doctor = doctorRepo.findByUsername(appointment.getDoctor().getUser().getUsername().toLowerCase())
                .orElseThrow(() -> new BadRequestException("Doctor not found"));

        // Check if username is "director"
        if (doctor.getUser().getUsername().equalsIgnoreCase("director")) {
            throw new BadRequestException("Cannot make appointment with director");
        }

        // Find available date
        for (AvailableDates date : doctor.getAvailableDates()) {
            if (date.getAvailabletime().toString().equals(appointment.getDate()) && !date.isBooked()) {
                date.setBooked(true);
                appointment.setDoctor(doctor);
                appointmentRepo.save(appointment);
                doctorRepo.save(doctor);
                return appointment;
            }
        }
        throw new BadRequestException("No available dates found for this doctor");
    }

    public ResponseEntity<?> deleteAppointmentById(long id) {
        if (!appointmentRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Appointment not found!"));
        }
        Appointment appointment = appointmentRepo.findById(id).get();
        appointmentRepo.deleteById(id);
        // free doctor's time
        Doctor doctor = doctorRepo.findByUsername(appointment.getDoctor().getUser().getUsername()).orElseThrow();
        for (AvailableDates date: doctor.getAvailableDates()) {
            if (Objects.equals(date.getAvailabletime().toString(), appointment.getDate())) {
                date.setBooked(false);
                doctorRepo.save(doctor);
                break;
            }
        }
        return ResponseEntity.ok(Map.of("message", "The appointment has been deleted!"));
    }

    public ResponseEntity<List<Map<String, Long>>> getAppointmentCountPerDay() {
        if (isRepositoryEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointmentRepo.countAppointmentsByDate());
    }

    public ResponseEntity<List<Map<String, Long>>> getAppointmentCountPerDoctor() {
        if (isRepositoryEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointmentRepo.countAppointmentsByDoctor());
    }
}
