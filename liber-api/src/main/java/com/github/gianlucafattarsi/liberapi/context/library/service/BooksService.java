package com.github.gianlucafattarsi.liberapi.context.library.service;

import com.github.gianlucafattarsi.liberapi.application.exception.type.NoSuchEntityException;
import com.github.gianlucafattarsi.liberapi.context.library.dto.BookDTO;
import com.github.gianlucafattarsi.liberapi.context.library.entity.Book;
import com.github.gianlucafattarsi.liberapi.context.library.mapper.BookMapper;
import com.github.gianlucafattarsi.liberapi.context.library.repository.Books;
import com.github.gianlucafattarsi.liberapi.context.library.utils.CoverUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BooksService {

    private final Books books;

    private final BookMapper mapper;

    public BookDTO loadRandomBook() {

        final Book book = books.findRandom();

        return mapper.toDTO(book, CoverUtils.fetchThumbnail(book));
    }

    public BookDTO loadBook(long id) {

        final Book book = books.findById(id)
                               .orElseThrow(NoSuchEntityException::new);

        return null;
    }
}
