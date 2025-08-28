package com.github.gianlucafattarsi.liberapi.context.library.repository;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Authors extends JpaRepository<Author, Long> {

}