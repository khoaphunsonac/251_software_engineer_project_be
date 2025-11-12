package HCMUT.TutorSytem.controller;

import HCMUT.TutorSytem.dto.SessionStatusDTO;
import HCMUT.TutorSytem.model.SessionStatus;
import HCMUT.TutorSytem.payload.response.BaseResponse;
import HCMUT.TutorSytem.repo.SessionStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/session-statuses")
public class SessionStatusController {

    @Autowired
    private SessionStatusRepository sessionStatusRepository;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllSessionStatuses() {
        List<SessionStatus> statuses = sessionStatusRepository.findAll();

        List<SessionStatusDTO> statusDTOs = statuses.stream()
                .map(status -> {
                    SessionStatusDTO dto = new SessionStatusDTO();
                    dto.setId(status.getId());
                    dto.setName(status.getName());
                    dto.setDescription(status.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());

        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setMessage("Session statuses retrieved successfully");
        response.setData(statusDTOs);
        return ResponseEntity.ok(response);
    }
}

