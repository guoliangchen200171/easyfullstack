package net.fernandosalas.ems.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String COOKIE_AUTH = "cookieAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("学生宠物管理系统 API")
                        .version("1.0")
                        .description("""
                                REST API 文档。认证方式为 Session Cookie：\
                                先调用 POST /api/auth/login，浏览器会保存 JSESSIONID，\
                                再调试需 ADMIN / STUDENT / DEPARTMENT 角色的接口。"""
                        ))
                .addSecurityItem(new SecurityRequirement().addList(COOKIE_AUTH))
                .components(new Components()
                        .addSecuritySchemes(COOKIE_AUTH, new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("JSESSIONID")
                                .description("登录成功后由服务端设置的 Session Cookie")));
    }
}
