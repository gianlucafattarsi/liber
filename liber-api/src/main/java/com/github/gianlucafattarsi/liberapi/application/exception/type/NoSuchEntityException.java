package com.github.gianlucafattarsi.liberapi.application.exception.type;

import java.io.Serial;

public class NoSuchEntityException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8043003509060151098L;

    public NoSuchEntityException() {
        super();
    }

    public NoSuchEntityException(String s) {
        super(s);
    }

    public NoSuchEntityException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NoSuchEntityException(Throwable throwable) {
        super(throwable);
    }
}