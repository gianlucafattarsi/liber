package com.github.gianlucafattarsi.liberapi.context.library.controller.impl;

import com.github.gianlucafattarsi.liberapi.context.library.controller.BooksApi;
import com.github.gianlucafattarsi.liberapi.context.library.dto.BookDTO;
import com.github.gianlucafattarsi.liberapi.context.library.service.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BooksController implements BooksApi {

    private final BooksService booksService;

    @Override
    public BookDTO loadRandomBook() {
        return booksService.loadRandomBook();
    }
}
