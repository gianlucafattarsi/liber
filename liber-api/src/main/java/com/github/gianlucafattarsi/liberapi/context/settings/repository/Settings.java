package com.github.gianlucafattarsi.liberapi.context.settings.repository;

import com.github.gianlucafattarsi.liberapi.context.settings.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Settings extends JpaRepository<Setting, Long> {

}