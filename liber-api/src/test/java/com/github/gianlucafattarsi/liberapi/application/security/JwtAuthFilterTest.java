package com.github.gianlucafattarsi.liberapi.application.security;

import com.github.gianlucafattarsi.liberapi.context.account.permission.entity.Permission;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtAuthFilterTest {

    String token;

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
    @DisplayName("Filter without auth")
    public void redirectTest_noHeaderAuth() throws ServletException, IOException {
        assertThat(SecurityContextHolder.getContext()
                                        .getAuthentication()).isNull();

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        JwtAuthFilter filter = new JwtAuthFilter(jwtTokenManager);
        filter.doFilterInternal(req, res, chain);

        assertThat(SecurityContextHolder.getContext()
                                        .getAuthentication()).isNull();
    }

    @Test
    @DisplayName("Filter with auth (no Bearer key)")
    public void redirectTest_noBearerKey() throws ServletException, IOException {
        assertThat(SecurityContextHolder.getContext()
                                        .getAuthentication()).isNull();

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        req.addHeader("Authorization", token);
        JwtAuthFilter filter = new JwtAuthFilter(jwtTokenManager);
        filter.doFilterInternal(req, res, chain);

        assertThat(SecurityContextHolder.getContext()
                                        .getAuthentication()).isNull();
    }

    @Test
    @DisplayName("Filter with valid auth")
    public void redirectTest_validAuth() throws ServletException, IOException {
        assertThat(SecurityContextHolder.getContext()
                                        .getAuthentication()).isNull();

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        // request with auth
        req.removeHeader("Authorization");
        req.addHeader("Authorization", "Bearer " + token);
        final JwtAuthFilter filter = new JwtAuthFilter(jwtTokenManager);
        filter.doFilterInternal(req, res, chain);

        assertThat(SecurityContextHolder.getContext()
                                        .getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext()
                                        .getAuthentication()
                                        .getName()).isEqualTo("test");

    }
}
