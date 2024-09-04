package ru.fitnes.fitnestreaker.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "speciality")
    private String specialty;

    @Column(name = "description")
    private String description;

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.REMOVE,  fetch = FetchType.LAZY)
    private List<CoachingTime> coachingTimes = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<User> users = new HashSet<>();

}
