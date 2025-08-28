package com.github.gianlucafattarsi.liberapi.context.importers;

import com.github.gianlucafattarsi.liberapi.context.importers.connectors.ImporterConnector;
import com.github.gianlucafattarsi.liberapi.context.importers.dto.ImportParamDTO;
import com.github.gianlucafattarsi.liberapi.context.importers.websocket.controller.LibraryImporterMessageHandler;
import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryDTO;
import com.github.gianlucafattarsi.liberapi.context.library.service.LibraryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class LibraryImporter {

    private final List<ImporterConnector> connectors;

    private final LibraryService libraryService;

    private final LibraryImporterMessageHandler messageHandler;

    @Async("threadPoolExecutor")
    @Transactional
    public void importLibrary(String importerName, ImportParamDTO dto) {
        LibraryDTO library;
        if (dto.library()
               .id() == null) {
            library = libraryService.createLibrary(dto.library());
        } else {
            library = libraryService.loadLibrary(dto.library()
                                                    .id());
        }

        final Optional<ImporterConnector> importerConnector = getConnectorByName(importerName);

        if (importerConnector.isPresent() && importerConnector.get()
                                                              .testConnection(dto)) {
            log.debug("Start import data for {}", importerName);
            importerConnector.get()
                             .importData(library, dto);
        }
    }

    /**
     * Retriave a connector by its name.
     *
     * @param name the name of the connector
     * @return an Optional containing the connector if found, otherwise empty
     */
    private Optional<ImporterConnector> getConnectorByName(String name) {
        return connectors.stream()
                         .filter(connector -> connector.getName()
                                                       .equals(name))
                         .findFirst();
    }

    public boolean testConnection(String importerName, ImportParamDTO dto) {
        messageHandler.sendMessage(false, "library.importer.test.started");

        final Optional<ImporterConnector> importerConnector = getConnectorByName(importerName);

        boolean testOK = false;
        if (importerConnector.isPresent()) {
            log.debug("Start testing connection for {}", importerName);
            testOK = importerConnector.get()
                                      .testConnection(dto);
        }

        messageHandler.sendMessage(!testOK, "library.importer.test.finished");
        return testOK;
    }

    public List<String> getAvailableImporters() {
        return connectors.stream()
                         .map(ImporterConnector::getName)
                         .toList();
    }
}
