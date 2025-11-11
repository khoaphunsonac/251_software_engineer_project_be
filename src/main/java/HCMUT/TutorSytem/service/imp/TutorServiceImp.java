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
        // Create a new user for the tutor
        User user = new User();
        user.setRole("tutor");
        user.setFirstName(request.getName().split(" ")[0]);
        user.setLastName(request.getName().substring(request.getName().indexOf(" ") + 1));
        user.setAcademicStatus(request.getTitle());

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
        tutorProfile.setRating(request.getRating() != null ? BigDecimal.valueOf(request.getRating()) : null);
        tutorProfile.setTotalSessionsCompleted(request.getStudentCount() != null ? request.getStudentCount().longValue() : 0L);
        tutorProfile.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);

        tutorProfile = tutorProfileRepository.save(tutorProfile);
        return tutorMapper.toDTO(tutorProfile);
    }

    @Override
    public TutorDTO updateTutor(Long id, TutorRequest request) {
        TutorProfile tutorProfile = tutorProfileRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundExceptions("Tutor not found with id: " + id));

        User user = tutorProfile.getUser();
        user.setFirstName(request.getName().split(" ")[0]);
        user.setLastName(request.getName().substring(request.getName().indexOf(" ") + 1));
        user.setAcademicStatus(request.getTitle());

        // Update major if provided
        if (request.getMajorId() != null) {
            Major major = majorRepository.findById(request.getMajorId())
                    .orElseThrow(() -> new DataNotFoundExceptions("Major not found with id: " + request.getMajorId()));
            user.setMajor(major);
        }

        userRepository.save(user);

        // Handle subjects - replace with subjects by IDs (ManyToMany)
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

        tutorProfile.setExperienceYears(request.getExperienceYears() != null ? request.getExperienceYears().shortValue() : null);
        tutorProfile.setBio(request.getDescription());
        tutorProfile.setRating(request.getRating() != null ? BigDecimal.valueOf(request.getRating()) : null);
        tutorProfile.setTotalSessionsCompleted(request.getStudentCount() != null ? request.getStudentCount().longValue() : 0L);
        tutorProfile.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);

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

