package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables.books;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Author;
import com.github.gianlucafattarsi.liberapi.context.library.entity.BookData;
import com.github.gianlucafattarsi.liberapi.context.library.entity.BookFormat;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Language;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Publisher;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Rating;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Serie;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Tag;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Authors;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Languages;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Publishers;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Ratings;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Series;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Tags;
import jakarta.validation.constraints.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BooksDataHolder {

    private final Tags tags;
    private final Series series;
    private final Ratings ratings;
    private final Publishers publishers;
    private final Languages languages;
    private final Authors authors;

    private Map<String, Tag> libraryTags;
    private Map<String, Serie> librarySeries;
    private Map<Integer, Rating> libraryRatings;
    private Map<String, Publisher> libraryPublishers;
    private Map<String, Language> libraryLanguages;
    private Map<String, Author> libraryAuthors;

    private Map<Integer, List<BookData>> calibreBooksData;
    private Map<Integer, List<String>> calibreBooksTags;
    private Map<Integer, List<String>> calibreBooksAuthors;
    private Map<Integer, List<String>> calibreBooksPublishers;
    private Map<Integer, String> calibreBooksLanguages;
    private Map<Integer, Integer> calibreBooksRatings;
    private Map<Integer, String> calibreBooksSeries;

    public BooksDataHolder(Connection connection, Tags tags, Series series, Ratings ratings, Publishers publishers,
                           Languages languages, Authors authors) throws Exception {
        this.tags = tags;
        this.series = series;
        this.ratings = ratings;
        this.publishers = publishers;
        this.languages = languages;
        this.authors = authors;

        loadLibraryData(connection);
        loadCalibreData(connection);
    }

    private void loadLibraryData(Connection connection) {
        libraryTags = tags.findAll()
                          .stream()
                          .collect(Collectors.toMap(Tag::getName, Function.identity()));

        librarySeries = series.findAll()
                              .stream()
                              .collect(Collectors.toMap(Serie::getName, Function.identity()));

        libraryRatings = ratings.findAll()
                                .stream()
                                .collect(Collectors.toMap(Rating::getRating,
                                        Function.identity()));

        libraryPublishers = publishers.findAll()
                                      .stream()
                                      .collect(Collectors.toMap(Publisher::getName,
                                              Function.identity()));

        libraryLanguages = languages.findAll()
                                    .stream()
                                    .collect(Collectors.toMap(Language::getLangCode,
                                            Function.identity()));

        libraryAuthors = authors.findAll()
                                .stream()
                                .collect(Collectors.toMap(Author::getName,
                                        Function.identity()));
    }

    private void loadCalibreData(Connection connection) throws Exception {
        // Load books data =====================================================================
        ResultSet resultSet = connection.prepareStatement(BooksStatements.BOOKS_DATA_STATEMENT)
                                        .executeQuery();

        calibreBooksData = new HashMap<>();
        while (resultSet.next()) {
            final int bookId = resultSet.getInt("book");
            final String format = resultSet.getString("format");
            final Integer size = resultSet.getInt("uncompressed_size");
            String name = resultSet.getString("name");

            calibreBooksData.putIfAbsent(bookId, new ArrayList<>());
            calibreBooksData.get(bookId)
                            .add(BookData.builder()
                                         .format(BookFormat.valueOf(format))
                                         .uncompressedSize(size)
                                         .name(name)
                                         .build());
        }

        // Load tags =====================================================================
        resultSet = connection.prepareStatement(BooksStatements.BOOKS_TAGS_STATEMENT)
                              .executeQuery();

        calibreBooksTags = new HashMap<>();
        while (resultSet.next()) {
            final int bookId = resultSet.getInt("book");
            final String tagName = resultSet.getString("name");

            calibreBooksTags.putIfAbsent(bookId, new ArrayList<>());
            calibreBooksTags.get(bookId)
                            .add(tagName);
        }

        // Load authors =====================================================================
        resultSet = connection.prepareStatement(BooksStatements.BOOKS_AUTHORS_STATEMENT)
                              .executeQuery();

        calibreBooksAuthors = new HashMap<>();
        while (resultSet.next()) {
            final int bookId = resultSet.getInt("book");
            final String authorName = resultSet.getString("name");

            calibreBooksAuthors.putIfAbsent(bookId, new ArrayList<>());
            calibreBooksAuthors.get(bookId)
                               .add(authorName);
        }

        // Load publishers =====================================================================
        resultSet = connection.prepareStatement(BooksStatements.BOOKS_PUBLSHERS_TATEMENTS)
                              .executeQuery();

        calibreBooksPublishers = new HashMap<>();
        while (resultSet.next()) {
            final int bookId = resultSet.getInt("book");
            final String authorName = resultSet.getString("name");

            calibreBooksPublishers.putIfAbsent(bookId, new ArrayList<>());
            calibreBooksPublishers.get(bookId)
                                  .add(authorName);
        }

        // Load languages =====================================================================
        resultSet = connection.prepareStatement(BooksStatements.BOOKS_LANGUAGE_STATEMENT)
                              .executeQuery();

        calibreBooksLanguages = new HashMap<>();
        while (resultSet.next()) {
            final int bookId = resultSet.getInt("book");
            final String langCode = resultSet.getString("lang_code");

            calibreBooksLanguages.putIfAbsent(bookId, langCode);
        }

        // Load ratings =====================================================================
        resultSet = connection.prepareStatement(BooksStatements.BOOKS_RATINGS_STATEMENT)
                              .executeQuery();

        calibreBooksRatings = new HashMap<>();
        while (resultSet.next()) {
            final int bookId = resultSet.getInt("book");
            final Integer rating = resultSet.getInt("rating");

            calibreBooksRatings.putIfAbsent(bookId, rating);
        }

        // Load series =====================================================================
        resultSet = connection.prepareStatement(BooksStatements.BOOKS_SERIES_STATEMENT)
                              .executeQuery();

        calibreBooksSeries = new HashMap<>();
        while (resultSet.next()) {
            final int bookId = resultSet.getInt("book");
            final String serieName = resultSet.getString("name");

            calibreBooksSeries.putIfAbsent(bookId, serieName);
        }
    }

    public @NotNull List<BookData> getData(Integer calibraBookId) {
        return calibreBooksData.entrySet()
                               .stream()
                               .filter(entry -> entry.getKey()
                                                     .equals(calibraBookId))
                               .map(Map.Entry::getValue)
                               .flatMap(List::stream)
                               .toList();
    }

    public Set<Tag> getTags(Integer calibreBookId) {
        return calibreBooksTags.entrySet()
                               .stream()
                               .filter(entry -> entry.getKey()
                                                     .equals(calibreBookId))
                               .map(Map.Entry::getValue)
                               .flatMap(List::stream)
                               .map(tagName -> libraryTags.getOrDefault(tagName, null))
                               .filter(Objects::nonNull)
                               .collect(Collectors.toSet());
    }

    public Set<Author> getAuthors(Integer calibreBookId) {
        return calibreBooksAuthors.entrySet()
                                  .stream()
                                  .filter(entry -> entry.getKey()
                                                        .equals(calibreBookId))
                                  .map(Map.Entry::getValue)
                                  .flatMap(List::stream)
                                  .map(tagName -> libraryAuthors.getOrDefault(tagName, null))
                                  .filter(Objects::nonNull)
                                  .collect(Collectors.toSet());
    }

    public Set<Publisher> getPublishers(Integer calibreBookId) {
        return calibreBooksPublishers.entrySet()
                                     .stream()
                                     .filter(entry -> entry.getKey()
                                                           .equals(calibreBookId))
                                     .map(Map.Entry::getValue)
                                     .flatMap(List::stream)
                                     .map(tagName -> libraryPublishers.getOrDefault(tagName, null))
                                     .filter(Objects::nonNull)
                                     .collect(Collectors.toSet());
    }

    public Language getLanguage(Integer calibreBookId) {
        return calibreBooksLanguages.entrySet()
                                    .stream()
                                    .filter(entry -> entry.getKey()
                                                          .equals(calibreBookId))
                                    .map(Map.Entry::getValue)
                                    .map(tagName -> libraryLanguages.getOrDefault(tagName, null))
                                    .filter(Objects::nonNull)
                                    .findFirst()
                                    .orElse(null);
    }

    public Rating getRating(Integer calibreBookId) {
        return calibreBooksRatings.entrySet()
                                  .stream()
                                  .filter(entry -> entry.getKey()
                                                        .equals(calibreBookId))
                                  .map(Map.Entry::getValue)
                                  .map(tagName -> libraryRatings.getOrDefault(tagName, null))
                                  .filter(Objects::nonNull)
                                  .findFirst()
                                  .orElse(null);
    }

    public Serie getSerie(Integer calibreBookId) {
        return calibreBooksSeries.entrySet()
                                 .stream()
                                 .filter(entry -> entry.getKey()
                                                       .equals(calibreBookId))
                                 .map(Map.Entry::getValue)
                                 .map(tagName -> librarySeries.getOrDefault(tagName, null))
                                 .filter(Objects::nonNull)
                                 .findFirst()
                                 .orElse(null);
    }
}