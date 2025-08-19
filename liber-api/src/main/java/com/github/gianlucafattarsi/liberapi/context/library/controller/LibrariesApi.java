package com.github.gianlucafattarsi.liberapi.context.library.controller;

import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@PreAuthorize("hasAuthority('USER_ADMIN')")
@Tag(name = "Library", description = "Library related class level tag")
@RequestMapping(path = "/api/libraries")
public interface LibrariesApi {

    @Operation(summary = "Load", description = "Load all `Library`", tags = {
            "Library",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = LibraryDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    List<LibraryDTO> loadLibraries();
}
