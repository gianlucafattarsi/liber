package com.github.gianlucafattarsi.liberapi.application.exception.handler;

import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleResolver;
import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.exception.handler.parser.ExceptionParser;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ConstraintError;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorData;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorInternal;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
public class DataIntegrityViolationExceptionHandler {

    private final LocaleUtilsMessage localeUtilsMessage;
    private final LocaleResolver localeResolver;
    @Autowired
    private List<ExceptionParser<? extends Throwable>> exceptionParsers;

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handle(final DataIntegrityViolationException ex,
                                         final WebRequest request) {


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
                                             .constraintErrors(List.of(parseException(ex, request)))
                                             .internal(ErrorInternal.builder()
                                                                    .exception(ex.getClass()
                                                                                 .getName())
                                                                    .stack(stack)
                                                                    .build())
                                             .build();

        final ErrorResponse errorResponse = new ErrorResponse().error(errorData);

        return new ResponseEntity<>(errorResponse, null, HttpStatus.BAD_REQUEST);
    }

    private ConstraintError parseException(DataIntegrityViolationException ex, WebRequest request) {

        for (ExceptionParser<? extends Throwable> parser : exceptionParsers) {
            if (parser.canParse(ex.getCause())) {
                return parser.parse(ex.getCause(), localeResolver.resolveLocale(request));
            }
        }

        // If no parser can handle the exception, we default to a generic ConstraintError
        return buildConstraintError(ex, request);
    }

    private ConstraintError buildConstraintError(DataIntegrityViolationException ex,
                                                 final WebRequest request) {

        ConstraintError.ConstraintErrorBuilder constraintErrorBuilder = ConstraintError.builder();

        String originalMessage = ex.getMostSpecificCause()
                                   .getMessage();

        String message;
        String field;

        // This solution is not perfect, but it extracts the main value causing the exception.
        if (originalMessage != null) {
            message = originalMessage.split("for")[0];
            field = localeUtilsMessage.getMessage(
                    StringUtils.substringBetween(originalMessage.split("for")[1], "'"),
                    null,
                    request);
            constraintErrorBuilder = constraintErrorBuilder.fieldName(field)
                                                           .constraintsNotRespected(List.of(message));
        }

        return constraintErrorBuilder.build();
    }

}
