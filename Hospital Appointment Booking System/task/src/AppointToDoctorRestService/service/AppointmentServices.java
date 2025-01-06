package AppointToDoctorRestService.service;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import AppointToDoctorRestService.entities.Appointment;
import AppointToDoctorRestService.entities.AvailableDates;
import AppointToDoctorRestService.entities.Doctor;
import AppointToDoctorRestService.errors.BadRequestException;
import AppointToDoctorRestService.repo.AppointmentRepository;
import AppointToDoctorRestService.repo.DoctorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AppointmentServices {

    private final AppointmentRepository appointmentRepo;
    private final DoctorRepository doctorRepo;

    public AppointmentServices(AppointmentRepository appointmentRepo, DoctorRepository doctorRepo) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
    }

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
        appointment.setDoctor(appointment.getDoctor().toLowerCase());
        appointment.setPatient(appointment.getPatient().toLowerCase());
        if (doctorRepo.existsByDoctorName(appointment.getDoctor()) && !appointment.getDoctor()
                .equalsIgnoreCase("director")) {

            Doctor doctor = doctorRepo.findByDoctorName(appointment.getDoctor());
            for (AvailableDates date: doctor.getAvailableDates()) {
                System.out.println(date.getAvailabletime().toString());
                System.out.println(appointment.getDate());
                System.out.println(date.getAvailabletime().toString().equals(appointment.getDate()) + " " +!date.isBooked() );
                if (Objects.equals(date.getAvailabletime().toString(), appointment.getDate()) && !date.isBooked()) {
                    appointmentRepo.save(appointment);
                    date.setBooked(true);
                    doctorRepo.save(doctor);
                    return appointment;
                }
            }
        }
        throw new BadRequestException("Doctor Not On Our Service");
    }

    public ResponseEntity<?> deleteAppointmentById(Long id) {
        if (!appointmentRepo.existsById(id)) {
            return new ResponseEntity<>(Map.of("error", "The appointment does not exist or was already " +
                    "cancelled"), HttpStatus.BAD_REQUEST);
        }
        Appointment appointment = appointmentRepo.findById(id).get();
        appointmentRepo.deleteById(id);
        // free doctor's time
        Doctor doctor = doctorRepo.findByDoctorName(appointment.getDoctor());
        for (AvailableDates date: doctor.getAvailableDates()) {
            if (Objects.equals(date.getAvailabletime().toString(), appointment.getDate())) {
                date.setBooked(false);
                doctorRepo.save(doctor);
                break;
            }
        }
        return ResponseEntity.ok(appointment);
    }

    // getAppointmentCountPerDay and getAppointmentCountPerDoctor methods are for stage 4.
    public ResponseEntity<List<Map<String, Long>>> getAppointmentCountPerDay() {
        if (isRepositoryEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<Map<String, Long>> appointments = new ArrayList<>();
        appointmentRepo.countAppointmentsByDate().forEach(
                (record) -> appointments.add(Map.of(record[0].toString(), Long.parseLong(record[1].toString())))
        );
        return ResponseEntity.ok(appointments);
    }

    public ResponseEntity<List<Map<String, Long>>> getAppointmentCountPerDoctor() {
        if (isRepositoryEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<Map<String, Long>> appointments = new ArrayList<>();
        appointmentRepo.countAppointmentByDoctor().forEach(
                (record) -> appointments.add(Map.of(record[0].toString(), Long.parseLong(record[1].toString())))
        );
        return ResponseEntity.ok(appointments);
    }
}
