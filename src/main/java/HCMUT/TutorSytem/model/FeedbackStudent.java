package HCMUT.TutorSytem.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "feedback_student")
public class FeedbackStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(name = "rating", nullable = false, precision = 2, scale = 1)
    @DecimalMin("0.5")
    @DecimalMax("5.0")
    private BigDecimal rating;

    @Column(name = "content_quality", columnDefinition = "tinyint UNSIGNED")
    private Short contentQuality;

    @Column(name = "teaching_effectiveness", columnDefinition = "tinyint UNSIGNED")
    private Short teachingEffectiveness;

    @Column(name = "communication", columnDefinition = "tinyint UNSIGNED")
    private Short communication;

    @Lob
    @Column(name = "comment")
    private String comment;

    @Lob
    @Column(name = "suggestion")
    private String suggestion;

    @Column(name = "would_recommend")
    private Boolean wouldRecommend;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

}