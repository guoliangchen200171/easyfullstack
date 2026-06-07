package net.fernandosalas.ems.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 给学生购买商品、部门自助注册请求生成 16 位追踪 ID 放进 MDC，配合 logback-spring.xml 里的 traceid： 前缀输出，
 * 让同一次链路的所有日志（切面 + 业务日志）在控制台带同一个 traceId，便于串联排查。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "traceId";

    private static final Pattern STUDENT_PURCHASE_PATH =
            Pattern.compile(".*/api/students/me/products/\\d+/purchase$");

    private static final Pattern DEPARTMENT_REGISTER_PATH =
            Pattern.compile(".*/api/auth/register/department$");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !isTracedRequest(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        MDC.put(TRACE_ID, newTraceId());
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID);
        }
    }

    private static boolean isTracedRequest(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String uri = request.getRequestURI();
        if (uri == null) {
            return false;
        }
        return STUDENT_PURCHASE_PATH.matcher(uri).matches()
                || DEPARTMENT_REGISTER_PATH.matcher(uri).matches();
    }

    private static String newTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
