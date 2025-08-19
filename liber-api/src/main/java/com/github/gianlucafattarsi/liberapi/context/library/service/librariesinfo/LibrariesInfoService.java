package com.github.gianlucafattarsi.liberapi.context.library.service.librariesinfo;

import com.github.gianlucafattarsi.liberapi.application.exception.type.NoSuchEntityException;
import com.github.gianlucafattarsi.liberapi.context.library.dto.LibrariesInfoDTO;
import com.github.gianlucafattarsi.liberapi.context.library.dto.LibraryInfoDTO;
import com.github.gianlucafattarsi.liberapi.context.library.mapper.LibraryInfoMapper;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Authors;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Books;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Languages;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Libraries;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Publishers;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Series;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LibrariesInfoService {

    private final Libraries libraries;
    private final Books books;
    private final Series series;
    private final Publishers publishers;
    private final Authors authors;
    private final Tags tags;
    private final Languages languages;

    private final LibraryInfoMapper libraryInfoMapper;

    public LibraryInfoDTO loadLibraryInfo(long id) {

        libraries.findById(id)
                 .orElseThrow(NoSuchEntityException::new);

        final LibraryInfo infoByLibrary = libraries.findInfoByLibrary(id);

        return libraryInfoMapper.toDTO(infoByLibrary);
    }

    public LibrariesInfoDTO loadLibrariesInfo() {

        return new LibrariesInfoDTO(books.count(),
                series.count(),
                publishers.count(),
                authors.count(),
                tags.count(),
                languages.count());
    }
}
