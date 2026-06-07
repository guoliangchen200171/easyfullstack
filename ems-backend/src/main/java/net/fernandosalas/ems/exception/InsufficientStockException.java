package net.fernandosalas.ems.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(InsufficientStockException.class);

    public InsufficientStockException(Long productId, int quantity) {
        super("库存不足");
        log.warn("购买失败 | 库存不足 | productId={} | qty={}", productId, quantity);
    }
}
