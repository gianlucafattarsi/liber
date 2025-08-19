package com.github.gianlucafattarsi.liberapi.context.account.session.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gianlucafattarsi.liberapi.application.exception.model.ErrorResponse;
import com.github.gianlucafattarsi.liberapi.application.exception.type.NoSuchEntityException;
import com.github.gianlucafattarsi.liberapi.application.security.service.AuthenticationService;
import com.github.gianlucafattarsi.liberapi.context.account.session.dto.Session;
import com.github.gianlucafattarsi.liberapi.context.account.session.dto.UserInfo;
import com.github.gianlucafattarsi.liberapi.context.account.session.payload.UserLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SessionControllerTests {

    private MockMvc mvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Login and get session")
    public void whenLogin_thenOK()
            throws Exception {

        UserLogin userLogin = new UserLogin("testUser", "testPassword");

        UserInfo userInfo = new UserInfo("testUser", "test@email.com");

        Session session = new Session();
        session.setAccessToken("i234n253425n3u45n2o34nrfnq23jri23");
        session.setEnvironment("test");
        session.setRefreshToken(UUID.randomUUID());
        session.setUserInfo(userInfo);

        when(authenticationService.authenticate(any()))
                .thenReturn(session);

        mvc.perform(post("/api/auth/login")
                   .content(new ObjectMapper().writeValueAsString(userLogin))
                   .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(result -> {
               Session responseSession = new ObjectMapper().readValue(result.getResponse()
                                                                            .getContentAsString(), Session.class);
               Mockito.verify(authenticationService)
                      .authenticate(userLogin);
               Mockito.verifyNoMoreInteractions(authenticationService);

               // Validate the session response
               assert responseSession.getAccessToken() != null;
               assert responseSession.getRefreshToken() != null;
               assert responseSession.getUserInfo() != null;
               assert "testUser".equals(responseSession.getUserInfo()
                                                       .userName());
           })
           .andDo(print());
    }

    @Test
    @DisplayName("Login with invalid credentials")
    public void whenLogin_withInvalidCredentials_thenStatus401()
            throws Exception {

        UserLogin userLogin = new UserLogin("testUser", "testPassword");

        when(authenticationService.authenticate(any()))
                .thenThrow(new UsernameNotFoundException(String.format("No user found with username '%s'.",
                        userLogin.username())));

        mvc.perform(post("/api/auth/login")
                   .content(new ObjectMapper().writeValueAsString(userLogin))
                   .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().is4xxClientError())
           .andExpect(status().isUnauthorized())
           .andDo(print());
    }

    @Test
    @DisplayName("Login with non-existing user")
    public void whenLogin_withNonExistingUser_thenStatus404()
            throws Exception {

        UserLogin userLogin = new UserLogin("testUser", "testPassword");

        when(authenticationService.authenticate(any()))
                .thenThrow(new NoSuchEntityException());

        mvc.perform(post("/api/auth/login")
                   .content(new ObjectMapper().writeValueAsString(userLogin))
                   .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().is4xxClientError())
           .andExpect(status().isNotFound())
           .andExpect(result -> {
               ErrorResponse errorResponse = new ObjectMapper().readValue(result.getResponse()
                                                                                .getContentAsString(),
                       ErrorResponse.class);

               assert errorResponse != null;
               assert errorResponse.getError()
                                   .getInternal()
                                   .getException()
                                   .equals(NoSuchEntityException.class.getName());
           })
           .andDo(print());
    }

    @Test
    @DisplayName("Logout")
    public void whenLogout_thenSuccess() throws Exception {
        UUID refreshToken = UUID.randomUUID();

        mvc.perform(post("/api/auth/logout")
                   .content(new ObjectMapper().writeValueAsString(refreshToken))
                   .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().is2xxSuccessful())
           .andDo(print());

        Mockito.verify(authenticationService)
               .revokeRefreshToken(refreshToken);
        Mockito.verifyNoMoreInteractions(authenticationService);
    }

    @Test
    @DisplayName("Refresh token")
    public void whenRefreshToken_thenOK() throws Exception {
        UUID refreshToken = UUID.randomUUID();

        UserInfo userInfo = new UserInfo("testUser", "test@email.com");

        Session session = new Session();
        session.setAccessToken("i234n253425n3u45n2o34nrfnq23jri23");
        session.setEnvironment("test");
        session.setRefreshToken(UUID.randomUUID());
        session.setUserInfo(userInfo);

        when(authenticationService.refreshToken(refreshToken))
                .thenReturn(session);


        mvc.perform(post("/api/auth/refresh-token")
                   .content(new ObjectMapper().writeValueAsString(refreshToken))
                   .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().is2xxSuccessful())
           .andExpect(result -> {
               Session responseSession = new ObjectMapper().readValue(result.getResponse()
                                                                            .getContentAsString(), Session.class);

               // Validate the session response
               assert responseSession.getAccessToken() != null;
               assert responseSession.getRefreshToken() != null;
               assert responseSession.getUserInfo() != null;
               assert "testUser".equals(responseSession.getUserInfo()
                                                       .userName());
           })
           .andDo(print());
    }
}
