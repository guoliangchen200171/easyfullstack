package net.fernandosalas.membership.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "membership_level")
public class MembershipLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level_code", nullable = false, unique = true)
    private String levelCode;

    @Column(name = "level_name", nullable = false)
    private String levelName;

    @Column(name = "min_points", nullable = false)
    private long minPoints;

    @Column(name = "description")
    private String description;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}
