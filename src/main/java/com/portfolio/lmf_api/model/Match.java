package com.portfolio.lmf_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "matches")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(name = "division_name", nullable = false)
    private String divisionName;

    @Column(name = "home_team_name", nullable = false)
    private String homeTeamName;

    @Column(name = "visit_team_name", nullable = false)
    private String visitTeamName;

    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @OneToMany(mappedBy = "match")
    private List<Payment> payments;
}
