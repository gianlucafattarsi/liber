package com.github.gianlucafattarsi.liberapi.application.security;

import com.github.gianlucafattarsi.liberapi.context.account.permission.entity.Permission;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class JwtUser implements UserDetails {

    @Serial
    private static final long serialVersionUID = -2833270119513844317L;
    private Long id;
    private String username;
    private String password;
    private String email;
    private String lang;
    private List<String> roles;

    public static JwtUser createInstance(User user) {

        return JwtUser.builder()
                      .id(user.getId())
                      .username(user.getUserName())
                      .email(user.getEmail())
                      .password(user.getPassword())
                      .lang(user.getLang())
                      .roles(user.getPermissions()
                                 .stream()
                                 .map(Permission::getCode)
                                 .toList())
                      .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return mapToGrantedAuthorities(this.getRoles());
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                          .map(SimpleGrantedAuthority::new)
                          .collect(Collectors.toList());
    }
}