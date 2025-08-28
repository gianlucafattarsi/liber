package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables.books;

import com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables.TableImporter;
import com.github.gianlucafattarsi.liberapi.context.importers.websocket.controller.LibraryImporterMessageHandler;
import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryDTO;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Author;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Book;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Language;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Library;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Publisher;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Rating;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Serie;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Tag;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Authors;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Books;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Languages;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Publishers;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Ratings;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Series;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class BooksImporter implements TableImporter {

    private final LibraryImporterMessageHandler messageHandler;

    private final BooksImporterDataTransfer dataTransfer;

    private final Books books;
    private final Tags tags;
    private final Series series;
    private final Ratings ratings;
    private final Publishers publishers;
    private final Languages languages;
    private final Authors authors;

    @Override
    public boolean check(final Connection connection) {
        try {
            for (String statement : BooksStatements.getCheckStatements()) {
                connection.prepareStatement(statement)
                          .executeQuery();
            }
            log.info("Books table is available for import.");
            messageHandler.sendMessage(false,
                    "library.importer.checking.table",
                    new String[]{getManagedTable()});

            return true;
        } catch (Exception e) {
            log.error("Books table is not available for import.", e);
            messageHandler.sendMessage(true,
                    "library.importer.checking.table",
                    new String[]{getManagedTable()});
            return false;
        }
    }

    @Override
    public int order() {
        return 1000;
    }

    @Override
    public String getManagedTable() {
        return "Books";
    }

    @Override
    public void importData(final Connection connection, LibraryDTO library, String sourcePath) {
        log.debug("BOOKS: Importing data...");

        try {
            dataTransfer.createLibraryDir(library);

            final ResultSet resultSetCount = connection.prepareStatement(BooksStatements.COUNT_STATEMENT)
                                                       .executeQuery();

            resultSetCount.next();
            int recordsNr = resultSetCount.getInt(1);


            BooksDataHolder dataHolder = new BooksDataHolder(connection,
                    tags,
                    series,
                    ratings,
                    publishers,
                    languages,
                    authors);

            final ResultSet resultSet = connection.prepareStatement(BooksStatements.SELECT_STATEMENT)
                                                  .executeQuery();

            int currentRecord = 1;
            while (resultSet.next()) {
                final int calibreBookId = resultSet.getInt("id");
                final String title = resultSet.getString("title");
                final String sort = resultSet.getString("sort");
                final int seriesIndex = resultSet.getInt("series_index");
                final String authorSort = resultSet.getString("author_sort");
                final String path = resultSet.getString("path");
                final String description = resultSet.getString("description");
                Set<Tag> tags = dataHolder.getTags(calibreBookId);
                Set<Author> authors = dataHolder.getAuthors(calibreBookId);
                Set<Publisher> publishers = dataHolder.getPublishers(calibreBookId);
                Language language = dataHolder.getLanguage(calibreBookId);
                Rating rating = dataHolder.getRating(calibreBookId);
                Serie serie = dataHolder.getSerie(calibreBookId);

                Book book = Book.builder()
                                .library(Library.builder()
                                                .id(library.id())
                                                .build())
                                .title(title)
                                .description(description)
                                .sort(sort)
                                .seriesIndex(serie != null ? seriesIndex : 0)
                                .authorSort(authorSort)
                                .path(path)
                                .tags(tags)
                                .authors(authors)
                                .publishers(publishers)
                                .language(language)
                                .rating(rating)
                                .serie(serie)
                                .build();
                book.addData(dataHolder.getData(calibreBookId));

                books.save(book);

                dataTransfer.copyIntoLibrary(library,
                        Paths.get(sourcePath), Paths.get(path));

                if (currentRecord % 100 == 0) {
                    messageHandler.sendMessage(false,
                            "library.importer.table.import.records.imported",
                            new String[]{getManagedTable(), String.valueOf(currentRecord), String.valueOf(recordsNr)});
                    log.debug("BOOKS: Records imported: {} of {}.", currentRecord, recordsNr);
                }

                currentRecord++;
            }

            messageHandler.sendMessage(false,
                    "library.importer.table.imported",
                    new String[]{getManagedTable(), String.valueOf(recordsNr), String.valueOf(recordsNr)});
            log.debug("BOOKS: Table imported successfully.");
        } catch (Exception e) {
            dataTransfer.deleteLibraryDir(library);
            messageHandler.sendMessage(true,
                    "library.importer.table.not.imported", new String[]{getManagedTable()});
            log.error("BOOKS:  Failed to import data.", e);
        }
    }


}
