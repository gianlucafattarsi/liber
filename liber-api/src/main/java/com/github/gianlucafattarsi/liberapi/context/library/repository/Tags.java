package com.github.gianlucafattarsi.liberapi.context.library.repository;

import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Tags extends JpaRepository<Tag, Long> {

}