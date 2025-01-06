package AppointToDoctorRestService.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import AppointToDoctorRestService.entities.Appointment;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Modifying
    @Transactional
    @Query("Update Appointment a set a.doctor = 'director' where a.doctor = ?1")
    void updateDoctorName(String doctorName);
    @Query("SELECT a.date, COUNT(a) FROM Appointment a GROUP BY a.date ORDER BY a.date")
    List<Object[]> countAppointmentsByDate();
    @Query("SELECT a.doctor, COUNT(a) FROM Appointment a GROUP BY a.doctor")
    List<Object[]> countAppointmentByDoctor();
    @Transactional
    void deleteAppointmentsByDoctor(@NotNull @NotEmpty @NotBlank String doctor);
}
