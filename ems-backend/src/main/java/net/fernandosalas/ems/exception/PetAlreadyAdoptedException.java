package net.fernandosalas.ems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PetAlreadyAdoptedException extends RuntimeException {
    public PetAlreadyAdoptedException(String message) {
        super(message);
    }
}
