package com.github.gianlucafattarsi.liberapi.context.library.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Schema(description = "Book", name = "Book")
public record BookDTO(

        long id,

        @NotNull
        @NotEmpty
        @Size(max = 200)
        @Schema(description = "Book title", example = "The Great Gatsby")
        String title,

        @Size(max = 15_000)
        @Schema(description = "Book description", example = "Description.....")
        String description,

        @NotNull
        @Schema(description = "ISO 639 alpha-2/alpha-3 language code.", example = "en")
        String language,

        @Schema(description = "Base64 encoded cover image")
        String cover,

        @NotNull
        @Schema(description = "Series index, if the book is part of a series. If not, it should be 1.", example = "10")
        int seriesIndex,

        @Schema(description = "Series the book belongs to")
        SerieDTO serie,

        @ArraySchema(uniqueItems = true)
        Set<AuthorDTO> authors,

        @ArraySchema(uniqueItems = true)
        Set<PublisherDTO> publishers,

        @ArraySchema(uniqueItems = true)
        Set<TagDTO> tags,

        @ArraySchema(uniqueItems = true)
        Set<BookDataDTO> data
) {
}
