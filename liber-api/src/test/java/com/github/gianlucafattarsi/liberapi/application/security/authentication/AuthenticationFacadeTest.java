package com.github.gianlucafattarsi.liberapi.application.security.authentication;

import com.github.gianlucafattarsi.liberapi.application.config.security.authentication.AuthenticationFacade;
import com.github.gianlucafattarsi.liberapi.application.config.security.authentication.Principal;
import com.github.gianlucafattarsi.liberapi.application.security.JwtTokenManager;
import com.github.gianlucafattarsi.liberapi.application.security.JwtUser;
import com.github.gianlucafattarsi.liberapi.context.account.permission.entity.Permission;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AuthenticationFacadeTest {

    String token;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private AuthenticationFacade authenticationFacade;

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
    @DisplayName("Get current principal")
    public void testGetCurrentPrincipal() throws ServletException, IOException {

        Jwt jwt = new Jwt(token,
                Instant.now(),
                Instant.now(),
                Map.of("alg", "RS256"),
                Map.of("sub", "test", "mail", "test@mail.com", "id", 1));

        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                jwt,
                "testPassword");


        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication())
               .thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        final Principal principal = authenticationFacade.getCurrentPrincipal();

        assertThat(principal).isNotNull();
        assertThat(principal.id()).isEqualTo(1L);
        assertThat(principal.email()).isEqualTo("test@mail.com");
        assertThat(principal.username()).isEqualTo("test");
    }

    @Test
    @DisplayName("Get current principal null")
    public void testGetCurrentPrincipalNull() throws ServletException, IOException {

        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                null,
                "testPassword");


        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication())
               .thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        final Principal principal = authenticationFacade.getCurrentPrincipal();

        assertThat(principal).isNull();
    }
}
