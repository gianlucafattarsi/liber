package com.github.gianlucafattarsi.liberapi.context.account.session.controller.impl;


import com.github.gianlucafattarsi.liberapi.application.security.service.AuthenticationService;
import com.github.gianlucafattarsi.liberapi.context.account.session.controller.SessionApi;
import com.github.gianlucafattarsi.liberapi.context.account.session.dto.Session;
import com.github.gianlucafattarsi.liberapi.context.account.session.payload.UserLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class SessionController implements SessionApi {

    private final AuthenticationService authenticationService;

    @Override
    public Session login(final UserLogin userLogin) {
        return authenticationService.authenticate(userLogin);
    }

    @Override
    public Session refreshToken(final UUID refreshToken) {
        return authenticationService.refreshToken(refreshToken);
    }

    @Override
    public void logout(final UUID refreshToken) {
        authenticationService.revokeRefreshToken(refreshToken);
    }
}
