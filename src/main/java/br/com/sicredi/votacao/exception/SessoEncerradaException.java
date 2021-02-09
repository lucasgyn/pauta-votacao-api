package br.com.sicredi.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class SessoEncerradaException extends RuntimeException {

    public SessoEncerradaException(final String message) {
        super(message);
    }
}
