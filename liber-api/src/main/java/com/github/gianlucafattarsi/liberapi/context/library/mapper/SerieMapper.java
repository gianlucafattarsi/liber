package com.github.gianlucafattarsi.liberapi.context.library.mapper;

import com.github.gianlucafattarsi.liberapi.context.library.dto.BookDTO;
import com.github.gianlucafattarsi.liberapi.context.library.dto.SerieDTO;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Serie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SerieMapper {

    @Mapping(target = "books", source = "books")
    SerieDTO toDTO(Serie serie, List<BookDTO> books);
}
