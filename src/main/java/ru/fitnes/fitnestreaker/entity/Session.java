package ru.fitnes.fitnestreaker.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.fitnes.fitnestreaker.entity.enums.SessionStatus;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_of_training")
    private LocalDate dateOfTraining;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "coaching_time_id")
    private CoachingTime coachingTime;

    @Column(name = "user_comment")
    private String userComment;

    @Column(name = "trainer_comment")
    private String trainerComment;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SessionStatus status;
}
