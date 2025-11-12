package HCMUT.TutorSytem.dto;

import lombok.Data;

@Data
public class MajorDTO {
    private Long id;
    private String name;
    private String majorCode;
    private String programCode;
    private String note;
    private Long departmentId;
    private String departmentName;
}

