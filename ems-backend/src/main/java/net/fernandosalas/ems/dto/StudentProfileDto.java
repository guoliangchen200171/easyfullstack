package net.fernandosalas.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long departmentId;
    private String departmentName;
    private Long petId;
    private String petName;
    private int returnCount;
}
