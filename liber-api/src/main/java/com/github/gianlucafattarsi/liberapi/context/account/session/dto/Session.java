package com.github.gianlucafattarsi.liberapi.context.account.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Schema(description = "Session information")
@Getter
@Setter
@Validated
public class Session {

    @Schema(description = "User information")
    @NotNull
    @Valid
    private UserInfo userInfo;

    @Schema(description = "User environment", example = "prod")
    private String environment;

    @Schema(description = "Access token", example = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJnaWFubHVjYS5mYXR0YXJzaSIsIm1haWwiOiJnaWFubHVjYUBlbWFpbC5jb20iLCJleHAiOjE3NDY5MTY3MTEsImlhdCI6MTc0NjkxMzExMSwicm9sZXMiOltdLCJpc0VuYWJsZWQiOnRydWV9.k3ohenFLJQCer_IqRrxJED0jCBBj8jxw0hmLVlU4qE6rI6OUvqzC68mqZD8wD9BhhavPONZZzg738UpJ3Q8zo6-wUsyR6VtAD36S5fkkdKUIV52ADYsa2ZKXhwE0FEweivTVkTh_o7p7QJwsVEDU2ZCxD4ReKBU930d_nxERBjkwQm-0vY8FUd7yPGTBpfTtfethdL07oRS0o5SZ2PtwN6OUKdbpSjBMvHXCtDzWSFvpJiOnXQlqP9UjHOrnr59xrEPrbGmuF3DHc25vyl0A1N5udvA0eVgo9Cw83Cvff-UuGIFNL-eMnz9YjpmCCDD76HaiVljOqjX9rwCrS4NOtQ")
    @NotNull
    private String accessToken;

    @Schema(description = "Refresh token", example = "647db92a-8258-4e0f-a152-711f8b76e2af")
    @NotNull
    private UUID refreshToken;
}