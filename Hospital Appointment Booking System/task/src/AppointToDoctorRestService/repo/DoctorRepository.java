package AppointToDoctorRestService.repo;

import AppointToDoctorRestService.entities.Doctor;
import AppointToDoctorRestService.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    
    @Query("SELECT d FROM Doctor d WHERE d.specialization = :specialization")
    List<Doctor> findBySpecialization(String specialization);
    
    boolean existsByUser(User user);
    
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.user.username) = LOWER(:username)")
    Optional<Doctor> findByUsername(String username);
    
    @Query("SELECT d FROM Doctor d WHERE d.consultationFee <= :maxFee")
    List<Doctor> findByConsultationFeeLessThanEqual(String maxFee);
}
