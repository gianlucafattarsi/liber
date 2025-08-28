package com.github.gianlucafattarsi.liberapi.context.library.mapper;

import com.github.gianlucafattarsi.liberapi.context.library.dto.BookDTO;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Book;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Language;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Named("languageToLangCode")
    static String languageToLangCode(Language language) {
        return language == null ? "" : language.getLangCode();
    }

    @Mapping(source = "book.language", target = "language", qualifiedByName = "languageToLangCode")
    @Mapping(source = "coverBase64", target = "cover")
    @Mapping(target = "serie.books", ignore = true)
    BookDTO toDTO(Book book, String coverBase64);

    @Mapping(source = "book.language", target = "language", qualifiedByName = "languageToLangCode")
    @Mapping(target = "cover", ignore = true)
    @Mapping(target = "serie.books", ignore = true)
    BookDTO toDTOWithoutCover(Book book);
}
