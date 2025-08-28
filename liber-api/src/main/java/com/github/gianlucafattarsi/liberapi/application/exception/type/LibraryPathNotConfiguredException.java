package com.github.gianlucafattarsi.liberapi.application.exception.type;

import java.io.Serial;

/**
 * Exception thrown when a library path is not configured in the settings.
 */
public class LibraryPathNotConfiguredException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6523428152344961287L;

    public LibraryPathNotConfiguredException() {
        super();
    }
}