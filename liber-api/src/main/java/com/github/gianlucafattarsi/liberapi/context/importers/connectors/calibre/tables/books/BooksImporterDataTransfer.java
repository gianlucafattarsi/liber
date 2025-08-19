package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables.books;

import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Slf4j
@Component
public class BooksImporterDataTransfer {

    /**
     * Creates the directory for the library.
     *
     * @param library the library for which to create the directory
     */
    public void createLibraryDir(LibraryDTO library) {

        final Path path = Paths.get(library.path());

        if (path.toFile()
                .exists()) {
            log.debug("Library directory already exists: {}", path);
        } else {
            try {
                //noinspection ResultOfMethodCallIgnored
                path.toFile()
                    .mkdirs();
                log.info("Library directory created: {}", path);
            } catch (Exception e) {
                log.error("Failed to create library directory: {}", path, e);
                throw new RuntimeException("Failed to create library directory", e);
            }
        }
    }

    /**
     * Copies the data from the source path into the library directory.
     *
     * @param library    the library into which to copy the data
     * @param sourcePath the source path from which to copy the data
     * @param bookPath   the path of the book within the library
     */
    public void copyIntoLibrary(LibraryDTO library, Path sourcePath, Path bookPath) throws Exception {
        final Path destinationPath = Paths.get(library.path())
                                          .resolve(bookPath);

        Path sourceBookPath = sourcePath.resolve(bookPath);

        FileUtils.copyDirectory(sourceBookPath.toFile(), destinationPath.toFile());
    }

    /**
     * Deletes the library directory.
     *
     * @param library the library for which to delete the directory
     */
    public void deleteLibraryDir(LibraryDTO library) {

        final Path path = Paths.get(library.path());

        if (path.toFile()
                .exists()) {
            try {
                FileSystemUtils.deleteRecursively(path);
                log.info("Library directory deleted: {}", path);
            } catch (Exception e) {
                log.error("Failed to delete library directory: {}", path, e);
                throw new RuntimeException("Failed to delete library directory", e);
            }
        }
    }
}
