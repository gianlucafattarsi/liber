package com.github.gianlucafattarsi.liberapi.context.account.user.repository;

import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrUserName(String email, String userName);

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserNameIgnoreCase(String userName);
}