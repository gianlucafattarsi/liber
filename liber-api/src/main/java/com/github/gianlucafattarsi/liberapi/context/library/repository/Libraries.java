package com.github.gianlucafattarsi.liberapi.context.library.repository;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Library;
import com.github.gianlucafattarsi.liberapi.context.library.service.librariesinfo.LibraryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Libraries extends JpaRepository<Library, Long> {

    @Query(value = """
            select new com.github.gianlucafattarsi.liberapi.context.library.service.librariesinfo.LibraryInfo(
                   book.library,
                   count(book.id),
            	   max(book.createdAt),
            	   count(distinct book.serie),
            	   count(distinct book.language))
            from Book book
            where book.library.id = ?1
            """)
    LibraryInfo findInfoByLibrary(Long id);
}