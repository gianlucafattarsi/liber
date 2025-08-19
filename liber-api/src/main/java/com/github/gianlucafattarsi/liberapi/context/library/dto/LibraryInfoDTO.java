package com.github.gianlucafattarsi.liberapi.context.library.dto;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Library;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Schema(description = "LibraryInfo", name = "LibraryInfo")
public record LibraryInfoDTO(

        Library library,

        @NotNull
        @Schema(description = "Number of books in the library", example = "123")
        long booksCount,

        @Schema(description = "Timestamp of the last book added to the library", example = "2023-10-01T12:00:00Z")
        Instant lastBookAddedAt,

        @NotNull
        @Schema(description = "Number of series in the library", example = "45")
        long seriesCount,

        @NotNull
        @Schema(description = "Number of languages in the library", example = "5")
        long languagesCount
) {
}
