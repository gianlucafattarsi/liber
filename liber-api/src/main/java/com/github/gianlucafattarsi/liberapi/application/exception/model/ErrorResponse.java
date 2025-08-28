package com.github.gianlucafattarsi.liberapi.application.exception.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "Error response", name = "LiberErrorResponse")
@Data
public class ErrorResponse {

    @NotNull
    private ErrorData error = null;

    public ErrorResponse error(ErrorData error) {
        this.error = error;
        return this;
    }
}