package com.github.gianlucafattarsi.liberapi.application.exception.handler;

import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorData;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorInternal;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@RequiredArgsConstructor
@ControllerAdvice
public class IllegalArgumentExceptionHandler {

    private final LocaleUtilsMessage localeUtilsMessage;

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handle(final IllegalArgumentException ex,
                                         final WebRequest request) {
        final ErrorData errorData = ErrorData.builder()
                                             .userTitle(localeUtilsMessage.getMessage(
                                                     "ValidationFailed.title",
                                                     null,
                                                     request))
                                             .userMessage(localeUtilsMessage.getMessage(
                                                     "ValidationFailed.detail",
                                                     null,
                                                     request))
                                             .internal(ErrorInternal.builder()
                                                                    .exception(ex.getMessage())
                                                                    .build())
                                             .build();

        final ErrorResponse errorResponse = new ErrorResponse().error(errorData);

        return new ResponseEntity<>(errorResponse, null, HttpStatus.BAD_REQUEST);
    }
}
