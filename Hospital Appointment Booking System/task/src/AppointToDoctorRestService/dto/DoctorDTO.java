package AppointToDoctorRestService.dto;

import AppointToDoctorRestService.entities.Doctor;

public record DoctorDTO(Long id, String doctorName) {
    public static DoctorDTO mapToDTO(Doctor doctor) {
        return new DoctorDTO(doctor.getId(), doctor.getDoctorName());
    }
}
