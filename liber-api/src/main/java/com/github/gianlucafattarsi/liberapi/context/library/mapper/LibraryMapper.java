package com.github.gianlucafattarsi.liberapi.context.library.mapper;

import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryDTO;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Library;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LibraryMapper {

    LibraryDTO toDTO(Library library);

    Library toEntity(LibraryDTO libraryDTO);
}
