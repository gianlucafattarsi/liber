package com.github.gianlucafattarsi.liberapi.application.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ConstraintError;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorData;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorInternal;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final LocaleUtilsMessage localeUtilsMessage;

    public GlobalExceptionHandler(final LocaleUtilsMessage localeUtilsMessage) {
        this.localeUtilsMessage = localeUtilsMessage;
    }

    @Nonnull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex, final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        // Create ValidationError instances
        final List<FieldError> fieldErrors = ex.getBindingResult()
                                               .getFieldErrors();
        List<ConstraintError> constraintsViolated;
        final Map<String, List<FieldError>> errorsMap = fieldErrors.stream()
                                                                   .collect(groupingBy(
                                                                           FieldError::getField));
        constraintsViolated = errorsMap.keySet()
                                       .stream()
                                       .map((k) -> {
                                           // In case of array property ( es: property[1].name) the substring after last
                                           // char '.' represent the field name
                                           final int index = StringUtils.lastIndexOf(k, ".");

                                           final String fieldName = (index != -1) ? StringUtils.substring(
                                                   k,
                                                   index + 1) : k;
                                           return ConstraintError.builder()
                                                                 .fieldName(fieldName)
                                                                 .constraintsNotRespected(
                                                                         errorsMap.get(k)
                                                                                  .stream()
                                                                                  .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                                                                  .collect(
                                                                                          Collectors.toList()))
                                                                 .build();
                                       })
                                       .collect(Collectors.toList());

        final ErrorData errorData = ErrorData.builder()
                                             .userTitle(localeUtilsMessage.getMessage(
                                                     "ValidationFailed.title",
                                                     null,
                                                     request))
                                             .userMessage(localeUtilsMessage.getMessage(
                                                     "ValidationFailed.detail",
                                                     null,
                                                     request))
                                             .constraintErrors(constraintsViolated)
                                             .build();

        final ErrorResponse errorResponse = new ErrorResponse().error(errorData);

        return new ResponseEntity<>(errorResponse, headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException ex, final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {

        String titleKey = "GenericFail.title";
        String detailKey = "GenericFail.detail";

        if (ex.getCause() instanceof InvalidFormatException) {
            titleKey = "InvalidFormatException.title";
            detailKey = "InvalidFormatException.detail";
        }

        final ErrorData errorData = ErrorData.builder()
                                             .userTitle(localeUtilsMessage.getMessage(
                                                     titleKey,
                                                     null,
                                                     request))
                                             .userMessage(localeUtilsMessage.getMessage(
                                                     detailKey,
                                                     null,
                                                     request))
                                             .internal(ErrorInternal.builder()
                                                                    .exception(ex.getClass()
                                                                                 .getName())
                                                                    .build())
                                             .build();

        final ErrorResponse errorResponse = new ErrorResponse().error(errorData);

        return new ResponseEntity<>(errorResponse, headers, HttpStatus.BAD_REQUEST);
    }
}