package com.github.gianlucafattarsi.liberapi.context.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Library", name = "Library")
public record LibraryDTO(

        @Schema(description = "ID", example = "1")
        Long id,

        @Schema(description = "Name", example = "MyLibrary")
        String name,

        @Schema(description = "Path to the library", example = "/path/to/library")
        String path
) {
}
