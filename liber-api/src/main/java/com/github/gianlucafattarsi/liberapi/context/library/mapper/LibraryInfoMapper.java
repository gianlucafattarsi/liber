package com.github.gianlucafattarsi.liberapi.context.library.mapper;

import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryInfoDTO;
import com.github.gianlucafattarsi.liberapi.context.library.service.librariesinfo.LibraryInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LibraryInfoMapper {

    LibraryInfoDTO toDTO(LibraryInfo libraryInfo);
}
