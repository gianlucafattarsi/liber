package com.github.gianlucafattarsi.liberapi.context.account.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Schema(description = "User information")
@Validated
public record UserInfo(

        @Schema(description = "Username", example = "testuser", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty
        @NotNull
        @Size(min = 5, max = 30)
        String userName,

        @Schema(description = "Email", example = "testuser@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Email
        @Size(min = 5, max = 30)
        String email
) {
}
