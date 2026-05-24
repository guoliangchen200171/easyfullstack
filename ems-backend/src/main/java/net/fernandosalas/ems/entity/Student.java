package net.fernandosalas.ems.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "return_count", nullable = false)
    private int returnCount = 0;

    @Column(name = "deposit", nullable = false, precision = 10, scale = 2)
    private BigDecimal deposit = BigDecimal.ZERO;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;

    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY)
    @JsonIgnore
    private Pet pet;
}
