package com.github.gianlucafattarsi.liberapi.application.exception.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

@Schema(description = "Internal error")
@Validated
@Getter
@Builder
@JsonDeserialize(builder = ErrorInternal.ErrorInternalBuilder.class)
public class ErrorInternal {

    @Schema(description = "Raised exception", example = "com.github.fattazzo.wowmaster.exception.handler.exception.GenericException")
    @NotNull
    private String exception;

    @Schema(description = "Error stacktrace message")
    private String stack;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ErrorInternalBuilder {
    }
}