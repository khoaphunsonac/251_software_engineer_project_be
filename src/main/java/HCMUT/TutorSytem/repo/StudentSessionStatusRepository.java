package HCMUT.TutorSytem.repo;

import HCMUT.TutorSytem.model.StudentSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentSessionStatusRepository extends JpaRepository<StudentSessionStatus, Byte> {
    Optional<StudentSessionStatus> findByName(String name);
}

