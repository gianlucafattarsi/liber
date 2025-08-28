package com.github.gianlucafattarsi.liberapi.context.library.controller.impl;

import com.github.gianlucafattarsi.liberapi.context.library.controller.LibrariesInfoApi;
import com.github.gianlucafattarsi.liberapi.context.library.dto.LibrariesInfoDTO;
import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryInfoDTO;
import com.github.gianlucafattarsi.liberapi.context.library.service.librariesinfo.LibrariesInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LibrariesInfoController implements LibrariesInfoApi {

    private final LibrariesInfoService librariesInfoService;

    @Override
    public LibraryInfoDTO loadLibraryInfo(final long id) {
        return librariesInfoService.loadLibraryInfo(id);
    }

    @Override
    public LibrariesInfoDTO loadLibrariesInfo() {
        return librariesInfoService.loadLibrariesInfo();
    }


}
