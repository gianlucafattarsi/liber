package com.github.gianlucafattarsi.liberapi.context.account.session.controller;

import com.github.gianlucafattarsi.liberapi.context.account.session.dto.Session;
import com.github.gianlucafattarsi.liberapi.context.account.session.payload.UserLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "Auth", description = "Session related class level tag")
@RequestMapping(path = "/api/auth", produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE})
@SecurityRequirements()
public interface SessionApi {

    @Operation(summary = "Login", description = "Logs user into the system", tags = {
            "Auth",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Session.class))),
            @ApiResponse(responseCode = "401", description = "Username or password not valid",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @PostMapping(value = "/login")
    Session login(
            @Valid
            @RequestBody
            UserLogin userLogin);

    @Operation(summary = "Refresh token", description = "Obtain a new access token by providing a refresh token", tags = {
            "Auth",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New session",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Session.class))),
            @ApiResponse(responseCode = "400", description = "Response failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @PostMapping(value = "/refresh-token")
    Session refreshToken(
            @Parameter(description = "Refresh token", example = "647db92a-8258-4e0f-a152-711f8b76e2af")
            @Valid
            @RequestBody
            UUID refreshToken);

    @Operation(summary = "Logout", description = "User logout", tags = {"Auth",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logout successful"),
            @ApiResponse(responseCode = "400", description = "Response failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @PostMapping(value = "/logout")
    void logout(
            @Parameter(description = "Refresh token", example = "647db92a-8258-4e0f-a152-711f8b76e2af")
            @Valid
            @RequestBody
            UUID refreshToken);
}
