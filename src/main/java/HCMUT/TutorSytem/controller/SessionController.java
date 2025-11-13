package HCMUT.TutorSytem.controller;

import HCMUT.TutorSytem.dto.PageDTO;
import HCMUT.TutorSytem.dto.SessionDTO;
import HCMUT.TutorSytem.payload.request.SessionRequest;
import HCMUT.TutorSytem.payload.response.BaseResponse;
import HCMUT.TutorSytem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllSessions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageDTO<SessionDTO> sessionPage = sessionService.getAllSessions(page, size);
        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setMessage("Sessions retrieved successfully");
        response.setData(sessionPage);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createSession(@RequestBody SessionRequest sessionRequest) {
        SessionDTO session = sessionService.createSession(sessionRequest);
        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setMessage("Session created successfully");
        response.setData(session);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateSession(@PathVariable Long id, @RequestBody SessionRequest sessionRequest) {
        // Check ownership: only the tutor who created the session can update it
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Long currentUserId = getCurrentUserId(authentication);
            Long sessionTutorId = sessionService.getTutorIdFromSession(id);

            if (!currentUserId.equals(sessionTutorId)) {
                BaseResponse response = new BaseResponse();
                response.setStatusCode(403);
                response.setMessage("Access denied: You can only update your own sessions");
                response.setData(null);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        }

        SessionDTO session = sessionService.updateSession(id, sessionRequest);
        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setMessage("Session updated successfully");
        response.setData(session);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteSession(@PathVariable Long id) {
        // Check ownership: only the tutor who created the session can delete it
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Long currentUserId = getCurrentUserId(authentication);
            Long sessionTutorId = sessionService.getTutorIdFromSession(id);

            if (!currentUserId.equals(sessionTutorId)) {
                BaseResponse response = new BaseResponse();
                response.setStatusCode(403);
                response.setMessage("Access denied: You can only delete your own sessions");
                response.setData(null);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        }

        sessionService.deleteSession(id);
        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setMessage("Session deleted successfully");
        response.setData(null);
        return ResponseEntity.ok(response);
    }

    private Long getCurrentUserId(Authentication authentication) {
        // TODO: Extract user ID from authentication principal
        // This depends on how you implement authentication
        // Example: return ((UserDetails) authentication.getPrincipal()).getId();
        return 1L; // Placeholder - replace with actual implementation
    }
}

