package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Publisher;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Publishers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class PublishersImporter extends AbstractTableImporter<Publisher, String> {

    private final Publishers publishers;

//    @Override
//    public void importData(final Connection connection, LibraryDTO library) {
//        log.debug("PUBLISHERS: Importing data...");
//        final Map<String, Publisher> existingPublishers = publishers.findAll()
//                                                                    .stream()
//                                                                    .collect(Collectors.toMap(Publisher::getName,
//                                                                            Function.identity()));
//
//        try {
//            final ResultSet resultSet = connection.prepareStatement(getSelectStatement())
//                                                  .executeQuery();
//
//            while (resultSet.next()) {
//                String newPublishersName = resultSet.getString("name");
//
//                if (!existingPublishers.containsKey(newPublishersName)) {
//                    final Publisher newPublisher = Publisher.builder()
//                                                            .name(newPublishersName)
//                                                            .build();
//                    publishers.save(newPublisher);
//                    existingPublishers.put(newPublishersName, newPublisher);
//                }
//            }
//
//            log.debug("PUBLISHERS: Table imported successfully.");
//        } catch (Exception e) {
//            log.error("PUBLISHERS:  Failed to import data.", e);
//        }
//    }

    @Override
    protected Map<String, Publisher> getExistingEntitiesBySearchKey() {
        return publishers.findAll()
                         .stream()
                         .collect(Collectors.toMap(Publisher::getName,
                                 Function.identity()));
    }

    @Override
    protected Optional<Publisher> buildEntity(final ResultSet resultSet,
                                              final Map<String, Publisher> existingEntities) throws Exception {
        String newPublishersName = resultSet.getString("name");

        Publisher publisher = null;
        if (!existingEntities.containsKey(newPublishersName)) {
            publisher = Publisher.builder()
                                 .name(newPublishersName)
                                 .build();
            existingEntities.put(newPublishersName, publisher);
        }

        return Optional.ofNullable(publisher);
    }

    @Override
    protected String getSelectStatement() {
        return "select name from publishers";
    }

    @Override
    protected JpaRepository<Publisher, Long> getRepository() {
        return publishers;
    }

    @Override
    public String getManagedTable() {
        return "Publishers";
    }
}
