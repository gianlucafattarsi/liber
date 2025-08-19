package com.github.gianlucafattarsi.liberapi.application.security;

import com.github.gianlucafattarsi.liberapi.context.account.permission.entity.Permission;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.RefreshToken;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtTokenManagerTest {

    String token;

    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @BeforeEach
    public void setUp() {

        Permission permission = new Permission(1L, "USER_ADMIN", "Permission ADMIN");

        User user = new User(1L,
                "test",
                "test@mail.com",
                "testPwd",
                new ArrayList<>(List.of(permission)));
        JwtUser jwtUser = JwtUser.createInstance(user);

        token = jwtTokenManager.generateAccessToken(jwtUser);
    }

    @Test
    @DisplayName("Access token generator")
    public void whenGenerateAccessToken() {

        Permission permission = new Permission(1L, "USER_ADMIN", "Permission ADMIN");

        User user = new User(1L,
                "test",
                "test@mail.com",
                "testPwd",
                new ArrayList<>(List.of(permission)));
        JwtUser jwtUser = JwtUser.createInstance(user);

        final String accessToken = jwtTokenManager.generateAccessToken(jwtUser);

        assertThat(accessToken).isNotNull();
    }

    @Test
    @DisplayName("Resolve user from access token")
    public void whenResolveUser_fromAccessToken_thenOk() {

        final UserDetails userDetails = jwtTokenManager.resolveUser(token);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("test");
        assertThat(userDetails.getPassword()).isBlank();
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities()
                              .stream()
                              .findFirst()
                              .get()
                              .getAuthority()).isEqualTo("USER_ADMIN");
    }

    @Test
    @DisplayName("Generate refresh token")
    public void whenGenerateRefreshToken() {

        Permission permission = new Permission(1L, "USER_ADMIN", "Permission ADMIN");

        User user = new User(1L,
                "test",
                "test@mail.com",
                "testPwd",
                new ArrayList<>(List.of(permission)));

        final RefreshToken refreshToken = jwtTokenManager.generateRefreshToken(user);

        assertThat(refreshToken).isNotNull();
    }

    @Test
    @DisplayName("Extract username from token")
    public void whenExtractUsername_thenOk() {

        final String username = jwtTokenManager.extractUsername(token);

        assertThat(username).isNotNull();
        assertThat(username).isEqualTo("test");
    }
}
