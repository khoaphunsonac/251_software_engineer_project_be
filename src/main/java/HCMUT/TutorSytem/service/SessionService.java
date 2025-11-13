package HCMUT.TutorSytem.service;

import HCMUT.TutorSytem.dto.PageDTO;
import HCMUT.TutorSytem.dto.SessionDTO;
import HCMUT.TutorSytem.payload.request.SessionRequest;

public interface SessionService {
    PageDTO<SessionDTO> getAllSessions(int page, int size);
    SessionDTO createSession(SessionRequest request);
    SessionDTO updateSession(Long id, SessionRequest request);
    void deleteSession(Long id);
    Long getTutorIdFromSession(Long sessionId); // Get tutor ID to check ownership
}

