package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.enums.Role;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.security.UserPrincipal;
import net.fernandosalas.ems.service.AuthPasswordService;
import net.fernandosalas.ems.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthPasswordServiceImplementation implements AuthPasswordService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Override
    public Role verifyStudentForPasswordChange(String username, String currentPassword) {
        UserPrincipal principal = authenticateWithoutSession(username, currentPassword);
        if (principal.getRole() != Role.STUDENT) {
            throw new InvalidSearchParameterException(UNSUPPORTED_ROLE_MESSAGE);
        }
        return Role.STUDENT;
    }

    @Override
    public void changeStudentPasswordPublic(String username, String currentPassword, String newPassword) {
        UserPrincipal principal = authenticateWithoutSession(username, currentPassword);
        if (principal.getRole() != Role.STUDENT) {
            throw new InvalidSearchParameterException(UNSUPPORTED_ROLE_MESSAGE);
        }
        userService.changePassword(principal.getId(), currentPassword, newPassword);
    }

    private UserPrincipal authenticateWithoutSession(String username, String currentPassword) {
        if (username == null || username.isBlank()
                || currentPassword == null || currentPassword.isBlank()) {
            throw new InvalidSearchParameterException("用户名和原密码不能为空");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username.trim(), currentPassword));
            if (!(authentication.getPrincipal() instanceof UserPrincipal principal)) {
                throw new InvalidSearchParameterException("原密码不正确");
            }
            return principal;
        } catch (BadCredentialsException ex) {
            throw new InvalidSearchParameterException("原密码不正确");
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
