package com.github.gianlucafattarsi.liberapi.context.library.dto;

import com.github.gianlucafattarsi.liberapi.context.library.entity.BookFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "BookData", name = "BookData")
public record BookDataDTO(

        long id,

        @NotNull
        @Schema(description = "Book format", example = "EPUB")
        BookFormat format,

        @NotNull
        @Schema(description = "Uncompressed size in bytes", example = "2048000")
        Integer uncompressedSize,

        @NotNull
        @NotEmpty
        @Size(max = 300)
        @Schema(description = "File name without extension", example = "Lo Hobbit - J. R. R. Tolkien")
        String name
) {
}
