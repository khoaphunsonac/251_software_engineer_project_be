package HCMUT.TutorSytem.repo;

import HCMUT.TutorSytem.model.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionStatusRepository extends JpaRepository<SessionStatus, Byte> {
    Optional<SessionStatus> findByName(String name);
}

