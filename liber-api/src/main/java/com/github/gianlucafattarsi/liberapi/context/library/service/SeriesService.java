package com.github.gianlucafattarsi.liberapi.context.library.service;

import com.github.gianlucafattarsi.liberapi.context.library.dto.BookDTO;
import com.github.gianlucafattarsi.liberapi.context.library.dto.SerieDTO;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Book;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Serie;
import com.github.gianlucafattarsi.liberapi.context.library.mapper.BookMapper;
import com.github.gianlucafattarsi.liberapi.context.library.mapper.SerieMapper;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Series;
import com.github.gianlucafattarsi.liberapi.context.library.utils.CoverUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SeriesService {

    private final Series series;

    private final SerieMapper serieMapper;
    private final BookMapper bookMapper;

    public SerieDTO loadRandomSerie() {

        final Serie serie = series.findRandom();

        if (serie == null) {
            return null;
        }

        List<BookDTO> booksDTO;
        final List<Book> books = serie.getBooks();
        if (books
                .size() > 10) {
            booksDTO = books
                    .stream()
                    .map(bookMapper::toDTOWithoutCover)
                    .toList();
        } else {
            booksDTO = books
                    .stream()
                    .map(book -> bookMapper.toDTO(book, CoverUtils.fetchThumbnail(book)))
                    .toList();
        }

        return serieMapper.toDTO(serie, booksDTO);
    }
}
