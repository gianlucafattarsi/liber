package com.github.gianlucafattarsi.liberapi.context.importers.connectors;

import com.github.gianlucafattarsi.liberapi.context.importers.dto.ImportParamDTO;
import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryDTO;

public interface ImporterConnector {

    /**
     * Returns the name of the connector.
     *
     * @return the name of the connector
     */
    String getName();

    /**
     * Test the connection for the connector.
     *
     * @param importParamDTO the parameters required for the connection test
     * @return true if the connection is successful, false otherwise
     */
    boolean testConnection(final ImportParamDTO importParamDTO);

    /**
     * Imports data using the connector.
     *
     * @param library        library where the data will be imported
     * @param importParamDTO the parameters required for the import
     */
    void importData(LibraryDTO library, final ImportParamDTO importParamDTO);
}
