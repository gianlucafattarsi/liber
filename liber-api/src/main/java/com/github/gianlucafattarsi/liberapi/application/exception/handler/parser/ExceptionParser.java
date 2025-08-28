package com.github.gianlucafattarsi.liberapi.application.exception.handler.parser;

import com.github.gianlucafattarsi.liberapi.application.exception.model.ConstraintError;

import java.util.Locale;

public interface ExceptionParser<T extends Throwable> {

    /**
     * Parses the given exception and returns a ConstraintError.
     *
     * @param ex     the exception to parse
     * @param locale the locale to use for parsing
     * @return a ConstraintError representing the parsed exception
     */
    default ConstraintError parse(Throwable ex, Locale locale) {
        //noinspection unchecked
        return parseInternal((T) ex, locale);
    }

    ConstraintError parseInternal(T ex, Locale locale);

    /**
     * Determines if the parser can handle the given exception type.
     *
     * @param ex the exception to check
     * @return true if the parser can handle the exception, false otherwise
     */
    boolean canParse(Throwable ex);
}
