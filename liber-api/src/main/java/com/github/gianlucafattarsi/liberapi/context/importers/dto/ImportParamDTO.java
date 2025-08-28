package com.github.gianlucafattarsi.liberapi.context.importers.dto;

import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "LibraryImportParam", name = "LibraryImportParam")
public record ImportParamDTO(

        @NotNull
        @Schema(description = "Library. If the ID is empty, a new one will be created.")
        LibraryDTO library,

        @NotNull
        @Schema(description = "Import path.", example = "/path/to/import")
        String importPath,

        @Schema(description = "Additional parameters for the import", example = "{\"key\":\"value\"}")
        String additionalParams
) {
}
