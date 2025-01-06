package AppointToDoctorRestService.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import AppointToDoctorRestService.entities.Doctor;

import jakarta.transaction.Transactional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByDoctorName(String doctorName);
    Doctor findByDoctorName(String doctorName);
    @Transactional
    void deleteByDoctorName(String doctorName);


}
