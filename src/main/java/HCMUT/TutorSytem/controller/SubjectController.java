package HCMUT.TutorSytem.controller;

import HCMUT.TutorSytem.dto.SubjectDTO;
import HCMUT.TutorSytem.model.Subject;
import HCMUT.TutorSytem.payload.response.BaseResponse;
import HCMUT.TutorSytem.repo.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();

        List<SubjectDTO> subjectDTOs = subjects.stream()
                .map(subject -> {
                    SubjectDTO dto = new SubjectDTO();
                    dto.setId(subject.getId());
                    dto.setName(subject.getName());
                    return dto;
                })
                .collect(Collectors.toList());

        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setMessage("Subjects retrieved successfully");
        response.setData(subjectDTOs);
        return ResponseEntity.ok(response);
    }
}

