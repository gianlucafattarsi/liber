package com.github.gianlucafattarsi.liberapi.context.account.user.service;

import com.github.gianlucafattarsi.liberapi.application.config.security.authentication.AuthenticationFacade;
import com.github.gianlucafattarsi.liberapi.application.config.security.authentication.Principal;
import com.github.gianlucafattarsi.liberapi.application.exception.type.NoSuchEntityException;
import com.github.gianlucafattarsi.liberapi.application.exception.type.UserAlreadyExistsException;
import com.github.gianlucafattarsi.liberapi.application.mail.MailService;
import com.github.gianlucafattarsi.liberapi.context.account.permission.entity.Permission;
import com.github.gianlucafattarsi.liberapi.context.account.permission.repository.Permissions;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.NewUserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.UserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.dto.UserDTO;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import com.github.gianlucafattarsi.liberapi.context.account.user.mapper.UserMapper;
import com.github.gianlucafattarsi.liberapi.context.account.user.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

    Permission permission1 = new Permission(1L, "perm1", "Permission 1");
    Permission permission2 = new Permission(2L, "perm2", "Permission 2");
    Permission permissionAdmin = new Permission(3L, "USER_ADMIN", "Permission ADMIN");

    User userAdmin = new User(1L,
            "testAdmin",
            "testAdminEmail@mail.com",
            "testUserPwd",
            new ArrayList<>(List.of(permission1, permission2, permissionAdmin)));
    User userAdmin2 = new User(2L,
            "testAdmin2",
            "testAdmin2Email@mail.com",
            "testUserPwd",
            new ArrayList<>(List.of(permissionAdmin)));
    User user = new User(3L,
            "testUser",
            "testEmail@mail.com",
            "testUserPwd",
            new ArrayList<>(List.of(permission1, permission2)));
    User user2 = new User(4L, "testUser2", "test2Email@mail.com", "testUserPwd", null);
    User user5 = new User(5L,
            "testUser",
            null,
            "testUserPwd",
            null);

    final List<User> usersRepo = List.of(userAdmin, userAdmin2, user, user2, user5);

    @MockitoBean
    private UserRepository users;
    @MockitoBean
    private Permissions permissions;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private AuthenticationFacade authenticationFacade;

    @MockitoBean
    private ApplicationEventPublisher applicationEventPublisher;
    @MockitoBean
    private MailService mailService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        given(users.findAll()).willReturn(usersRepo);
        given(users.findByUserNameIgnoreCase(any())).willAnswer(answer -> {
            String username = answer.getArgument(0);
            return usersRepo.stream()
                            .filter(user -> user.getUserName()
                                                .equalsIgnoreCase(username))
                            .findFirst();
        });
        given(users.findById(any())).willAnswer(answer -> {
            long id = answer.getArgument(0);
            return usersRepo.stream()
                            .filter(user -> user.getId()
                                                .equals(id))
                            .findFirst();
        });
    }

    @Test
    @DisplayName("Load all users")
    public void whenLoadAllUsers_thenOk() {

        final List<UserDTO> usersDTO = userService.loadAllUsers();

        assertThat(usersDTO).isNotNull();
        assertThat(usersDTO).hasSize(5);
        for (int i = 0; i < 3; i++) {
            assertThat(usersDTO.get(i)).usingRecursiveComparison()
                                       .ignoringFields("permissions", "id", "administrator")
                                       .isEqualTo(usersRepo.get(i));
        }
    }

    @Test
    @DisplayName("Load user by username")
    public void whenLoadUserByUsername_thenOk() {

        UserDetails user = userService.loadUserByUsername("testAdmin");

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testAdmin");


        user = userService.loadUserByUsername("TESTadmin");

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testAdmin");
    }

    @Test
    @DisplayName("Load user by username not found")
    public void whenLoadUserByUsernameNotFound_thenThrowException() {

        assertThatThrownBy(() -> userService.loadUserByUsername("unknownUser"))
                .isInstanceOf(org.springframework.security.core.userdetails.UsernameNotFoundException.class);
        assertThatThrownBy(() -> userService.loadUserByUsername("unknownUser")).hasMessageContaining("unknownUser");
    }

    @Test
    @DisplayName("Validate user by username")
    public void whenValidateUserByUsername_thenOk() {
        assertThatCode(() -> userService.validateUser("testAdmin")).doesNotThrowAnyException();

        assertThatCode(() -> userService.validateUser("TESTadmin")).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Validate user by username not found")
    public void whenValidateUserByUsernameNotFound_thenThrowException() {

        assertThatThrownBy(() -> userService.validateUser("unknownUser"))
                .isInstanceOf(org.springframework.security.core.userdetails.UsernameNotFoundException.class);
        assertThatThrownBy(() -> userService.validateUser("unknownUser")).hasMessageContaining("unknownUser");
    }

    @Test
    @DisplayName("Create new user")
    public void whenCreateNewUser_thenOk() {

        NewUserPayload userPayload = new NewUserPayload("newUser", "new@email.com");

        given(passwordEncoder.encode(any())).willAnswer(answer -> {
            String passwordToEncode = answer.getArgument(0);
            if (StringUtils.isBlank(passwordToEncode)) {
                return null;
            } else {
                return "encoded_" + passwordToEncode; // Simulate encoding
            }
        });
        given(users.save(any())).willAnswer(answer -> {
            User user = answer.getArgument(0);
            assertThat(user.getId()).isNull();
            assertThat(user.getPassword()).isNotNull();
            user.updateId(1L);
            return user;
        });
        given(permissions.findAll()).willReturn(List.of(permission1, permission2, permissionAdmin));

        userService.createNewUser(userPayload);
    }

    @Test
    @DisplayName("Create new user ( user already exists)")
    public void whenCreateNewUser_userAlreadyExists_thenThrowException() {

        NewUserPayload userPayload = new NewUserPayload("newUser", "new@email.com");

        final User existingUser = new User(1L, "newUser", "new@email.com", "userPwd", List.of());

        given(users.findByUserNameIgnoreCase("newUser")).willReturn(Optional.of(existingUser));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createNewUser(userPayload);
        });
        assertThat(exception).isNotNull();
        assertThat(exception.getUserName()).isEqualTo("newUser");
        assertThat(exception.getEmail()).isEqualTo("new@email.com");
    }

    @Test
    @DisplayName("Edit existing user")
    public void whenEditExistingUser_thenOk() {

        UserPayload userPayload = new UserPayload("testUser", "newTestUser@email.com", "newPassword", false);

        given(users.save(any())).willAnswer(answer -> {
            User user = answer.getArgument(0);
            assertThat(user.getEmail()).isEqualTo(userPayload.email());
            assertThat(user.getPassword()).isEqualTo(passwordEncoder.encode(userPayload.password()));
            return user;
        });
        given(permissions.findAll()).willReturn(new ArrayList<>(List.of(permission1, permission2, permissionAdmin)));


        UserDTO userDTO = userService.editUser(3L, userPayload);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.id()).isEqualTo(3L);
        assertThat(userDTO.userName()).isEqualTo(userPayload.userName());
        assertThat(userDTO.email()).isEqualTo(userPayload.email());
        assertThat(userDTO.administrator()).isFalse();

        // With administrator set to true
        UserPayload userPayloadAdmin = new UserPayload("testAdmin", "newTestUser@email.com", "newPassword", true);

        userDTO = userService.editUser(1L, userPayloadAdmin);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.id()).isEqualTo(1L);
        assertThat(userDTO.userName()).isEqualTo(userPayloadAdmin.userName());
        assertThat(userDTO.email()).isEqualTo(userPayloadAdmin.email());
        assertThat(userDTO.administrator()).isTrue();

        // with no permissions associated and email
        UserPayload userNoPermissionsPayload = new UserPayload(user5.getUserName(),
                "newTestUser@email.com",
                user5.getPassword(),
                false);

        userDTO = userService.editUser(5L, userNoPermissionsPayload);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.id()).isEqualTo(5L);
        assertThat(userDTO.userName()).isEqualTo(userNoPermissionsPayload.userName());
        assertThat(userDTO.email()).isEqualTo(userNoPermissionsPayload.email());
    }

    @Test
    @DisplayName("Edit unexisting user")
    public void whenEditUnexistingUser_thenThrowException() {

        UserPayload userPayload = new UserPayload("testUser", "newTestUser@email.com", "newPassword", false);

        assertThatThrownBy(() -> userService.editUser(300L, userPayload)).isInstanceOf(NoSuchEntityException.class);
    }

    @Test
    @DisplayName("Delete user")
    public void whenDeleteUser_thenOk() {
        given(authenticationFacade.getCurrentPrincipal()).willReturn(new Principal(1L,
                "testUser",
                "testUser@email.com"));

        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }

    @Test
    @DisplayName("Delete different logged user")
    public void whenDeleteDifferentLoggedUser_thenThrowException() {

        given(authenticationFacade.getCurrentPrincipal()).willReturn(new Principal(1L,
                "testUser",
                "testUser@email.com"));

        assertThatThrownBy(() -> userService.deleteUser(2L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("You cannot delete a different user than yourself");

        given(authenticationFacade.getCurrentPrincipal()).willReturn(null);

        assertThatThrownBy(() -> userService.deleteUser(2L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("You cannot delete the user");
    }

    @TestConfiguration
    public class UserServiceTestConfig {
        @Bean
        public UserService userService() {
            return new UserService(users,
                    permissions,
                    passwordEncoder,
                    userMapper,
                    applicationEventPublisher,
                    mailService,
                    authenticationFacade);
        }
    }
}
