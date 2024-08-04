package ru.fitnes.fitnestreaker.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "membership_duration")
    private Long membershipDuration;

//    @Column(name = "membership_status")
//    @Enumerated(EnumType.STRING)
//    private MembershipStatus membershipStatus;
//
//    @Column(name = "membership_end_date")
//    @Enumerated(EnumType.STRING)
//    private MembershipType membershipEndDate;


    @Column(name = "freezing_days")
    private Long freezingDays;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
