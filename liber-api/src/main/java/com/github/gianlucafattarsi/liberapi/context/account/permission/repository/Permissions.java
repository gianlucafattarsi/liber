package com.github.gianlucafattarsi.liberapi.context.account.permission.repository;


import com.github.gianlucafattarsi.liberapi.context.account.permission.entity.Permission;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Permissions extends JpaRepository<Permission, Long> {

    Optional<User> findByCode(String code);
}