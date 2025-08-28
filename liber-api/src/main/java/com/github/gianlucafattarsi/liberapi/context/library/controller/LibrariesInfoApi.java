package com.github.gianlucafattarsi.liberapi.context.library.controller;

import com.github.gianlucafattarsi.liberapi.context.library.dto.LibrariesInfoDTO;
import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("hasAuthority('USER_ADMIN')")
@Tag(name = "Library Info", description = "Library info related class level tag")
@RequestMapping(path = "/api/libraries")
public interface LibrariesInfoApi {

    @Operation(summary = "Get library info", description = "Load information about the library", tags = {
            "Library Info",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LibraryInfoDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @GetMapping(path = "{id}/info", produces = {MediaType.APPLICATION_JSON_VALUE})
    LibraryInfoDTO loadLibraryInfo(
            @Parameter(description = "User id", example = "1")
            @PathVariable("id")
            long id
    );

    @Operation(summary = "Get libraries info", description = "Load information about the libraries", tags = {
            "Library Info",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LibrariesInfoDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @GetMapping(path = "info", produces = {MediaType.APPLICATION_JSON_VALUE})
    LibrariesInfoDTO loadLibrariesInfo();
}
