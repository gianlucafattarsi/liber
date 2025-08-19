package com.github.gianlucafattarsi.liberapi.context.account.session.mapper;

import com.github.gianlucafattarsi.liberapi.context.account.session.dto.UserInfo;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {


    UserInfo fromUser(User user);
}
