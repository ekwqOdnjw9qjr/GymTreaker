package ru.fitnes.fitnestreaker.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
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
    private Long id;

    @Column(name = "start_of_training")
    private LocalDateTime startOfTraining;

    @Column(name = "end_of_training")
    private LocalDateTime endOfTraining;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "coaching_times_trainers",
            joinColumns = @JoinColumn(name = "coaching_time_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id"))
    private Set<Trainer> trainers = new HashSet<>();

    @OneToMany(mappedBy = "coachingTime", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, orphanRemoval = true)
    private Set<Session> sessions = new HashSet<>();

}
