package com.portfolio.lmf_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "courts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "court_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(name = "owner_team_name", nullable = false, unique = true)
    private String ownerTeamName;

    @OneToMany(mappedBy = "court")
    private List<Match> matches;
}
