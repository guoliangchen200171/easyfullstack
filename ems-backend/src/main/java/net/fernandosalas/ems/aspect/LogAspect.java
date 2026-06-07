package net.fernandosalas.ems.aspect;

import lombok.extern.slf4j.Slf4j;
import net.fernandosalas.ems.dto.ProductDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 管理员创建商品链路日志切面：成功时打印 log.info。
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* net.fernandosalas.ems.service.implementation.ProductServiceImplementation.createProduct(..))")
    public void adminCreateProduct() {
    }

    @Around("adminCreateProduct()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            if (result instanceof ProductDto dto) {
                log.info("商品创建成功 | productId={} | name={} | stock={} | price={}",
                        dto.getId(), dto.getName(), dto.getStock(), dto.getPrice());
            }
            return result;
        } catch (Throwable ex) {
            log.warn("商品创建失败 | error={}: {}",
                    ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }
    }
}
