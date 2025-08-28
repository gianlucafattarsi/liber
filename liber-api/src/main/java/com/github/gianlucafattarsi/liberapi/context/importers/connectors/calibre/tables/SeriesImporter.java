package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Serie;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Series;
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
public class SeriesImporter extends AbstractTableImporter<Serie, String> {

    private final Series series;

//    @Override
//    public void importData(final Connection connection, LibraryDTO library) {
//        log.debug("SERIES: Importing data...");
//        final Map<String, Serie> existingSeries = series.findAll()
//                                                        .stream()
//                                                        .collect(Collectors.toMap(Serie::getName, Function.identity()));
//
//        try {
//            final ResultSet resultSet = connection.prepareStatement(getSelectStatement())
//                                                  .executeQuery();
//
//            while (resultSet.next()) {
//                String newSerieName = resultSet.getString("name");
//                String newSerieSort = resultSet.getString("sort");
//
//                if (!existingSeries.containsKey(newSerieName)) {
//                    final Serie newSerie = Serie.builder()
//                                                .name(newSerieName)
//                                                .sort(newSerieSort)
//                                                .build();
//                    series.save(newSerie);
//                    existingSeries.put(newSerieName, newSerie);
//                }
//            }
//
//            log.debug("SERIES: Table imported successfully.");
//        } catch (Exception e) {
//            log.error("SERIES:  Failed to import data.", e);
//        }
//    }

    @Override
    protected Map<String, Serie> getExistingEntitiesBySearchKey() {
        return series.findAll()
                     .stream()
                     .collect(Collectors.toMap(Serie::getName, Function.identity()));
    }

    @Override
    protected Optional<Serie> buildEntity(final ResultSet resultSet,
                                          final Map<String, Serie> existingEntities) throws Exception {
        String newSerieName = resultSet.getString("name");
        String newSerieSort = resultSet.getString("sort");

        Serie serie = null;
        if (!existingEntities.containsKey(newSerieName)) {
            serie = Serie.builder()
                         .name(newSerieName)
                         .sort(newSerieSort)
                         .build();
            existingEntities.put(newSerieName, serie);
        }

        return Optional.ofNullable(serie);
    }

    @Override
    protected String getSelectStatement() {
        return "select name,sort from series";
    }

    @Override
    protected JpaRepository<Serie, Long> getRepository() {
        return series;
    }

    @Override
    public String getManagedTable() {
        return "Series";
    }
}
