package HCMUT.TutorSytem.repo;

import HCMUT.TutorSytem.model.StudentSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentSessionRepository extends JpaRepository<StudentSession, Long> {
    List<StudentSession> findByStudentId(Long studentId);
    List<StudentSession> findBySessionId(Long sessionId);
    Optional<StudentSession> findByStudentIdAndSessionId(Long studentId, Long sessionId);

    // Use studentSessionStatus.id instead of status string
    List<StudentSession> findByStudentSessionStatusId(Byte statusId);

    // Or use studentSessionStatus.name if needed
    List<StudentSession> findByStudentSessionStatusName(String statusName);
}

