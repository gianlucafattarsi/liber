package com.github.gianlucafattarsi.liberapi.context.settings.service;

import com.github.gianlucafattarsi.liberapi.context.settings.repository.Settings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SettingsService {

    private final Settings settings;
}
