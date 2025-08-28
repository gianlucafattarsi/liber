package com.github.gianlucafattarsi.liberapi.context.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Publisher", name = "Publisher")
public record PublisherDTO(

        long id,

        @NotNull
        @NotEmpty
        @Size(max = 100)
        @Schema(description = "Name", example = "Amazon Publishing")
        String name
) {
}
