package com.github.gianlucafattarsi.liberapi.application.exception.type;

import java.io.Serial;

/**
 * Exception thrown when a token is expired.
 */
public class ExpiredTokenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6523428152344961287L;

    public ExpiredTokenException(String s) {
        super(s);
    }
}