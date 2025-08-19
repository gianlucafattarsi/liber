package com.github.gianlucafattarsi.liberapi.context.account.user.mapper;

import com.github.gianlucafattarsi.liberapi.context.account.permission.entity.Permission;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.NewUserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.UserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.dto.UserDTO;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    @DisplayName("Map User to UserDTO")
    public void whenUserToUserDTO_thenOk() {

        Permission permission1 = new Permission(1L, "perm1", "Permission 1");
        Permission permission2 = new Permission(2L, "perm2", "Permission 2");

        User user = new User(1L, "testUser", "testEmail@mail.com", "testUserPwd", List.of(permission1, permission2));

        final UserDTO userDTO = mapper.toDTO(user);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO).usingRecursiveComparison()
                           .ignoringFields("permissions", "password", "administrator")
                           .isEqualTo(user);
    }

    @Test
    @DisplayName("Map Null User to UserDTO")
    public void whenUserNullToUserDTO_thenOk() {

        final UserDTO userDTO = mapper.toDTO(null);

        assertThat(userDTO).isNull();
    }

    @Test
    @DisplayName("Map User to UserDTO with Administrator")
    public void whenUserToUserDTOWithAdministrator_thenOk() {

        Permission permission1 = new Permission(1L, "perm1", "Permission 1");
        Permission permission2 = new Permission(2L, "perm2", "Permission 2");
        Permission permissionAdmin = new Permission(2L, "USER_ADMIN", "Permission ADMIN");

        User userAdmin = new User(1L,
                "testUser",
                "testEmail@mail.com",
                "testUserPwd",
                List.of(permission1, permission2, permissionAdmin));
        User userAdmin2 = new User(1L, "testUser", "testEmail@mail.com", "testUserPwd", List.of(permissionAdmin));
        User user = new User(1L, "testUser", "testEmail@mail.com", "testUserPwd", List.of(permission1, permission2));
        User user2 = new User(1L, "testUser", "testEmail@mail.com", "testUserPwd", null);

        UserDTO userDTO = mapper.toDTO(user);
        assertThat(userDTO.administrator()).isFalse();

        userDTO = mapper.toDTO(user2);
        assertThat(userDTO.administrator()).isFalse();

        userDTO = mapper.toDTO(userAdmin);
        assertThat(userDTO.administrator()).isTrue();

        userDTO = mapper.toDTO(userAdmin2);
        assertThat(userDTO.administrator()).isTrue();
    }

    @Test
    @DisplayName("Map User null id to UserDTO")
    public void whenUserNullIdToUserDTO_thenOk() {

        Permission permission1 = new Permission(1L, "perm1", "Permission 1");
        Permission permission2 = new Permission(2L, "perm2", "Permission 2");

        User user = new User(null, "testUser", "testEmail@mail.com", "testUserPwd", List.of(permission1, permission2));

        final UserDTO userDTO = mapper.toDTO(user);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO).usingRecursiveComparison()
                           .ignoringFields("permissions", "password", "administrator", "id")
                           .isEqualTo(user);
    }

    @Test
    @DisplayName("Map UserPayload to User")
    public void whenUserPayloadToUser_thenOk() {

        UserPayload userPayload = new UserPayload("testUser", "test@mail.com", "testPassword", true);

        final User user = mapper.toEntity(userPayload);

        assertThat(user).isNotNull();
        assertThat(user).usingRecursiveComparison()
                        .ignoringFields("administrator", "permissions", "id")
                        .isEqualTo(userPayload);
    }

    @Test
    @DisplayName("Map UserPayload null to User")
    public void whenUserPayloadNullToUser_thenOk() {

        final User user = mapper.toEntity((UserPayload) null);

        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Map NewUserPayload null to User")
    public void whenNewUserPayloadNullToUser_thenOk() {

        final User user = mapper.toEntity((NewUserPayload) null);

        assertThat(user).isNull();
    }
}
