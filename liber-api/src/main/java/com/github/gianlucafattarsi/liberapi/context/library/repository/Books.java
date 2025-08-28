package com.github.gianlucafattarsi.liberapi.context.library.repository;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Books extends JpaRepository<Book, Long> {

    @Query("""
            select b from Book b order by RAND() limit 1
            """)
    Book findRandom();
}