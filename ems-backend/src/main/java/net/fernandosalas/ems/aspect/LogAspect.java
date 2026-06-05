package net.fernandosalas.ems.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 统一日志切面：自动给 controller 与 service 实现层的方法打印
 * 「入参 / 返回值 / 耗时 / 异常」，无需在业务代码里手写日志。
 *
 * <p>只打印到控制台（由 logback-spring.xml 控制），异常仅记录后原样抛出，
 * 仍交给 GlobalExceptionHandler 统一处理。
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    /** Controller 层所有方法 */
    @Pointcut("execution(* net.fernandosalas.ems.controller..*(..))")
    public void controllerLayer() {
    }

    /** Service 实现层所有方法 */
    @Pointcut("execution(* net.fernandosalas.ems.service.implementation..*(..))")
    public void serviceLayer() {
    }

    @Around("controllerLayer() || serviceLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().getDeclaringType().getSimpleName()
                + "." + joinPoint.getSignature().getName();
        long start = System.currentTimeMillis();

        log.debug("进入 {} 入参={}", method, safeArgs(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            long cost = System.currentTimeMillis() - start;
            log.debug("返回 {} 结果={} 耗时={}ms", method, summarize(result), cost);
            return result;
        } catch (Throwable ex) {
            long cost = System.currentTimeMillis() - start;
            log.error("异常 {} 耗时={}ms 错误={}: {}", method, cost,
                    ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }
    }

    /** 对入参做脱敏 + 摘要，避免把明文密码等敏感字段打到控制台 */
    private String safeArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        return Arrays.stream(args)
                .map(this::maskIfSensitive)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private String maskIfSensitive(Object arg) {
        if (arg == null) {
            return "null";
        }
        String typeName = arg.getClass().getSimpleName();
        // 登录/注册/改密等含密码的对象只打印类型，不打印内容
        if (typeName.contains("Login")
                || typeName.contains("Password")
                || typeName.contains("Register")
                || typeName.contains("Credential")) {
            return typeName + "(***)";
        }
        return summarize(arg);
    }

    /** 集合/分页等大对象只打印类型和数量，不整体 dump */
    private String summarize(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Collection<?> collection) {
            return value.getClass().getSimpleName() + "(size=" + collection.size() + ")";
        }
        String text = String.valueOf(value);
        if (text.length() > 200) {
            return value.getClass().getSimpleName() + "(len=" + text.length() + ")";
        }
        return text;
    }
}
