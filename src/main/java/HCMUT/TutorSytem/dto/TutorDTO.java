package HCMUT.TutorSytem.dto;

import lombok.Data;

import java.util.List;

@Data
public class TutorDTO {
    private Long id;
    private String name;
    private String title;
    private Long majorId;
    private String majorName;
    private String department; // From major.department.name (faculty = department)
    private String description;
    private List<String> specializations; // List of subject names
    private Double rating;
    private Integer reviewCount;
    private Integer studentCount;
    private Integer experienceYears;
    private Boolean isAvailable;
}

