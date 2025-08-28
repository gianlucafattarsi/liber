package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Author;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Authors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthorsImporter extends AbstractTableImporter<Author, String> {

    @Autowired
    private Authors authors;

//    @Override
//    public void importData(final Connection connection, LibraryDTO library) {
//        log.debug("AUTHORS: Importing data...");
//        final Map<String, Author> existingAuthors = authors.findAll()
//                                                           .stream()
//                                                           .collect(Collectors.toMap(Author::getName,
//                                                                   Function.identity()));
//
//        try {
//            final ResultSet resultSet = connection.prepareStatement(getSelectStatement())
//                                                  .executeQuery();
//
//            while (resultSet.next()) {
//                String newAuthorName = resultSet.getString("name");
//                String newAuthorSort = resultSet.getString("sort");
//
//                if (!existingAuthors.containsKey(newAuthorName)) {
//                    final Author newAuthor = Author.builder()
//                                                   .name(newAuthorName)
//                                                   .sort(newAuthorSort)
//                                                   .build();
//                    authors.save(newAuthor);
//                    existingAuthors.put(newAuthorName, newAuthor);
//                }
//            }
//
//            log.debug("AUTHORS: Table imported successfully.");
//        } catch (Exception e) {
//            log.error("AUTHORS:  Failed to import data.", e);
//        }
//    }

    @Override
    protected Map<String, Author> getExistingEntitiesBySearchKey() {
        return authors.findAll()
                      .stream()
                      .collect(Collectors.toMap(Author::getName,
                              Function.identity()));
    }

    @Override
    protected Optional<Author> buildEntity(final ResultSet resultSet,
                                           final Map<String, Author> existingEntities) throws Exception {
        String newAuthorName = resultSet.getString("name");
        String newAuthorSort = resultSet.getString("sort");

        Author author = null;
        if (!existingEntities.containsKey(newAuthorName)) {
            author = Author.builder()
                           .name(newAuthorName)
                           .sort(newAuthorSort)
                           .build();
            existingEntities.put(newAuthorName, author);
        }

        return Optional.ofNullable(author);
    }

    @Override
    protected String getSelectStatement() {
        return "select name,sort from authors";
    }

    @Override
    protected JpaRepository<Author, Long> getRepository() {
        return authors;
    }

    @Override
    public String getManagedTable() {
        return "Authors";
    }
}
