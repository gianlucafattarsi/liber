package com.github.gianlucafattarsi.liberapi.context.account.user.controller.impl;

import com.github.gianlucafattarsi.liberapi.context.account.user.controller.UsersApi;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.NewUserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.UserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.dto.UserDTO;
import com.github.gianlucafattarsi.liberapi.context.account.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UsersController implements UsersApi {

    private final UserService service;


    @Override
    public List<UserDTO> loadUsers() {
        return service.loadAllUsers();
    }

    @Override
    public void createUser(final NewUserPayload userPayload) {
        service.createNewUser(userPayload);
    }

    @Override
    public UserDTO updateUser(final long id, final UserPayload userPayload) {
        return service.editUser(id, userPayload);
    }

    @Override
    public void deleteUser(final long id) {
        service.deleteUser(id);
    }
}
