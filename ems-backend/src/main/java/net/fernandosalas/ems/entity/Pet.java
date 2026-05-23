package net.fernandosalas.ems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.fernandosalas.ems.enums.PetCategory;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private PetCategory category;

    @Column(name = "adopted", nullable = false)
    private boolean adopted = false;

    @Column(name = "adoption_count", nullable = false)
    private int adoptionCount = 0;

    @Column(name = "return_count", nullable = false)
    private int returnCount = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", unique = true)
    @JsonIgnore
    private Student student;

    @Transient
    private Long studentId;

    public Long getStudentId() {
        if (student != null) {
            return student.getId();
        }
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
