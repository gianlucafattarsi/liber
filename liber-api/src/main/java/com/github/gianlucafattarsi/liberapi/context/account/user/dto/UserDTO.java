package com.github.gianlucafattarsi.liberapi.context.account.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "User", name = "User")
public record UserDTO(

        @NotNull
        long id,

        @NotNull
        @Size(min = 5, max = 30)
        String userName,

        @Email
        @Size(min = 5, max = 100)
        String email,

        @Schema(description = "ISO 639 alpha-2/alpha-3 language code.", example = "en", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @NotEmpty
        @Size(min = 2, max = 3)
        String lang,

        boolean administrator
) {
}