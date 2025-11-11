package HCMUT.TutorSytem.service;

import HCMUT.TutorSytem.dto.PageDTO;
import HCMUT.TutorSytem.dto.TutorDTO;
import HCMUT.TutorSytem.payload.request.TutorRequest;

public interface TutorService {
    PageDTO<TutorDTO> getAllTutors(int page, int size);
    TutorDTO createTutor(TutorRequest request);
    TutorDTO updateTutor(Long id, TutorRequest request);
    void deleteTutor(Long id);
    Long getUserIdFromTutorProfile(Long tutorProfileId); // Get user ID to check ownership
}

