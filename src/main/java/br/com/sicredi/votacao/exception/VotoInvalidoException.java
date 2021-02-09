package br.com.sicredi.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VotoInvalidoException extends RuntimeException {

    public VotoInvalidoException(final String message) {
        super(message);
    }
}