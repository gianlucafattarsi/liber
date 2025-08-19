package com.github.gianlucafattarsi.liberapi.context.importers.controller.impl;

import com.github.gianlucafattarsi.liberapi.context.importers.LibraryImporter;
import com.github.gianlucafattarsi.liberapi.context.importers.controller.LibraryImporterApi;
import com.github.gianlucafattarsi.liberapi.context.importers.dto.ImportParamDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class LibraryImporterController implements LibraryImporterApi {

    private final LibraryImporter libraryImporter;

    @Override
    public List<String> loadImporters() {
        return libraryImporter.getAvailableImporters();
    }

    @Override
    public boolean testImporterConnection(String importerName, final ImportParamDTO importParamDTO) {
        return libraryImporter.testConnection(importerName, importParamDTO);
    }

    @Override
    public void importData(final String importerName, final ImportParamDTO importParamDTO) {
        libraryImporter.importLibrary(importerName, importParamDTO);
    }
}
