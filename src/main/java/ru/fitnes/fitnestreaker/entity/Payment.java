package ru.fitnes.fitnestreaker.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;


    @Column(name = "status")
    private String status; // "completed", "pending", etc.

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
