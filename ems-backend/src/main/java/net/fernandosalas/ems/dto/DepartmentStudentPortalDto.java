package net.fernandosalas.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentStudentPortalDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String petName;
    private int returnCount;
    private int petAdoptionCount;
    private int petReturnCount;
}
