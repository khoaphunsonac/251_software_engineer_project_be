package HCMUT.TutorSytem.service.imp;

import HCMUT.TutorSytem.dto.PageDTO;
import HCMUT.TutorSytem.dto.SessionDTO;
import HCMUT.TutorSytem.exception.DataNotFoundExceptions;
import HCMUT.TutorSytem.mapper.SessionMapper;
import HCMUT.TutorSytem.model.Session;
import HCMUT.TutorSytem.model.Subject;
import HCMUT.TutorSytem.model.User;
import HCMUT.TutorSytem.payload.request.SessionRequest;
import HCMUT.TutorSytem.repo.SessionRepository;
import HCMUT.TutorSytem.repo.SubjectRepository;
import HCMUT.TutorSytem.repo.UserRepository;
import HCMUT.TutorSytem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionServiceImp implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SessionMapper sessionMapper;

    @Override
    public PageDTO<SessionDTO> getAllSessions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Session> sessionPage = sessionRepository.findAll(pageable);

        List<SessionDTO> sessionDTOs = sessionPage.getContent().stream()
                .map(sessionMapper::toDTO)
                .collect(Collectors.toList());

        PageDTO<SessionDTO> pageDTO = new PageDTO<>();
        pageDTO.setContent(sessionDTOs);
        pageDTO.setPageNumber(sessionPage.getNumber());
        pageDTO.setPageSize(sessionPage.getSize());
        pageDTO.setTotalElements(sessionPage.getTotalElements());
        pageDTO.setTotalPages(sessionPage.getTotalPages());
        pageDTO.setFirst(sessionPage.isFirst());
        pageDTO.setLast(sessionPage.isLast());
        pageDTO.setEmpty(sessionPage.isEmpty());

        return pageDTO;
    }

    @Override
    public SessionDTO createSession(SessionRequest request) {
        Session session = new Session();

        // Set tutor
        if (request.getTutorId() != null) {
            User tutor = userRepository.findById(request.getTutorId())
                    .orElseThrow(() -> new DataNotFoundExceptions("Tutor not found with id: " + request.getTutorId()));
            session.setTutor(tutor);
        }

        // Set student
        if (request.getStudentId() != null) {
            User student = userRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new DataNotFoundExceptions("Student not found with id: " + request.getStudentId()));
            session.setStudent(student);
        }

        // Set subject
        if (request.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new DataNotFoundExceptions("Subject not found with id: " + request.getSubjectId()));
            session.setSubject(subject);
        }

        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setFormat(request.getFormat());
        session.setLocation(request.getLocation());
        session.setStatus(request.getStatus() != null ? request.getStatus() : "scheduled");
        session.setCreatedDate(Instant.now());

        session = sessionRepository.save(session);
        return sessionMapper.toDTO(session);
    }

    @Override
    public SessionDTO updateSession(Long id, SessionRequest request) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundExceptions("Session not found with id: " + id));

        // Update tutor if provided
        if (request.getTutorId() != null) {
            User tutor = userRepository.findById(request.getTutorId())
                    .orElseThrow(() -> new DataNotFoundExceptions("Tutor not found with id: " + request.getTutorId()));
            session.setTutor(tutor);
        }

        // Update student if provided
        if (request.getStudentId() != null) {
            User student = userRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new DataNotFoundExceptions("Student not found with id: " + request.getStudentId()));
            session.setStudent(student);
        }

        // Update subject if provided
        if (request.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new DataNotFoundExceptions("Subject not found with id: " + request.getSubjectId()));
            session.setSubject(subject);
        }

        if (request.getStartTime() != null) session.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) session.setEndTime(request.getEndTime());
        if (request.getFormat() != null) session.setFormat(request.getFormat());
        if (request.getLocation() != null) session.setLocation(request.getLocation());
        if (request.getStatus() != null) session.setStatus(request.getStatus());
        session.setUpdatedDate(Instant.now());

        session = sessionRepository.save(session);
        return sessionMapper.toDTO(session);
    }

    @Override
    public void deleteSession(Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new DataNotFoundExceptions("Session not found with id: " + id);
        }
        sessionRepository.deleteById(id);
    }

    @Override
    public Long getTutorIdFromSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new DataNotFoundExceptions("Session not found with id: " + sessionId));
        return session.getTutor() != null ? session.getTutor().getId() : null;
    }
}

