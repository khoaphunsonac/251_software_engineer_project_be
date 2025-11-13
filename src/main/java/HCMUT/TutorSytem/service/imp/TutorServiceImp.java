package HCMUT.TutorSytem.service.imp;

import HCMUT.TutorSytem.dto.PageDTO;
import HCMUT.TutorSytem.dto.TutorDTO;
import HCMUT.TutorSytem.exception.DataNotFoundExceptions;
import HCMUT.TutorSytem.mapper.TutorMapper;
import HCMUT.TutorSytem.model.Major;
import HCMUT.TutorSytem.model.Subject;
import HCMUT.TutorSytem.model.TutorProfile;
import HCMUT.TutorSytem.model.User;
import HCMUT.TutorSytem.payload.request.TutorRequest;
import HCMUT.TutorSytem.repo.MajorRepository;
import HCMUT.TutorSytem.repo.SubjectRepository;
import HCMUT.TutorSytem.repo.TutorProfileRepository;
import HCMUT.TutorSytem.repo.UserRepository;
import HCMUT.TutorSytem.service.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TutorServiceImp implements TutorService {

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TutorMapper tutorMapper;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Override
    public PageDTO<TutorDTO> getAllTutors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TutorProfile> tutorPage = tutorProfileRepository.findAll(pageable);

        List<TutorDTO> tutorDTOs = tutorPage.getContent().stream()
                .map(tutorMapper::toDTO)
                .collect(Collectors.toList());

        PageDTO<TutorDTO> pageDTO = new PageDTO<>();
        pageDTO.setContent(tutorDTOs);
        pageDTO.setPageNumber(tutorPage.getNumber());
        pageDTO.setPageSize(tutorPage.getSize());
        pageDTO.setTotalElements(tutorPage.getTotalElements());
        pageDTO.setTotalPages(tutorPage.getTotalPages());
        pageDTO.setFirst(tutorPage.isFirst());
        pageDTO.setLast(tutorPage.isLast());
        pageDTO.setEmpty(tutorPage.isEmpty());

        return pageDTO;
    }

    @Override
    public TutorDTO createTutor(TutorRequest request) {
        // Assuming the current authenticated user is creating the tutor profile
        // TODO: Get current user from authentication context
        // For now, this will throw an error - need to get userId from JWT token
        // User user = getCurrentAuthenticatedUser();

        // Temporary: If you need to specify userId, add it to TutorRequest
        // For now, create a minimal user (this should be replaced with actual auth user)
        User user = new User();
        user.setRole("tutor");
        // Name comes from User datacore, not from request
        // user.setFirstName, user.setLastName should be already set

        // Set major if provided
        if (request.getMajorId() != null) {
            Major major = majorRepository.findById(request.getMajorId())
                    .orElseThrow(() -> new DataNotFoundExceptions("Major not found with id: " + request.getMajorId()));
            user.setMajor(major);
        }

        user = userRepository.save(user);

        // Create tutor profile
        TutorProfile tutorProfile = new TutorProfile();
        tutorProfile.setUser(user);

        // Handle subjects - link by subject IDs (ManyToMany)
        if (request.getSubjects() != null && !request.getSubjects().isEmpty()) {
            for (Long subjectId : request.getSubjects()) {
                Subject subject = subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new DataNotFoundExceptions("Subject not found with id: " + subjectId));
                tutorProfile.getSubjects().add(subject);
            }
        }

        tutorProfile.setExperienceYears(request.getExperienceYears() != null ? request.getExperienceYears().shortValue() : null);
        tutorProfile.setBio(request.getDescription());
        tutorProfile.setRating(BigDecimal.ZERO); // Auto set to 0, calculated from reviews
        tutorProfile.setTotalSessionsCompleted(0L); // Auto set to 0
        tutorProfile.setIsAvailable(true); // Auto set to true

        tutorProfile = tutorProfileRepository.save(tutorProfile);
        return tutorMapper.toDTO(tutorProfile);
    }

    @Override
    public TutorDTO updateTutor(Long id, TutorRequest request) {
        TutorProfile tutorProfile = tutorProfileRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundExceptions("Tutor not found with id: " + id));

        // Cannot update User fields (from Datacore) - they are managed separately
        // Only update TutorProfile specific fields

        // Update major if provided (this might be allowed or not, depending on business logic)
        User user = tutorProfile.getUser();
        if (request.getMajorId() != null) {
            Major major = majorRepository.findById(request.getMajorId())
                    .orElseThrow(() -> new DataNotFoundExceptions("Major not found with id: " + request.getMajorId()));
            user.setMajor(major);
            userRepository.save(user);
        }

        // Handle subjects - only update if provided and not empty
        if (request.getSubjects() != null && !request.getSubjects().isEmpty()) {
            // Clear existing subjects
            tutorProfile.getSubjects().clear();

            // Add subjects by IDs
            for (Long subjectId : request.getSubjects()) {
                Subject subject = subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new DataNotFoundExceptions("Subject not found with id: " + subjectId));
                tutorProfile.getSubjects().add(subject);
            }
        }

        // Update TutorProfile fields - only if not null and not empty
        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            user.setAcademicStatus(request.getTitle().trim());
            userRepository.save(user);
        }

        if (request.getExperienceYears() != null) {
            tutorProfile.setExperienceYears(request.getExperienceYears().shortValue());
        }

        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            tutorProfile.setBio(request.getDescription().trim());
        }

        // Rating is calculated from reviews, cannot be manually updated
        // isAvailable can be toggled by user (could add separate field in request if needed)
        // Do not update: name, phone, dob, hcmutId, etc. (from Datacore)

        tutorProfile = tutorProfileRepository.save(tutorProfile);
        return tutorMapper.toDTO(tutorProfile);
    }

    @Override
    public void deleteTutor(Long id) {
        if (!tutorProfileRepository.existsById(id)) {
            throw new DataNotFoundExceptions("Tutor not found with id: " + id);
        }
        tutorProfileRepository.deleteById(id);
    }

    @Override
    public Long getUserIdFromTutorProfile(Long tutorProfileId) {
        TutorProfile tutorProfile = tutorProfileRepository.findById(tutorProfileId)
                .orElseThrow(() -> new DataNotFoundExceptions("Tutor profile not found with id: " + tutorProfileId));
        return tutorProfile.getUser() != null ? tutorProfile.getUser().getId() : null;
    }
}

