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
 * 学生购买商品、部门自助注册链路日志切面：自动打印「入参 / 返回值 / 耗时 / 异常」。
 *
 * <p>只打印到控制台（由 logback-spring.xml 控制），异常仅记录后原样抛出，
 * 仍交给 GlobalExceptionHandler 统一处理。
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* net.fernandosalas.ems.controller.StudentPortalController.purchaseProduct(..))")
    public void studentPurchaseController() {
    }

    @Pointcut("execution(* net.fernandosalas.ems.service.implementation.StudentPortalServiceImplementation.purchaseProductForCurrentStudent(..))")
    public void studentPurchaseService() {
    }

    @Pointcut("execution(* net.fernandosalas.ems.service.implementation.ProductOrderServiceImplementation.recordOrder(..))")
    public void recordOrder() {
    }

    @Pointcut("execution(* net.fernandosalas.ems.controller.AuthController.registerDepartment(..))")
    public void departmentRegisterController() {
    }

    @Pointcut("execution(* net.fernandosalas.ems.service.implementation.AuthRegistrationServiceImplementation.registerDepartment(..))")
    public void departmentRegisterService() {
    }

    @Around("studentPurchaseController() || studentPurchaseService() || recordOrder()"
            + " || departmentRegisterController() || departmentRegisterService()")
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
