package HCMUT.TutorSytem.repo;

import HCMUT.TutorSytem.model.HcmutSso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface HcmutSsoRepository extends JpaRepository<HcmutSso, Long> {
    Optional<HcmutSso> findByEmail(String email);
}
