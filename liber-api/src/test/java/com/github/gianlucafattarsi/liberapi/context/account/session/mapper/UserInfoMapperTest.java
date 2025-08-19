package com.github.gianlucafattarsi.liberapi.context.account.session.mapper;

import com.github.gianlucafattarsi.liberapi.context.account.permission.entity.Permission;
import com.github.gianlucafattarsi.liberapi.context.account.session.dto.UserInfo;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserInfoMapperTest {

    @Autowired
    private UserInfoMapper mapper;

    @Test
    @DisplayName("Map User to UserInfo")
    public void whenUserToUserInfo_thenOk() {

        Permission permission1 = new Permission(1L, "perm1", "Permission 1");
        Permission permission2 = new Permission(2L, "perm2", "Permission 2");

        User user = new User(1L, "testUser", "testEmail@mail.com", "testUserPwd", List.of(permission1, permission2));

        final UserInfo userInfo = mapper.fromUser(user);

        assertThat(userInfo).isNotNull();
        assertThat(userInfo.userName()).isEqualTo(user.getUserName());
        assertThat(userInfo.email()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Map Null User to UserInfo")
    public void whenUserNullToUserInfo_thenOk() {

        final UserInfo userInfo = mapper.fromUser(null);

        assertThat(userInfo).isNull();
    }
}
