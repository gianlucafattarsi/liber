package com.github.gianlucafattarsi.liberapi.application.config.security.authentication;

import com.github.gianlucafattarsi.liberapi.application.security.JwtTokenManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public Principal getCurrentPrincipal() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                                             .getAuthentication()
                                             .getPrincipal();
        if (jwt != null) {
            return new Principal(
                    Long.valueOf(jwt.getClaimAsString(JwtTokenManager.CLAIM_KEY_USERID)),
                    jwt.getClaimAsString("sub"),
                    jwt.getClaimAsString(JwtTokenManager.CLAIM_KEY_MAIL),
                    jwt.getClaimAsString(JwtTokenManager.CLAIM_KEY_LANG));
        }
        return null;
    }
}