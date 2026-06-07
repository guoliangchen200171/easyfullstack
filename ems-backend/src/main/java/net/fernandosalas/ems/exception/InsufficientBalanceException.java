package net.fernandosalas.ems.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientBalanceException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(InsufficientBalanceException.class);

    public InsufficientBalanceException(Long studentId, BigDecimal required, BigDecimal deposit) {
        super("存款余额不足");
        log.warn("购买失败 | 存款余额不足 | studentId={} | required={} | deposit={}",
                studentId, required, deposit);
    }
}
