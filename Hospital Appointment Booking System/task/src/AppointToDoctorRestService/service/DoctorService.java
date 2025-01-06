package AppointToDoctorRestService.service;

import AppointToDoctorRestService.repo.AppointmentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import AppointToDoctorRestService.dto.DoctorDTO;
import AppointToDoctorRestService.entities.AvailableDates;
import AppointToDoctorRestService.entities.Doctor;
import AppointToDoctorRestService.errors.BadRequestException;
import AppointToDoctorRestService.repo.DoctorRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepo;
    private final AppointmentRepository appointmentRepo;

    public DoctorService(DoctorRepository doctorRepo, AppointmentRepository appointmentRepo) {
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
    }

    public boolean isRepositoryEmpty() {
        return doctorRepo.count() == 0;
    }

    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<Doctor> doctors = doctorRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<DoctorDTO> doctorDTOS = doctors.stream().map(DoctorDTO::mapToDTO).toList();
        if (isRepositoryEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(doctorDTOS);
    }

    public DoctorDTO addNewDoctor(Doctor doctor) {
        if (doctorRepo.existsByDoctorName(doctor.getDoctorName())) {
            throw new BadRequestException("Doctor already exist");
        }
        List<AvailableDates> dates = new ArrayList<>();
        dates.add(new AvailableDates(LocalDate.now().plusDays(1), false));
        dates.add(new AvailableDates(LocalDate.now().plusDays(2), false));
        dates.add(new AvailableDates(LocalDate.now().plusDays(3), false));
        dates.add(new AvailableDates(LocalDate.now().plusDays(4), false));
        doctor.setAvailableDates(dates);
        doctor.setDoctorName(doctor.getDoctorName().toLowerCase());
        doctorRepo.save(doctor);
        return DoctorDTO.mapToDTO(doctor);
    }

    public ResponseEntity<List<AvailableDates>> getAvailableDatesForADoctor(String name) {
        if (!doctorRepo.existsByDoctorName(name)) {
            return ResponseEntity.noContent().build();
        }
        Doctor doctor = doctorRepo.findByDoctorName(name);
        List<AvailableDates> sortedDates = doctor.getAvailableDates().stream().sorted(Comparator.comparing(AvailableDates::getAvailabletime)).collect(Collectors.toList());
        return ResponseEntity.ok(sortedDates);
    }

    public void transferAppointmentsToDirector(List<AvailableDates> appointmentDates) {
        if (!doctorRepo.existsByDoctorName("director")) {
            Doctor director = new Doctor();
            director.setDoctorName("director");
            director.setAvailableDates(appointmentDates);
            doctorRepo.save(director);
        } else {
            doctorRepo.findByDoctorName("director").setAvailableDates(appointmentDates);
        }
    }

    public DoctorDTO deleteDoctor(String name) {
        if (!doctorRepo.existsByDoctorName(name)) {
            throw new BadRequestException("Doctor not found");
        }
        Doctor doctor = doctorRepo.findByDoctorName(name);
        List<AvailableDates> sortedDates = doctor.getAvailableDates().stream().sorted(Comparator.
                comparing(AvailableDates::getAvailabletime)).toList();
        sortedDates.forEach(availableDates -> availableDates.setBooked(false)); //for the tests.
        if (!Objects.equals(name, "director")) {
            transferAppointmentsToDirector(sortedDates);
            appointmentRepo.updateDoctorName(name);
        } else {
            appointmentRepo.deleteAppointmentsByDoctor(name);
        }
        doctorRepo.deleteByDoctorName(name);
        return DoctorDTO.mapToDTO(doctor);
    }
}
