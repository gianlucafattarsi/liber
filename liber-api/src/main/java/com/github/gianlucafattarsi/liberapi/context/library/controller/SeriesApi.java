package com.github.gianlucafattarsi.liberapi.context.library.controller;

import com.github.gianlucafattarsi.liberapi.context.library.dto.SerieDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("hasAuthority('USER_ADMIN')")
@Tag(name = "Series", description = "Series related class level tag")
@RequestMapping(path = "/api/series")
public interface SeriesApi {

    @Operation(summary = "Random", description = "Load a random `Serie`", tags = {
            "Series",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SerieDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @GetMapping(path = "random", produces = {MediaType.APPLICATION_JSON_VALUE})
    SerieDTO loadRandomSerie();
}
