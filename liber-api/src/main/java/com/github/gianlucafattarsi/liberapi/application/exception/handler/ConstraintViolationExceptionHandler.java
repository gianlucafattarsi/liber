package com.github.gianlucafattarsi.liberapi.application.exception.handler;

import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ConstraintError;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorData;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorInternal;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@RequiredArgsConstructor
@ControllerAdvice
public class ConstraintViolationExceptionHandler {

    private final LocaleUtilsMessage localeUtilsMessage;

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handle(final ConstraintViolationException ex,
                                         final WebRequest request) {

        List<ConstraintError> constraintViolated = ex.getConstraintViolations()
                                                     .stream()
                                                     .map(violation -> buildConstraintError(
                                                             violation,
                                                             request))
                                                     .toList();

        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        final String stack = sw.toString()
                               .substring(0, 300);

        final ErrorData errorData = ErrorData.builder()
                                             .userTitle(localeUtilsMessage.getMessage(
                                                     "ValidationFailed.title",
                                                     null,
                                                     request))
                                             .userMessage(localeUtilsMessage.getMessage(
                                                     "ValidationFailed.detail",
                                                     null,
                                                     request))
                                             .constraintErrors(constraintViolated)
                                             .internal(ErrorInternal.builder()
                                                                    .exception(ex.getClass()
                                                                                 .getName())
                                                                    .stack(stack)
                                                                    .build())
                                             .build();

        final ErrorResponse errorResponse = new ErrorResponse().error(errorData);

        return new ResponseEntity<>(errorResponse, null, HttpStatus.BAD_REQUEST);
    }

    private ConstraintError buildConstraintError(ConstraintViolation<?> violation,
                                                 final WebRequest request) {
        String messageTemplate = violation.getMessageTemplate();
        if (messageTemplate.startsWith("{")) {
            messageTemplate = messageTemplate.substring(1);
        }
        if (messageTemplate.endsWith("}")) {
            messageTemplate = messageTemplate.substring(0, messageTemplate.length() - 1);
        }

        return ConstraintError.builder()
                              .fieldName(violation.getPropertyPath()
                                                  .toString())
                              .constraintsNotRespected(List.of(localeUtilsMessage.getMessage(
                                      messageTemplate,
                                      null,
                                      request)))
                              .build();
    }

}
