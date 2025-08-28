package com.github.gianlucafattarsi.liberapi.context.library.service;

import com.github.gianlucafattarsi.liberapi.application.exception.type.NoSuchEntityException;
import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryDTO;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Library;
import com.github.gianlucafattarsi.liberapi.context.library.mapper.LibraryMapper;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Libraries;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LibraryService {

    private final Libraries libraries;

    private final LibraryMapper mapper;

    public LibraryDTO loadLibrary(long id) {

        final Library library = libraries.findById(id)
                                         .orElseThrow(NoSuchEntityException::new);

        return mapper.toDTO(library);
    }

    public LibraryDTO createLibrary(LibraryDTO libraryDTO) {

        final Library library = mapper.toEntity(libraryDTO);
        library.clearId();

        final Library savedLibrary = libraries.save(library);

        return mapper.toDTO(savedLibrary);
    }

    public List<LibraryDTO> loadLibraries() {
        return libraries.findAll()
                        .stream()
                        .map(mapper::toDTO)
                        .toList();
    }
}
