package com.github.gianlucafattarsi.liberapi.context.account.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleResolver;
import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.security.JwtTokenManager;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.impl.UsersController;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.NewUserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.UserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.dto.UserDTO;
import com.github.gianlucafattarsi.liberapi.context.account.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UsersController.class)
@Import({LocaleUtilsMessage.class, LocaleResolver.class})
@EnableMethodSecurity(proxyTargetClass = true)
@EnableWebSecurity
@WithMockUser(username = "test", authorities = {"USER_ADMIN"})
public class UsersControllerTest {

    UserDTO userDTO = new UserDTO(1L, "user1", "user1@email.com", false);
    UserDTO userDTO2 = new UserDTO(2L, "user2", "user2@email.com", false);
    UserDTO userDTOAdmin = new UserDTO(3L, "userAdmin", "userAdmin@email.com", true);

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private JwtTokenManager jwtTokenManager;

    @MockitoBean
    private UserService userService;

    @WithAnonymousUser
    @Test
    @DisplayName("Load all users without authentication")
    public void whenLoadUsers_thenStatus401() throws Exception {

        mvc.perform(get("/api/users").with(csrf())
                                     .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().is4xxClientError())
           .andExpect(status().isUnauthorized())
           .andDo(print());
    }

    @Test
    @DisplayName("Load all users")
    public void whenLoadUsers_thenOk() throws Exception {

        given(userService.loadAllUsers()).willReturn(List.of(userDTO, userDTO2, userDTOAdmin));

        mvc.perform(get("/api/users").with(csrf())
                                     .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().is2xxSuccessful())
           .andExpect(status().isOk())
           .andExpect(content()
                   .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
           .andExpect(MockMvcResultMatchers.jsonPath("$[0].userName")
                                           .value(userDTO.userName()))
           .andExpect(MockMvcResultMatchers.jsonPath("$[1].userName")
                                           .value(userDTO2.userName()))
           .andExpect(MockMvcResultMatchers.jsonPath("$[2].userName")
                                           .value(userDTOAdmin.userName()))
           .andDo(print());
    }

    @WithMockUser(username = "test", authorities = {"USER_DEFAULT"})
    @Test
    @DisplayName("Create user")
    public void whenCreateUser_thenOK() throws Exception {

        NewUserPayload userPayload = new NewUserPayload("user1", "user1@email.com");

        mvc.perform(post("/api/users").with(csrf())
                                      .content(new ObjectMapper().writeValueAsString(userPayload))
                                      .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().is2xxSuccessful())
           .andExpect(status().isNoContent())
           .andDo(print());
    }

    @Test
    @DisplayName("Update user")
    public void whenUpdateUser_thenOK() throws Exception {

        UserPayload userPayload = new UserPayload("user1", "user1@email.com", "newPassword", false);

        given(userService.editUser(1L, userPayload)).willReturn(userDTO);

        mvc.perform(put("/api/users/{id}", 1L).with(csrf())
                                              .content(new ObjectMapper().writeValueAsString(userPayload))
                                              .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().is2xxSuccessful())
           .andExpect(status().isOk())
           .andExpect(content()
                   .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(MockMvcResultMatchers.jsonPath("userName")
                                           .value(userDTO.userName()))
           .andDo(print());
    }

    @WithMockUser(username = "test", authorities = {"USER_DEFAULT"})
    @Test
    @DisplayName("Update user with insufficient permissions")
    public void whenUpdateUserWithInsufficientPermissions_thenStatus403() throws Exception {

        UserPayload userPayload = new UserPayload("user1", "user1@email.com", "newPassword", false);

        given(userService.editUser(1L, userPayload)).willReturn(userDTO);

        mvc.perform(put("/api/users/{id}", 1L).with(csrf())
                                              .content(new ObjectMapper().writeValueAsString(userPayload))
                                              .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().is4xxClientError())
           .andExpect(status().isForbidden())
           .andDo(print());
    }

    @Test
    @DisplayName("Delete user")
    public void whenDeleteUser_thenOK() throws Exception {

        mvc.perform(delete("/api/users/{id}", 1L).with(csrf()))
           .andExpect(status().is2xxSuccessful())
           .andExpect(status().isNoContent())
           .andDo(print());
    }
}
