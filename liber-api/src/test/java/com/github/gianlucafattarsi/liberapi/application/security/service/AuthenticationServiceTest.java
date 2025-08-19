package com.github.gianlucafattarsi.liberapi.application.security.service;

import com.github.gianlucafattarsi.liberapi.application.exception.type.NoSuchEntityException;
import com.github.gianlucafattarsi.liberapi.application.security.JwtTokenManager;
import com.github.gianlucafattarsi.liberapi.context.account.permission.entity.Permission;
import com.github.gianlucafattarsi.liberapi.context.account.session.dto.Session;
import com.github.gianlucafattarsi.liberapi.context.account.session.mapper.UserInfoMapper;
import com.github.gianlucafattarsi.liberapi.context.account.session.payload.UserLogin;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.RefreshToken;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import com.github.gianlucafattarsi.liberapi.context.account.user.repository.RefreshTokenRepository;
import com.github.gianlucafattarsi.liberapi.context.account.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthenticationServiceTest {

    @MockitoBean
    private AuthenticationManager authenticationManager;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtTokenManager jwtTokenManager;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Revoke refresh token")
    public void whenRevokeRefreshToken_thenOk() {

        assertThatCode(() -> authenticationService.revokeRefreshToken(any())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Refresh access token")
    public void whenRefreshAccessToken_thenOk() {

        Permission permission = new Permission(1L, "ROLE_DEFAULT", "Default user permission");
        User user = new User(1L, "test", "test@email.com", "testPassword", List.of(permission));

        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID(),
                user,
                Instant.now(),
                Instant.now()
                       .plus(1,
                               ChronoUnit.HOURS));

        given(refreshTokenRepository.findByIdAndExpiresAtAfter(any(), any())).willReturn(Optional.of(refreshToken));

        RefreshToken newRefreshToken = new RefreshToken(UUID.randomUUID(),
                user,
                Instant.now(),
                Instant.now()
                       .plus(1,
                               ChronoUnit.HOURS));

        given(refreshTokenRepository.save(any())).willReturn(newRefreshToken);

        final Session session = authenticationService.refreshToken(refreshToken.getId());

        assertThat(session).isNotNull();
        assertThat(session.getAccessToken()).isNotNull();
        assertThat(session.getRefreshToken()).isEqualTo(newRefreshToken.getId());
        assertThat(session.getUserInfo()).isNotNull();
        assertThat(session.getUserInfo()
                          .userName()).isNotNull();
    }

    @Test
    @DisplayName("Refresh access token (wrong refresh token)")
    public void whenRefreshAccessToken_withWrongRefreshToken_thenOk() {

        given(refreshTokenRepository.findByIdAndExpiresAtAfter(any(), any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.refreshToken(UUID.randomUUID())).isInstanceOf(
                BadCredentialsException.class);
    }

    @Test
    @DisplayName("Authentication")
    public void whenAuthentication_thenOk() {

        assertThat(SecurityContextHolder.getContext()
                                        .getAuthentication()).isNull();

        Permission permission = new Permission(1L, "ROLE_DEFAULT", "Default user permission");
        User user = new User(1L, "test", "test@email.com", "testPassword", List.of(permission));
        given(userService.findByUsername("test")).willReturn(Optional.of(user));

        given(authenticationManager.authenticate(any())).willReturn(new UsernamePasswordAuthenticationToken("test",
                "testPassword"));

        RefreshToken newRefreshToken = new RefreshToken(UUID.randomUUID(),
                user,
                Instant.now(),
                Instant.now()
                       .plus(1,
                               ChronoUnit.HOURS));

        given(refreshTokenRepository.save(any())).willReturn(newRefreshToken);

        UserLogin userLogin = new UserLogin("test", "password");
        final Session session = authenticationService.authenticate(userLogin);

        assertThat(session).isNotNull();
        assertThat(session.getAccessToken()).isNotNull();
        assertThat(session.getRefreshToken()).isEqualTo(newRefreshToken.getId());
        assertThat(session.getUserInfo()).isNotNull();
        assertThat(session.getUserInfo()
                          .userName()).isEqualTo(userLogin.username());

        assertThat(SecurityContextHolder.getContext()
                                        .getAuthentication()).isNotNull();

    }

    @Test
    @DisplayName("Authentication (unexisting user)")
    public void whenAuthentication_withUnexistingUser_thenOk() {

        given(userService.findByUsername(any())).willReturn(Optional.empty());

        UserLogin userLogin = new UserLogin("test", "password");
        assertThatThrownBy(() -> authenticationService.authenticate(userLogin)).isInstanceOf(
                NoSuchEntityException.class);
    }

    @TestConfiguration
    public class AuthenticationServiceTestConfig {
        @Bean
        public AuthenticationService authenticationService() {
            return new AuthenticationService(authenticationManager,
                    userService,
                    refreshTokenRepository,
                    jwtTokenManager,
                    userInfoMapper);
        }
    }
}
