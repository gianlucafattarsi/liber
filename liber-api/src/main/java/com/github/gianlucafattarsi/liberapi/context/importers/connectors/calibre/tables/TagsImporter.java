package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Tag;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Tags;
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
public class TagsImporter extends AbstractTableImporter<Tag, String> {

    private final Tags tags;

    @Override
    public String getManagedTable() {
        return "Tags";
    }

    @Override
    protected Map<String, Tag> getExistingEntitiesBySearchKey() {
        return getRepository().findAll()
                              .stream()
                              .collect(Collectors.toMap(Tag::getName, Function.identity()));
    }

//    @Override
//    public void importData(final Connection connection, LibraryDTO library) {
//        log.debug("TAGS: Importing data...");
//
//        final Map<String, Tag> existingTags = tags.findAll()
//                                                  .stream()
//                                                  .collect(Collectors.toMap(Tag::getName, Function.identity()));
//
//        try {
//            final ResultSet resultSet = connection.prepareStatement(getSelectStatement())
//                                                  .executeQuery();
//
//            while (resultSet.next()) {
//                String newTagsName = resultSet.getString("name");
//
//                if (!existingTags.containsKey(newTagsName)) {
//                    final Tag newTag = Tag.builder()
//                                          .name(newTagsName)
//                                          .build();
//                    tags.save(newTag);
//                    existingTags.put(newTagsName, newTag);
//                }
//            }
//
//            log.debug("TAGS: Table imported successfully.");
//        } catch (Exception e) {
//            log.error("TAGS:  Failed to import data.", e);
//        }
//    }

    @Override
    protected Optional<Tag> buildEntity(final ResultSet resultSet, Map<String, Tag> existingEntities) throws Exception {
        String newTagsName = resultSet.getString("name");

        Tag tag = null;
        if (!existingEntities.containsKey(newTagsName)) {
            tag = Tag.builder()
                     .name(newTagsName)
                     .build();
            existingEntities.put(newTagsName, tag);
        }

        return Optional.ofNullable(tag);
    }

    @Override
    protected String getSelectStatement() {
        return "select name from tags";
    }

    @Override
    protected JpaRepository<Tag, Long> getRepository() {
        return tags;
    }
}
