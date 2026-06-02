package com.portfolio.lmf_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
}
