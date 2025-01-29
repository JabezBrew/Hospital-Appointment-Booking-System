package AppointToDoctorRestService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRegistrationRequest {
    private String username;
    private String email;
    private String password;
    private String specialization;
    private String qualification;
    private String experience;
    private String consultationFee;
}
