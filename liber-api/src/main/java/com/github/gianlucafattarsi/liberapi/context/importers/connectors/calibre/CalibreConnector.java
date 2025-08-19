package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre;

import com.github.gianlucafattarsi.liberapi.context.importers.connectors.ImporterConnector;
import com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables.TableImporter;
import com.github.gianlucafattarsi.liberapi.context.importers.dto.ImportParamDTO;
import com.github.gianlucafattarsi.liberapi.context.importers.websocket.controller.LibraryImporterMessageHandler;
import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class CalibreConnector implements ImporterConnector {

    private final List<TableImporter> tableImporters;

    private final LibraryImporterMessageHandler messageHandler;

    @Override
    public String getName() {
        return "Calibre";
    }

    @Override
    public boolean testConnection(final ImportParamDTO importParamDTO) {
        final DataSource build = buildDataSource(importParamDTO);

        try (final Connection connection = build.getConnection()) {
            tableImporters.sort(Comparator.comparingInt(TableImporter::order));

            for (TableImporter tableImporter : tableImporters) {
                if (!tableImporter.check(connection)) {
                    return false;
                }
            }
        } catch (Exception e) {
            messageHandler.sendMessage(true, "library.importer.error", new String[]{e.getMessage()});
            log.error("Error while testing connection to Calibre database", e);
            return false;
        }

        return true;
    }

    @Override
    public void importData(LibraryDTO library, ImportParamDTO importParamDTO) {
        messageHandler.sendMessage(false, "library.importer.started");

        final DataSource build = buildDataSource(importParamDTO);

        try (final Connection connection = build.getConnection()) {

            tableImporters.sort(Comparator.comparingInt(TableImporter::order));

            for (TableImporter tableImporter : tableImporters) {
                tableImporter.importData(connection, library, importParamDTO.importPath());
            }

            messageHandler.sendMessage(false, "library.importer.finished");
        } catch (Exception e) {
            messageHandler.sendMessage(true, "library.importer.finished");
            log.error("Error while importing data from Calibre database", e);
        }
    }

    private DataSource buildDataSource(ImportParamDTO importParamDTO) {
        DataSourceBuilder<?> dsBuilder = DataSourceBuilder.create();
        dsBuilder.driverClassName("org.sqlite.JDBC");
        dsBuilder.url("jdbc:sqlite:" + importParamDTO.importPath() + "/metadata.db");
        return dsBuilder.build();
    }
}
