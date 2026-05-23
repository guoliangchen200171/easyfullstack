package net.fernandosalas.ems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PetNotAdoptedException extends RuntimeException {
    public PetNotAdoptedException(String message) {
        super(message);
    }
}
