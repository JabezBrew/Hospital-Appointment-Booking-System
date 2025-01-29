package AppointToDoctorRestService.repo;

import AppointToDoctorRestService.entities.Appointment;
import AppointToDoctorRestService.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    @Query("SELECT NEW map(a.date as date, COUNT(a) as count) FROM Appointment a GROUP BY a.date ORDER BY a.date")
    List<Map<String, Long>> countAppointmentsByDate();

    @Query("SELECT NEW map(a.doctor.user.username as doctor, COUNT(a) as count) FROM Appointment a GROUP BY a.doctor.user.username ORDER BY a.doctor.user.username")
    List<Map<String, Long>> countAppointmentsByDoctor();

    List<Appointment> findByDoctorOrderByDateAsc(Doctor doctor);
}
