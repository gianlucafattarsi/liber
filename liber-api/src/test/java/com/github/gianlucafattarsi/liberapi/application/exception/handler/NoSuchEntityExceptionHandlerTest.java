package com.github.gianlucafattarsi.liberapi.application.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.exception.handler.controller.RestProcessingExceptionThrowingController;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import com.github.gianlucafattarsi.liberapi.application.exception.type.NoSuchEntityException;
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
public class NoSuchEntityExceptionHandlerTest {

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
                .setControllerAdvice(new NoSuchEntityExceptionHandler(localeUtilsMessage))
                .build();
    }

    @Test
    @DisplayName("Test NoSuchEntityException")
    public void testHandleNoSuchEntityException() throws Exception {
        this.mvc.perform(get("/tests/exception/no-such-entity"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(NoSuchEntityException.class,
                        result.getResolvedException()))
                .andExpect(result -> {
                    ErrorResponse errorResponse = new ObjectMapper().readValue(result.getResponse()
                                                                                     .getContentAsString(),
                            ErrorResponse.class);
                    assertThat(errorResponse).isNotNull();
                    assertThat(errorResponse.getError()
                                            .getInternal()
                                            .getException()).isEqualTo(NoSuchEntityException.class.getName());
                    assertThat(errorResponse.getError()
                                            .getUserTitle()).isEqualTo(localeUtilsMessage.getMessage(
                            "NoSuchEntityException.title",
                            null,
                            result.getRequest()));
                    assertThat(errorResponse.getError()
                                            .getUserMessage()).isEqualTo(localeUtilsMessage.getMessage(
                            "NoSuchEntityException.detail",
                            null,
                            result.getRequest()));
                })
                .andDo(print());

    }
}
