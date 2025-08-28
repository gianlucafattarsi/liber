package com.github.gianlucafattarsi.liberapi.application.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ConstraintError;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;

import java.beans.PropertyEditor;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private LocaleUtilsMessage localeUtilsMessage;

    @Autowired
    private GlobalExceptionHandler handler;

    @Test
    @DisplayName("Test handleHttpMessageNotReadable")
    public void testHandleHttpMessageNotReadable() throws Exception {

        final ResponseEntity<Object> handled = handler.handleHttpMessageNotReadable(new HttpMessageNotReadableException(
                "Message not readable",
                new IllegalStateException(),
                new MockClientHttpResponse()), null, null, new ServletWebRequest(new MockHttpServletRequest()));

        assertThat(handled).isNotNull();
        assertThat(handled.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorResponse errorResponse = (ErrorResponse) handled.getBody();

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getError()
                                .getInternal()
                                .getException()).isEqualTo(HttpMessageNotReadableException.class.getName());
        assertThat(errorResponse.getError()
                                .getUserTitle()).isEqualTo(localeUtilsMessage.getMessage(
                "GenericFail.title",
                null,
                new MockHttpServletRequest()));
        assertThat(errorResponse.getError()
                                .getUserMessage()).isEqualTo(localeUtilsMessage.getMessage(
                "GenericFail.detail",
                null,
                new MockHttpServletRequest()));
    }

    @Test
    @DisplayName("Test handleHttpMessageNotReadable - InvalidFormatException")
    public void testHandleHttpMessageNotReadable_invalidFormatException() throws Exception {
        final ResponseEntity<Object> handled = handler.handleHttpMessageNotReadable(new HttpMessageNotReadableException(
                "Message not readable",
                new InvalidFormatException(null, "Invalid format", null, null),
                new MockClientHttpResponse()), null, null, new ServletWebRequest(new MockHttpServletRequest()));

        assertThat(handled).isNotNull();
        assertThat(handled.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorResponse errorResponse = (ErrorResponse) handled.getBody();

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getError()
                                .getInternal()
                                .getException()).isEqualTo(HttpMessageNotReadableException.class.getName());
        assertThat(errorResponse.getError()
                                .getUserTitle()).isEqualTo(localeUtilsMessage.getMessage(
                "InvalidFormatException.title",
                null,
                new MockHttpServletRequest()));
        assertThat(errorResponse.getError()
                                .getUserMessage()).isEqualTo(localeUtilsMessage.getMessage(
                "InvalidFormatException.detail",
                null,
                new MockHttpServletRequest()));
    }

    @Test
    @DisplayName("Test handleMethodArgumentNotValid")
    public void testHandleMethodArgumentNotValid() throws Exception {

        final BindingResult bindingResult = new BindingResult() {

            @Override
            public String getObjectName() {
                return "";
            }

            @Override
            public void reject(final String errorCode, final Object[] errorArgs, final String defaultMessage) {

            }

            @Override
            public void rejectValue(final String field, final String errorCode, final Object[] errorArgs,
                                    final String defaultMessage) {

            }

            @Override
            public List<ObjectError> getGlobalErrors() {
                return List.of();
            }

            @Override
            public List<FieldError> getFieldErrors() {
                FieldError fieldError = new FieldError("objectName", "field", "Default message");
                FieldError field2Error = new FieldError("objectName", "field2", "Default message 2");
                FieldError field3Error = new FieldError("objectName", "field", "Default message 3");
                FieldError field4Error = new FieldError("objectName", "field.subfield", "Default message 4");
                return List.of(fieldError, field2Error, field3Error, field4Error);
            }

            @Override
            public Object getFieldValue(final String field) {
                return null;
            }

            @Override
            public Object getTarget() {
                return null;
            }

            @Override
            public Map<String, Object> getModel() {
                return Map.of();
            }

            @Override
            public Object getRawFieldValue(final String field) {
                return null;
            }

            @Override
            public PropertyEditor findEditor(final String field, final Class<?> valueType) {
                return null;
            }

            @Override
            public PropertyEditorRegistry getPropertyEditorRegistry() {
                return null;
            }

            @Override
            public String[] resolveMessageCodes(final String errorCode) {
                return new String[0];
            }

            @Override
            public String[] resolveMessageCodes(final String errorCode, final String field) {
                return new String[0];
            }

            @Override
            public void addError(final ObjectError error) {

            }
        };

        final ResponseEntity<Object> handled = handler.handleMethodArgumentNotValid(new MethodArgumentNotValidException(
                        null,
                        bindingResult),
                null, null, new ServletWebRequest(new MockHttpServletRequest()));

        assertThat(handled).isNotNull();
        assertThat(handled.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorResponse errorResponse = (ErrorResponse) handled.getBody();

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getError()
                                .getUserTitle()).isEqualTo(localeUtilsMessage.getMessage(
                "ValidationFailed.title",
                null,
                new MockHttpServletRequest()));
        assertThat(errorResponse.getError()
                                .getUserMessage()).isEqualTo(localeUtilsMessage.getMessage(
                "ValidationFailed.detail",
                null,
                new MockHttpServletRequest()));
        assertThat(errorResponse.getError()
                                .getConstraintErrors()).hasSize(3);
        assertThat(errorResponse.getError()
                                .getConstraintErrors()
                                .stream()
                                .map(ConstraintError::getFieldName)).containsExactlyInAnyOrder("field",
                "field2",
                "subfield");
        assertThat(errorResponse.getError()
                                .getConstraintErrors()
                                .stream()
                                .flatMap(c -> c.getConstraintsNotRespected()
                                               .stream())
                                .toList()).containsExactlyInAnyOrder("Default message",
                "Default message 2",
                "Default message 3",
                "Default message 4");
    }
}
