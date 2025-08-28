package com.github.gianlucafattarsi.liberapi.application.exception.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "Error data")
@Getter
@Builder
@JsonDeserialize(builder = ErrorData.ErrorDataBuilder.class)
public class ErrorData {

    @Schema(description = "Title of error", example = "Failed")
    @NotNull
    private String userTitle;

    @NotNull
    @Schema(description = "Description of error ", example = "Data not valid")
    private String userMessage;

    @Builder.Default
    private ErrorInternal internal = null;

    private List<ConstraintError> constraintErrors;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ErrorDataBuilder {
    }
}