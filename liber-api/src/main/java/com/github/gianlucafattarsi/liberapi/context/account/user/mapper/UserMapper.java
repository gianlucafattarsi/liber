package com.github.gianlucafattarsi.liberapi.context.account.user.mapper;

import com.github.gianlucafattarsi.liberapi.context.account.permission.entity.Permission;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.NewUserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.UserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.dto.UserDTO;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Named("permissionsToAdministrator")
    static boolean permissionsToAdministrator(List<Permission> permissions) {
        return permissions != null && permissions.stream()
                                                 .anyMatch(p -> p.getCode()
                                                                 .equals("USER_ADMIN"));
    }

    @Mapping(source = "permissions", target = "administrator", qualifiedByName = "permissionsToAdministrator")
    UserDTO toDTO(User user);

    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(UserPayload userPayload);

    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(NewUserPayload userPayload);
}
