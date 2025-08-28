package com.github.gianlucafattarsi.liberapi.context.account.user.controller;

import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.NewUserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.UserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Users", description = "Users related class level tag")
@RequestMapping(path = "/api/users")
public interface UsersApi {

    @Operation(summary = "Load Users", description = "Load all `User`", tags = {
            "Users",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    List<UserDTO> loadUsers();

    @Operation(summary = "Create user", description = "Create an `User`", tags = {
            "Users",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Creation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @SecurityRequirements()
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    void createUser(
            @Valid
            @RequestBody
            NewUserPayload userPayload);

    @Operation(summary = "Update user", description = "Update an existing `User`", tags = {
            "Users",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Update failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found for the given id",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @PutMapping(path = "{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    UserDTO updateUser(
            @Parameter(description = "User id", example = "1")
            @PathVariable("id")
            long id,

            @Valid
            @RequestBody
            UserPayload userPayload);

    @Operation(summary = "Delete an user", description = "Delete an `User`", tags = {
            "Users",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ok"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @DeleteMapping(path = "{id}")
    void deleteUser(
            @Parameter(description = "User id", example = "1")
            @PathVariable("id")
            long id);
}