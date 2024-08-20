package ru.fitnes.fitnestreaker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.fitnes.fitnestreaker.entity.enums.Role;

import java.util.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "users")
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    @Size(min = 2, max= 55, message = "First name should be from 2 to 55 characters")
    private String firstName;

    @Column(name = "last_name")
    @Size(min = 2, max= 55, message = "Last name should be from 2 to 55 characters")
    private String lastName;

    @Column(name = "email")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email format")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE,orphanRemoval = true)
    private Set<Membership> memberships = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Session> sessions = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonBackReference
    private Trainer trainer;


}
