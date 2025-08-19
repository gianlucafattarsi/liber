package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables;

import com.github.gianlucafattarsi.liberapi.context.importers.websocket.controller.LibraryImporterMessageHandler;
import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Optional;


@Slf4j
public abstract class AbstractTableImporter<T, K> implements TableImporter {

    @Autowired
    private LibraryImporterMessageHandler messageHandler;

    @Override
    public boolean check(final Connection connection) {
        try {
            connection.prepareStatement(getSelectStatement())
                      .executeQuery();

            log.info("{} table is available for import.", getManagedTable());
            messageHandler.sendMessage(false,
                    "library.importer.checking.table",
                    new String[]{getManagedTable()});

            return true;
        } catch (Exception e) {
            log.error("{} table is not available for import.", getManagedTable(), e);
            messageHandler.sendMessage(true,
                    "library.importer.checking.table",
                    new String[]{getManagedTable()});
            return false;
        }
    }

    @Override
    public int order() {
        return 100;
    }

    @Override
    public void importData(final Connection connection, LibraryDTO library, String sourcePath) {
        log.debug("{}: Importing data...", getManagedTable());

        final Map<K, T> existingEntities = getExistingEntitiesBySearchKey();

        try {
            final ResultSet resultSet = connection.prepareStatement(getSelectStatement())
                                                  .executeQuery();

            while (resultSet.next()) {
                buildEntity(resultSet, existingEntities).ifPresent(getRepository()::save);
            }

            String recordsNr = String.valueOf(resultSet.getRow());

            log.debug("{}: Table imported successfully. Records: {}", getManagedTable(), recordsNr);
            messageHandler.sendMessage(false,
                    "library.importer.table.imported", new String[]{getManagedTable(), recordsNr, recordsNr});
        } catch (Exception e) {
            messageHandler.sendMessage(true,
                    "library.importer.table.not.imported", new String[]{getManagedTable()});
            log.error("{}:  Failed to import data.", getManagedTable(), e);
        }
    }

    protected abstract Map<K, T> getExistingEntitiesBySearchKey();

    protected abstract Optional<T> buildEntity(ResultSet resultSet, Map<K, T> existingEntities) throws Exception;

    protected abstract String getSelectStatement();

    protected abstract JpaRepository<T, Long> getRepository();
}
