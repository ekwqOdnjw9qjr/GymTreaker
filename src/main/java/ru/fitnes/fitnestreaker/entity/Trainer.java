package ru.fitnes.fitnestreaker.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "name")
    private String name;

    @Column(name = "speciality")
    private String specialty;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.REFRESH,  fetch = FetchType.LAZY)
    private Set<Session> sessions;
}
