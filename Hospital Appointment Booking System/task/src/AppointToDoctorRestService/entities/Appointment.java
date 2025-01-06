package AppointToDoctorRestService.entities;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long idApp;

    @NotNull
    @NotEmpty
    @NotBlank
    private String doctor;
    @NotNull
    @NotEmpty
    @NotBlank
    private String patient;

    @Pattern(regexp = "^[0-9]{4}-[0-1][0-9]-[0-3][0-9]$")
    private String date;
}
