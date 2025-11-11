package HCMUT.TutorSytem.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class SessionDTO {
    private Long id;
    private String tutorName;
    private String studentName;
    private String subjectName;
    private Instant startTime;
    private Instant endTime;
    private String format;
    private String location;
    private String status;
    private Instant createdDate;
    private Instant updatedDate;
}

