package HCMUT.TutorSytem.mapper;

import HCMUT.TutorSytem.dto.SessionDTO;
import HCMUT.TutorSytem.model.Session;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionMapper {

    public SessionDTO toDTO(Session session) {
        if (session == null) {
            return null;
        }

        SessionDTO dto = new SessionDTO();
        dto.setId(session.getId());

        // Map tutor name
        if (session.getTutor() != null) {
            dto.setTutorName(session.getTutor().getFirstName() + " " + session.getTutor().getLastName());
        }

        // Map student name
        if (session.getStudent() != null) {
            dto.setStudentName(session.getStudent().getFirstName() + " " + session.getStudent().getLastName());
        }

        // Map subject name
        if (session.getSubject() != null) {
            dto.setSubjectName(session.getSubject().getName());
        }

        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        dto.setFormat(session.getFormat());
        dto.setLocation(session.getLocation());
        dto.setStatus(session.getStatus());
        dto.setCreatedDate(session.getCreatedDate());
        dto.setUpdatedDate(session.getUpdatedDate());

        return dto;
    }

    public List<SessionDTO> toDTOList(List<Session> sessions) {
        if (sessions == null) {
            return Collections.emptyList();
        }
        return sessions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

