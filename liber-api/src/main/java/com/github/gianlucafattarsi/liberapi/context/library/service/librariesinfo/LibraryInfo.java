package com.github.gianlucafattarsi.liberapi.context.library.service.librariesinfo;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Library;

import java.time.Instant;

public record LibraryInfo(

        Library library,

        long booksCount,

        Instant lastBookAddedAt,

        long seriesCount,

        long languagesCount
) {
}
