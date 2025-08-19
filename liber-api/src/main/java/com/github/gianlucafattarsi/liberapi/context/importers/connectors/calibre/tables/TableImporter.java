package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables;

import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryDTO;

import java.sql.Connection;

public interface TableImporter {

    /**
     * Verify if table data can be imported.
     *
     * @param connection database connection to the Calibre library
     * @return true if the table can be imported
     */
    boolean check(Connection connection);

    /**
     * Return the order of the importer need to be executed.
     * The lower the number, the earlier it will be executed.
     *
     * @return the order of the importer
     */
    int order();

    /**
     * Return the name of the table to be imported.
     *
     * @return the name of the table
     */
    String getManagedTable();

    /**
     * Import the data from the table.
     *
     * @param connection database connection to the Calibre library
     * @param library    Library where the data will be imported
     * @param sourcePath the path to the source data
     */
    void importData(Connection connection, LibraryDTO library, String sourcePath);
}
