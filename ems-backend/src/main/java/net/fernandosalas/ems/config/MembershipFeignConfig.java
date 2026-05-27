package net.fernandosalas.ems.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class MembershipFeignConfig {

    public static final String INTERNAL_API_KEY_HEADER = "X-Internal-Api-Key";

    @Bean
    public RequestInterceptor membershipInternalApiKeyInterceptor(
            @Value("${app.internal.api-key}") String apiKey) {
        return template -> template.header(INTERNAL_API_KEY_HEADER, apiKey);
    }
}
