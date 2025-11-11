package HCMUT.TutorSytem.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class TutorRequest {
    private String name;
    private String title;
    private Long majorId; // Major determines department
    private String description;
    private List<Long> subjects; // List of subject IDs from frontend
    private Double rating;
    private Integer reviewCount;
    private Integer studentCount;
    private Integer experienceYears;
    private Boolean isAvailable;
}

