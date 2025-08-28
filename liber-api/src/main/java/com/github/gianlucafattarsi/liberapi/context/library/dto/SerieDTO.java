package com.github.gianlucafattarsi.liberapi.context.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Serie", name = "Serie")
public record SerieDTO(

        long id,

        @NotNull
        @NotEmpty
        @Size(max = 100)
        @Schema(description = "Name", example = "Harry Potter")
        String name,

        List<BookDTO> books
) {
}
