package com.github.gianlucafattarsi.liberapi.application.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.exception.handler.controller.RestProcessingExceptionThrowingController;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import com.github.gianlucafattarsi.liberapi.application.exception.type.ExpiredTokenException;
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
public class ExpiredTokenExceptionHandlerTest {

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
                .setControllerAdvice(new ExpiredTokenExceptionHandler(localeUtilsMessage))
                .build();
    }

    @Test
    @DisplayName("Test ExpiredTokenException")
    public void testHandleExpiredTokenException() throws Exception {
        this.mvc.perform(get("/tests/exception/expired-token"))
                .andExpect(status().isAccepted())
                .andExpect(result -> assertInstanceOf(ExpiredTokenException.class,
                        result.getResolvedException()))
                .andExpect(result -> {
                    ErrorResponse errorResponse = new ObjectMapper().readValue(result.getResponse()
                                                                                     .getContentAsString(),
                            ErrorResponse.class);
                    assertThat(errorResponse).isNotNull();
                    assertThat(errorResponse.getError()
                                            .getInternal()
                                            .getException()).isEqualTo(ExpiredTokenException.class.getName());
                    assertThat(errorResponse.getError()
                                            .getUserTitle()).isEqualTo(localeUtilsMessage.getMessage(
                            "ExpiredTokenException.title",
                            null,
                            result.getRequest()));
                    assertThat(errorResponse.getError()
                                            .getUserMessage()).isEqualTo(localeUtilsMessage.getMessage(
                            "ExpiredTokenException.detail",
                            null,
                            result.getRequest()));
                })
                .andDo(print());

    }
}
