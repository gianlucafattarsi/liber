package com.github.gianlucafattarsi.liberapi.context.account.session.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
@Schema(description = "User login information")
public record UserLogin(

        @NotNull
        @NotEmpty
        @Size(min = 5, max = 30)
        @Schema(description = "User name", example = "gianluca.fattarsi", requiredMode = Schema.RequiredMode.REQUIRED)
        String username,

        @NotNull
        @NotEmpty
        @Size(min = 5, max = 100)
        @Schema(description = "User password", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password) {
}