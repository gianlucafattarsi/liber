package com.github.gianlucafattarsi.liberapi.application.exception.handler;

import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorData;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import com.github.gianlucafattarsi.liberapi.application.exception.type.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class UserAlreadyExistsExceptionHandler {

    LocaleUtilsMessage localeUtilsMessage;

    public UserAlreadyExistsExceptionHandler(final LocaleUtilsMessage localeUtilsMessage) {
        this.localeUtilsMessage = localeUtilsMessage;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public final ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
            final UserAlreadyExistsException ex, final WebRequest request) {

        final ErrorData errorData = ErrorData.builder()
                                             .userTitle(localeUtilsMessage.getMessage(
                                                     "UserAlreadyExistsException.title",
                                                     null,
                                                     request))
                                             .userMessage(localeUtilsMessage.getMessage(
                                                     "UserAlreadyExistsException.detail",
                                                     new String[]{ex.getUserName(), ex.getEmail()},
                                                     request))
                                             .build();

        final ErrorResponse errorResponse = new ErrorResponse().error(errorData);

        return new ResponseEntity<>(errorResponse, null, HttpStatus.FORBIDDEN);
    }
}
