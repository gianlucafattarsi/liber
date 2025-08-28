package com.github.gianlucafattarsi.liberapi.context.importers.controller;

import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import com.github.gianlucafattarsi.liberapi.context.importers.dto.ImportParamDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Library importer", description = "Library imports related class level tag")
@RequestMapping(path = "/api/libraries/importers")
public interface LibraryImporterApi {

    @Operation(summary = "Load importers", description = "Load all available importers", tags = {
            "Library importer",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = String.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    List<String> loadImporters();

    @Operation(summary = "Test importer connection", description = "Test importer connection", tags = {
            "Library importer",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful connected", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Connection failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            path = "{name}/test")
    boolean testImporterConnection(
            @Parameter(description = "Name of the importer", example = "Calibre")
            @PathVariable("name")
            String name,

            @Valid
            @RequestBody
            ImportParamDTO importParamDTO);

    @Operation(summary = "Import data", description = "Import data into library", tags = {
            "Library importer",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful imported data"),
            @ApiResponse(responseCode = "400", description = "Connection failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            path = "{name}/import")
    void importData(
            @Parameter(description = "Name of the importer", example = "Calibre")
            @PathVariable("name")
            String importerName,

            @Valid
            @RequestBody
            ImportParamDTO importParamDTO);
}