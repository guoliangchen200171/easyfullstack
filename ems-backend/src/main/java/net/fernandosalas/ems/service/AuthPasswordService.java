package net.fernandosalas.ems.service;

import net.fernandosalas.ems.enums.Role;

public interface AuthPasswordService {

    String UNSUPPORTED_ROLE_MESSAGE = "暂不支持此类型的密码修改";

    Role verifyStudentForPasswordChange(String username, String currentPassword);

    void changeStudentPasswordPublic(String username, String currentPassword, String newPassword);
}
