package AppointToDoctorRestService.service;

import AppointToDoctorRestService.dto.AuthenticationRequest;
import AppointToDoctorRestService.dto.AuthenticationResponse;
import AppointToDoctorRestService.dto.RegisterRequest;
import AppointToDoctorRestService.entities.User;
import AppointToDoctorRestService.repo.UserRepository;
import AppointToDoctorRestService.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .role(request.getRole())
                .build();
        repository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwtToken = jwtUtil.generateJwtToken(authentication);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}