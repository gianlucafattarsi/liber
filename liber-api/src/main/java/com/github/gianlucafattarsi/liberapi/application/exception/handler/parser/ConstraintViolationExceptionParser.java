package com.github.gianlucafattarsi.liberapi.application.exception.handler.parser;

import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ConstraintError;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Component
public class ConstraintViolationExceptionParser implements ExceptionParser<ConstraintViolationException> {

    private final LocaleUtilsMessage localeUtilsMessage;

    @Override
    public ConstraintError parseInternal(final ConstraintViolationException ex, Locale locale) {

        String field = localeUtilsMessage.getMessage(ex.getConstraintName(), null, locale);
        String message = switch (ex.getKind()) {
            case UNIQUE -> localeUtilsMessage.getMessage("ConstraintViolationException.unique", null, locale);
            case OTHER -> localeUtilsMessage.getMessage("ConstraintViolationException.other", null, locale);
        };

        return ConstraintError.builder()
                              .fieldName(field)
                              .constraintsNotRespected(List.of(message))
                              .build();
    }

    @Override
    public boolean canParse(final Throwable ex) {
        return ex instanceof ConstraintViolationException;
    }
}
