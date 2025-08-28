package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Rating;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Ratings;
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
public class RatingsImporter extends AbstractTableImporter<Rating, Integer> {

    private final Ratings ratings;

//    @Override
//    public void importData(final Connection connection, LibraryDTO library) {
//        log.debug("RATINGS: Importing data...");
//        final Map<Integer, Rating> existingRatings = ratings.findAll()
//                                                            .stream()
//                                                            .collect(Collectors.toMap(Rating::getRating,
//                                                                    Function.identity()));
//
//        try {
//            final ResultSet resultSet = connection.prepareStatement(getSelectStatement())
//                                                  .executeQuery();
//
//            while (resultSet.next()) {
//                Integer newRatingRating = resultSet.getInt("rating");
//
//                if (!existingRatings.containsKey(newRatingRating)) {
//                    final Rating newRating = Rating.builder()
//                                                   .rating(newRatingRating)
//                                                   .build();
//                    ratings.save(newRating);
//                    existingRatings.put(newRatingRating, newRating);
//                }
//            }
//
//            log.debug("RATINGS: Table imported successfully.");
//        } catch (Exception e) {
//            log.error("RATINGS:  Failed to import data.", e);
//        }
//    }

    @Override
    protected Map<Integer, Rating> getExistingEntitiesBySearchKey() {
        return ratings.findAll()
                      .stream()
                      .collect(Collectors.toMap(Rating::getRating,
                              Function.identity()));
    }

    @Override
    protected Optional<Rating> buildEntity(final ResultSet resultSet,
                                           final Map<Integer, Rating> existingEntities) throws Exception {
        Integer newRatingRating = resultSet.getInt("rating");

        Rating rating = null;
        if (!existingEntities.containsKey(newRatingRating)) {
            rating = Rating.builder()
                           .rating(newRatingRating)
                           .build();
            existingEntities.put(newRatingRating, rating);
        }

        return Optional.ofNullable(rating);
    }

    @Override
    protected String getSelectStatement() {
        return "select rating from ratings";
    }

    @Override
    protected JpaRepository<Rating, Long> getRepository() {
        return ratings;
    }

    @Override
    public String getManagedTable() {
        return "Ratings";
    }
}
