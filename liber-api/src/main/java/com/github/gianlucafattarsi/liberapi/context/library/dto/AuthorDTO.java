package com.github.gianlucafattarsi.liberapi.context.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Author", name = "Author")
public record AuthorDTO(

        long id,

        @NotNull
        @NotEmpty
        @Size(max = 100)
        @Schema(description = "Name", example = "Roald Dahl")
        String name,

        @NotNull
        @NotEmpty
        @Size(max = 100)
        @Schema(description = "Name", example = "Dahl, Roald")
        String sort
) {
}
