package AppointToDoctorRestService.dto;

import AppointToDoctorRestService.entities.AvailableDates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
    private Long id;
    private String username;
    private String email;
    private String specialization;
    private String qualification;
    private String experience;
    private String consultationFee;
    private List<AvailableDates> availableDates;
}
