package net.fernandosalas.ems.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.ChangePasswordRequest;
import net.fernandosalas.ems.dto.DepartmentRegisterRequest;
import net.fernandosalas.ems.dto.LoginRequest;
import net.fernandosalas.ems.dto.StudentRegisterRequest;
import net.fernandosalas.ems.security.SecurityUtils;
import net.fernandosalas.ems.security.UserPrincipal;
import net.fernandosalas.ems.service.AuthRegistrationService;
import net.fernandosalas.ems.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "认证", description = "登录、登出、注册与当前用户")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthRegistrationService authRegistrationService;
    private final UserService userService;
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    @Operation(summary = "用户登录", description = "Session 登录，成功后浏览器保存 JSESSIONID，再调试其它需认证接口")
    @PostMapping("/login")
    @SecurityRequirements
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);

            return ResponseEntity.ok(buildAuthResponse(authentication, "登录成功"));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "账号或密码错误"));
        }
    }

    @Operation(summary = "退出登录", description = "清除当前 Session")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      Authentication authentication) {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return ResponseEntity.ok(Map.of("message", "已退出登录"));
    }

    @Operation(summary = "获取当前用户", description = "返回是否已登录、用户名与角色")
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("authenticated", false));
        }
        return ResponseEntity.ok(buildAuthResponse(authentication, null));
    }

    @Operation(summary = "修改密码", description = "学生或院系登录后修改自己的密码，需提供原密码")
    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestBody ChangePasswordRequest request) {
        userService.changePassword(
                SecurityUtils.getCurrentUser().getId(),
                request.getCurrentPassword(),
                request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "密码修改成功"));
    }

    @Operation(summary = "注册部门账号", description = "公开注册，创建部门及部门登录用户")
    @PostMapping("/register/department")
    @SecurityRequirements
    public ResponseEntity<Map<String, String>> registerDepartment(
            @RequestBody DepartmentRegisterRequest request) {
        authRegistrationService.registerDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "部门注册成功"));
    }

    @Operation(summary = "注册学生账号", description = "公开注册，默认登录密码为 123")
    @PostMapping("/register/student")
    @SecurityRequirements
    public ResponseEntity<Map<String, String>> registerStudent(
            @RequestBody StudentRegisterRequest request) {
        authRegistrationService.registerStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "学生注册成功，默认密码为 123"));
    }

    private Map<String, Object> buildAuthResponse(Authentication authentication, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("authenticated", true);
        body.put("username", authentication.getName());
        if (message != null) {
            body.put("message", message);
        }
        if (authentication.getPrincipal() instanceof UserPrincipal principal) {
            body.put("role", principal.getRole().name());
        }
        return body;
    }
}
