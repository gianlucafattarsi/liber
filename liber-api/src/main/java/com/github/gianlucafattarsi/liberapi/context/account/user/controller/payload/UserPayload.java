package com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "UserPayload", name = "UserPayload")
public record UserPayload(

        @Schema(description = "UserName", example = "gianlucafattarsi", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty
        @NotNull
        @Size(min = 5, max = 30)
        String userName,

        @Schema(description = "Email", example = "gianlucafattarsi@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @Email
        @Size(min = 5, max = 100)
        String email,

        @Schema(description = "Password", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty
        @NotNull
        @Size(min = 5, max = 100)
        String password,

        @Schema(description = "ISO 639 alpha-2/alpha-3 language code.", example = "en", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @NotEmpty
        @Size(min = 2, max = 3)
        String lang,

        @Schema(description = "Administrator flag", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        boolean administrator
) {
}