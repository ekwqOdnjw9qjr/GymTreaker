package ru.fitnes.fitnestreaker.entity;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;



import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "coaching_times")
public class CoachingTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "start_of_training")
    private LocalTime  startOfTraining;

    @Column(name = "end_of_training")
    private LocalTime endOfTraining;

    @Column(name = "day_of_the_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    @EqualsAndHashCode.Exclude
    private Trainer trainer;

    @OneToMany(mappedBy = "coachingTime", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<Session> sessions = new HashSet<>();
}
