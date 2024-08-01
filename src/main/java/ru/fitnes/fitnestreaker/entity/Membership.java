package ru.fitnes.fitnestreaker.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "memberships")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

//    @Column(name = "active")
//    private boolean active;

    @Column(name = "membership_status")
    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus;

    @Column(name = "freezing_days")
    private Long freezingDays;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
