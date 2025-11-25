package HCMUT.TutorSytem.service;

import HCMUT.TutorSytem.dto.SessionDTO;
import HCMUT.TutorSytem.dto.StudentSessionDTO;
import HCMUT.TutorSytem.dto.TutorDTO;
import HCMUT.TutorSytem.dto.TutorDetailDTO;
import HCMUT.TutorSytem.payload.request.TutorProfileUpdateRequest;
import HCMUT.TutorSytem.payload.request.TutorRequest;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TutorService {
    Page<TutorDTO> getAllTutors(Pageable pageable);
    TutorDTO createTutor(TutorRequest request);
    TutorDTO updateTutor(Integer id, TutorRequest request);
    void deleteTutor(Integer id);
    Integer getUserIdFromTutorProfile(Integer tutorProfileId); // Get user ID to check ownership

    // New methods for profile management
    TutorDetailDTO getTutorDetail(Integer userId);
    TutorDetailDTO updateTutorProfile(Integer userId, TutorProfileUpdateRequest request);
    // API mới cho tutor duyệt/từ chối student
    Page<StudentSessionDTO> getPendingStudentSessions(Integer tutorId, Pageable pageable);
    StudentSessionDTO approveStudentSession(Integer tutorId, Integer studentSessionId); // Approve student
    StudentSessionDTO rejectStudentSession(Integer tutorId, Integer studentSessionId); // Reject student
    List<StudentSessionDTO> approveStudentSessions(Integer tutorId, List<Integer> studentSessionIds); // Approve nhiều student cùng lúc
    List<StudentSessionDTO> rejectStudentSessions(Integer tutorId, List<Integer> studentSessionIds); // Reject nhiều student cùng lúc

    // Get schedule for a week offset (0 current, 1 next..)
    List<SessionDTO> getWeekSchedule(Integer tutorId, Integer weekOffset);

}

