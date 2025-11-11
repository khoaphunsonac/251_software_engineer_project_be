package HCMUT.TutorSytem.payload.request;

import lombok.Data;

import java.time.Instant;

@Data
public class SessionRequest {
    private Long tutorId;
    private Long studentId;
    private Long subjectId;
    private Instant startTime;
    private Instant endTime;
    private String format;
    private String location;
    private String status;
}

