package com.github.gianlucafattarsi.liberapi.application.exception.handler;

import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorData;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorInternal;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import com.github.gianlucafattarsi.liberapi.application.exception.type.ExpiredTokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExpiredTokenExceptionHandler {

    private final LocaleUtilsMessage localeUtilsMessage;

    public ExpiredTokenExceptionHandler(final LocaleUtilsMessage localeUtilsMessage) {
        this.localeUtilsMessage = localeUtilsMessage;
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<?> handleExpiredTokenException(ExpiredTokenException ex,
                                                         HttpServletRequest request) {

        final ErrorInternal internal = ErrorInternal.builder()
                                                    .exception(ex.getClass()
                                                                 .getName())
                                                    .stack(ex.getMessage())
                                                    .build();
        final ErrorData errorData = ErrorData.builder()
                                             .internal(internal)
                                             .userMessage(localeUtilsMessage.getMessage(
                                                     "ExpiredTokenException.detail",
                                                     null,
                                                     request))
                                             .userTitle(localeUtilsMessage.getMessage(
                                                     "ExpiredTokenException.title",
                                                     null,
                                                     request))
                                             .build();

        final ErrorResponse errorResponse = new ErrorResponse().error(errorData);

        return new ResponseEntity<>(errorResponse, null, HttpStatus.ACCEPTED);
    }
}
