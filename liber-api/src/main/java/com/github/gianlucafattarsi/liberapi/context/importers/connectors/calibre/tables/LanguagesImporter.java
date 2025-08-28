package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Language;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Languages;
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
public class LanguagesImporter extends AbstractTableImporter<Language, String> {


    private final Languages languages;

//    @Override
//    public void importData(final Connection connection, LibraryDTO library) {
//        log.debug("LANGUAGES: Importing data...");
//        final Map<String, Language> existingLanguages = languages.findAll()
//                                                                 .stream()
//                                                                 .collect(Collectors.toMap(Language::getLangCode,
//                                                                         Function.identity()));
//
//        try {
//            final ResultSet resultSet = connection.prepareStatement(getSelectStatement())
//                                                  .executeQuery();
//
//            while (resultSet.next()) {
//                String newLanguageCode = resultSet.getString("lang_code");
//
//                if (!existingLanguages.containsKey(newLanguageCode)) {
//                    final Language newLanguage = Language.builder()
//                                                         .langCode(newLanguageCode)
//                                                         .build();
//                    languages.save(newLanguage);
//                    existingLanguages.put(newLanguageCode, newLanguage);
//                }
//            }
//
//            log.debug("LANGUAGES: Table imported successfully.");
//        } catch (Exception e) {
//            log.error("LANGUAGES:  Failed to import data.", e);
//        }
//    }

    @Override
    protected Map<String, Language> getExistingEntitiesBySearchKey() {
        return languages.findAll()
                        .stream()
                        .collect(Collectors.toMap(Language::getLangCode,
                                Function.identity()));
    }

    @Override
    protected Optional<Language> buildEntity(final ResultSet resultSet,
                                             final Map<String, Language> existingEntities) throws Exception {
        String newLanguageCode = resultSet.getString("lang_code");

        Language language = null;
        if (!existingEntities.containsKey(newLanguageCode)) {
            language = Language.builder()
                               .langCode(newLanguageCode)
                               .build();
            existingEntities.put(newLanguageCode, language);
        }

        return Optional.ofNullable(language);
    }

    @Override
    protected String getSelectStatement() {
        return "select lang_code from languages";
    }

    @Override
    protected JpaRepository<Language, Long> getRepository() {
        return languages;
    }

    @Override
    public String getManagedTable() {
        return "Languages";
    }
}
