package com.github.gianlucafattarsi.liberapi.context.library.controller.impl;

import com.github.gianlucafattarsi.liberapi.context.library.controller.LibrariesApi;
import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryDTO;
import com.github.gianlucafattarsi.liberapi.context.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class LibrariesController implements LibrariesApi {

    private final LibraryService libraryService;

    @Override
    public List<LibraryDTO> loadLibraries() {
        return libraryService.loadLibraries();
    }
}
