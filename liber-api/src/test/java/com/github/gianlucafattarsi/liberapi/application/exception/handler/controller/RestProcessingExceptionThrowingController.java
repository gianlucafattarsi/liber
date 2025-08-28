package com.github.gianlucafattarsi.liberapi.application.exception.handler.controller;

import com.github.gianlucafattarsi.liberapi.application.exception.type.ExpiredTokenException;
import com.github.gianlucafattarsi.liberapi.application.exception.type.NoSuchEntityException;
import com.github.gianlucafattarsi.liberapi.application.exception.type.UserAlreadyExistsException;
import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests/exception")
public class RestProcessingExceptionThrowingController {

    @GetMapping(value = "/user-already-exists")
    public @ResponseBody String userAlreadyExists() {
        throw new UserAlreadyExistsException("test", "test@email.com");
    }

    @GetMapping(value = "/no-such-entity")
    public @ResponseBody String noSuchEntity() {
        throw new NoSuchEntityException();
    }

    @GetMapping(value = "/illegal-argument")
    public @ResponseBody String illegalArgument() {
        throw new IllegalArgumentException("Illegal argument exception message");
    }

    @GetMapping(value = "/illegal-state")
    public @ResponseBody String illegalState() {
        throw new IllegalStateException("Illegal state exception message");
    }

    @GetMapping(value = "/expired-token")
    public @ResponseBody String expiredToken() {
        throw new ExpiredTokenException("Token expired");
    }

    @GetMapping(value = "/message-not-readable-invalid-format")
    public @ResponseBody String messageNotReadableInvalidFormat() {
        throw new HttpMessageNotReadableException("Message not readable",
                new InvalidFormatException(),
                new MockClientHttpResponse());
    }

    @GetMapping(value = "/message-not-readable")
    public @ResponseBody String messageNotReadable() {
        throw new HttpMessageNotReadableException("Message not readable",
                new MockitoException("Mock exception"),
                new MockClientHttpResponse());
    }
}