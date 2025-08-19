package com.github.gianlucafattarsi.liberapi.context.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "LibrariesInfo", name = "LibrariesInfo")
public record LibrariesInfoDTO(

        @NotNull
        @Schema(description = "Number of books", example = "123")
        long books,

        @NotNull
        @Schema(description = "Number of series", example = "45")
        long series,

        @NotNull
        @Schema(description = "Number of publishers", example = "34")
        long publishers,

        @NotNull
        @Schema(description = "Number of authors", example = "68")
        long authors,

        @NotNull
        @Schema(description = "Number of tags", example = "78")
        long tags,

        @NotNull
        @Schema(description = "Number of languages", example = "5")
        long languages
) {
}
