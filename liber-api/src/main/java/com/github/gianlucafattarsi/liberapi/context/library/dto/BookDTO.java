package com.github.gianlucafattarsi.liberapi.context.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Book", name = "Book")
public record BookDTO(

        long id,

        @NotNull
        @NotEmpty
        @Size(max = 200)
        @Schema(description = "Book title", example = "The Great Gatsby")
        String title,

        @NotNull
        @Schema(description = "ISO 639 alpha-2/alpha-3 language code.", example = "en")
        String language,

        @Schema(description = "Base64 encoded cover image")
        String cover,

        @NotNull
        @Schema(description = "Series index, if the book is part of a series. If not, it should be 0.", example = "0")
        int seriesIndex,

        SerieDTO serie

        // authors

        // publishers

        // tags
) {
}
