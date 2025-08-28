package com.github.gianlucafattarsi.liberapi.context.importers.connectors.calibre.tables.books;

import java.util.List;

public class BooksStatements {

    public static final String COUNT_STATEMENT = "select count(id) from books";
    public static final String SELECT_STATEMENT = """
            select distinct b.id,b.title,b.sort,b.series_index,b.author_sort,b.path,max(c."text") as description
            from books b left join comments c on b.id = c.book
            group by b.id
            """;
    public static final String BOOKS_DATA_STATEMENT = """
            select book, format, uncompressed_size, name
            from data
            """;
    public static final String BOOKS_TAGS_STATEMENT = """
            select btl.book,
            	   t.name
            from books_tags_link btl inner join tags t on btl.tag = t.id
            """;
    public static final String BOOKS_AUTHORS_STATEMENT = """
            select bal.book,
            	   a.name
            from books_authors_link bal inner join authors a  on bal.author = a.id
            """;
    public static final String BOOKS_PUBLSHERS_TATEMENTS = """
            select bpl.book,
            	   p.name
            from books_publishers_link bpl inner join publishers p on bpl.publisher = p.id
            """;
    public static final String BOOKS_LANGUAGE_STATEMENT = """
            select bll.book,
            	   l.lang_code
            from books_languages_link bll inner join languages l on bll.lang_code = l.id
            """;
    public static final String BOOKS_RATINGS_STATEMENT = """
            select brl.book,
            	   r.rating
            from books_ratings_link brl inner join ratings r on brl.rating = r.id
            """;
    public static final String BOOKS_SERIES_STATEMENT = """
            select bsl.book,
            	   s.name
            from books_series_link bsl inner join series s on bsl.series = s.id
            """;

    public static List<String> getCheckStatements() {
        return List.of(SELECT_STATEMENT,
                BOOKS_TAGS_STATEMENT,
                BOOKS_AUTHORS_STATEMENT,
                BOOKS_PUBLSHERS_TATEMENTS,
                BOOKS_LANGUAGE_STATEMENT,
                BOOKS_RATINGS_STATEMENT,
                BOOKS_SERIES_STATEMENT);
    }
}
