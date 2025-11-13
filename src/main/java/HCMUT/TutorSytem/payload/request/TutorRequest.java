package HCMUT.TutorSytem.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class TutorRequest {
    private String title;
    private Long majorId; // Major determines department
    private String description;
    private List<Long> subjects; // List of subject IDs from frontend
    private Integer experienceYears;
    // rating: auto calculated from reviews
    // isAvailable: auto set to true
    // name, phone, etc.: from User (Datacore)
}

