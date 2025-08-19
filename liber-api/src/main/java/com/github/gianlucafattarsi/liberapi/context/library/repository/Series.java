package com.github.gianlucafattarsi.liberapi.context.library.repository;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Series extends JpaRepository<Serie, Long> {

}