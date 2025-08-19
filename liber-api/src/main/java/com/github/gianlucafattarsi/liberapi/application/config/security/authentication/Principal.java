package com.github.gianlucafattarsi.liberapi.application.config.security.authentication;

public record Principal(

        Long id,

        String username,

        String email,

        String lang
) {
}
