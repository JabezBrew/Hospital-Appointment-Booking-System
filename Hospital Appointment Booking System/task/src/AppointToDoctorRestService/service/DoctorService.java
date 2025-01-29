package AppointToDoctorRestService.service;

import AppointToDoctorRestService.dto.DoctorRegistrationRequest;
import AppointToDoctorRestService.dto.DoctorResponse;
import AppointToDoctorRestService.entities.Doctor;
import AppointToDoctorRestService.entities.Role;
import AppointToDoctorRestService.entities.User;
import AppointToDoctorRestService.errors.BadRequestException;
import AppointToDoctorRestService.repo.AppointmentRepository;
import AppointToDoctorRestService.repo.DoctorRepository;
import AppointToDoctorRestService.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepo;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorResponse registerDoctor(DoctorRegistrationRequest request) {
        System.out.println("Doctor Registration Request: " + request);
        // First check if user already exists
        if (userRepository.existsByUsername(request.getUsername()) ||
            userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Username or email already exists");
        }
        // Create user with DOCTOR role
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.DOCTOR)
                .enabled(true)
                .build();
        
        userRepository.save(user);
        // Check if doctor profile already exists for this user
        if (doctorRepo.existsByUser(user)) {
            throw new RuntimeException("Doctor profile already exists for this user");
        }
        // Create doctor profile
        Doctor doctor = Doctor.builder()
                .user(user)
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experience(request.getExperience())
                .consultationFee(request.getConsultationFee())
                .build();

        Doctor savedDoctor = doctorRepo.save(doctor);
        return mapToDoctorResponse(savedDoctor);
    }

    public List<DoctorResponse> getAllDoctors() {
        return doctorRepo.findAll().stream()
                .map(this::mapToDoctorResponse)
                .collect(Collectors.toList());
    }

    public List<DoctorResponse> getDoctorsBySpecialization(String specialization) {
        return doctorRepo.findBySpecialization(specialization).stream()
                .map(this::mapToDoctorResponse)
                .collect(Collectors.toList());
    }

    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return mapToDoctorResponse(doctor);
    }

    public DoctorResponse updateDoctor(Long id, DoctorRegistrationRequest request) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        User user = doctor.getUser();
        if (user.getRole() != Role.DOCTOR) {
            throw new RuntimeException("User does not have doctor privileges");
        }

        if (!user.getUsername().equals(request.getUsername()) && 
            userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        if (!user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already taken");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);

        doctor.setSpecialization(request.getSpecialization());
        doctor.setQualification(request.getQualification());
        doctor.setExperience(request.getExperience());
        doctor.setConsultationFee(request.getConsultationFee());

        Doctor updatedDoctor = doctorRepo.save(doctor);
        return mapToDoctorResponse(updatedDoctor);
    }

    private DoctorResponse mapToDoctorResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .username(doctor.getUser().getUsername())
                .email(doctor.getUser().getEmail())
                .specialization(doctor.getSpecialization())
                .qualification(doctor.getQualification())
                .experience(doctor.getExperience())
                .consultationFee(doctor.getConsultationFee())
                .availableDates(doctor.getAvailableDates())
                .build();
    }

    public List<DoctorResponse> getAllDoctorsAuth() {
        return doctorRepo.findAll().stream()
                .map(this::mapToDoctorResponse)
                .collect(Collectors.toList());
    }
}
