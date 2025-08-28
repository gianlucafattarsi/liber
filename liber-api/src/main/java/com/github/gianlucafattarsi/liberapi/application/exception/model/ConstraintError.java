package com.github.gianlucafattarsi.liberapi.application.exception.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "Constraint error")
@Getter
@Builder
@JsonDeserialize(builder = ConstraintError.ConstraintErrorBuilder.class)
public class ConstraintError {

    @Schema(description = "Field name", example = "userName")
    private String fieldName;

    @ArraySchema(schema = @Schema(description = "List of constraints not respected",
            example = "[\"userName must be between 5 and 30 characters\", \"userName must not be null\"]"))
    private List<String> constraintsNotRespected;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ConstraintErrorBuilder {
    }
}