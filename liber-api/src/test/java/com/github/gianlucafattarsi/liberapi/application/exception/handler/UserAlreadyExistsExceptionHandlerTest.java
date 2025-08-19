package com.github.gianlucafattarsi.liberapi.application.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.exception.handler.controller.RestProcessingExceptionThrowingController;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import com.github.gianlucafattarsi.liberapi.application.exception.type.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserAlreadyExistsExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private LocaleUtilsMessage localeUtilsMessage;

    @InjectMocks
    private RestProcessingExceptionThrowingController restProcessingExceptionThrowingController;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .standaloneSetup(restProcessingExceptionThrowingController)
                .setControllerAdvice(new UserAlreadyExistsExceptionHandler(localeUtilsMessage))
                .build();
    }

    @Test
    @DisplayName("Test UserAlreadyExistsException")
    public void testHandleUserAlreadyExistsException() throws Exception {
        this.mvc.perform(get("/tests/exception/user-already-exists"))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertInstanceOf(UserAlreadyExistsException.class,
                        result.getResolvedException()))
                .andExpect(result -> {
                    UserAlreadyExistsException exception = (UserAlreadyExistsException) result.getResolvedException();
                    assertThat(exception).isNotNull();
                    assertThat(exception.getUserName()).isNotNull();
                    assertThat(exception.getEmail()).isNotNull();

                    ErrorResponse errorResponse = new ObjectMapper().readValue(result.getResponse()
                                                                                     .getContentAsString(),
                            ErrorResponse.class);
                    assertThat(errorResponse).isNotNull();
                    assertThat(errorResponse.getError()
                                            .getUserTitle()).isEqualTo(localeUtilsMessage.getMessage(
                            "UserAlreadyExistsException.title",
                            null,
                            result.getRequest()));
                    assertThat(errorResponse.getError()
                                            .getUserMessage()).isEqualTo(localeUtilsMessage.getMessage(
                            "UserAlreadyExistsException.detail",
                            new String[]{exception.getUserName(), exception.getEmail()},
                            result.getRequest()));
                })
                .andDo(print());

    }
}
