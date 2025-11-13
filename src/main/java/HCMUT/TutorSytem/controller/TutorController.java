package HCMUT.TutorSytem.controller;

import HCMUT.TutorSytem.dto.PageDTO;
import HCMUT.TutorSytem.dto.TutorDTO;
import HCMUT.TutorSytem.payload.request.TutorRequest;
import HCMUT.TutorSytem.payload.response.BaseResponse;
import HCMUT.TutorSytem.service.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tutors")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllTutors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageDTO<TutorDTO> tutorPage = tutorService.getAllTutors(page, size);
        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setMessage("Tutors retrieved successfully");
        response.setData(tutorPage);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createTutor(@RequestBody TutorRequest tutorRequest) {
        TutorDTO tutor = tutorService.createTutor(tutorRequest);
        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setMessage("Tutor created successfully");
        response.setData(tutor);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateTutor(@PathVariable Long id, @RequestBody TutorRequest tutorRequest) {
        // Check ownership: only the tutor owner can update their profile
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Long currentUserId = getCurrentUserId(authentication);
            Long profileUserId = tutorService.getUserIdFromTutorProfile(id);

            if (!currentUserId.equals(profileUserId)) {
                BaseResponse response = new BaseResponse();
                response.setStatusCode(403);
                response.setMessage("Access denied: You can only update your own tutor profile");
                response.setData(null);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        }

        TutorDTO tutor = tutorService.updateTutor(id, tutorRequest);
        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setMessage("Tutor updated successfully");
        response.setData(tutor);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteTutor(@PathVariable Long id) {
        // Check ownership: only the tutor owner can delete their profile
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Long currentUserId = getCurrentUserId(authentication);
            Long profileUserId = tutorService.getUserIdFromTutorProfile(id);

            if (!currentUserId.equals(profileUserId)) {
                BaseResponse response = new BaseResponse();
                response.setStatusCode(403);
                response.setMessage("Access denied: You can only delete your own tutor profile");
                response.setData(null);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        }

        tutorService.deleteTutor(id);
        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setMessage("Tutor deleted successfully");
        response.setData(null);
        return ResponseEntity.ok(response);
    }

    private Long getCurrentUserId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }
}
