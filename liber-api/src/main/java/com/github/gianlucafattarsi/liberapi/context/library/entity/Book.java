package com.github.gianlucafattarsi.liberapi.context.library.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "library_books", indexes = {
        @Index(name = "books_title_idx", columnList = "title"),
        @Index(name = "books_sort_idx", columnList = "sort")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdAt;

    @NotNull
    @NotEmpty
    @Size(max = 200)
    @Column(length = 200)
    private String title;

    @Size(max = 15_000)
    @Column(length = 15_000)
    private String description;

    @NotNull
    @NotEmpty
    @Size(max = 200)
    @Column(length = 200)
    private String sort;

    @Builder.Default
    @NotNull
    @Column(name = "series_index")
    private double seriesIndex = -1;

    // Denormalized field for search optimization
    // The length is set to 400 because 1 book can have multiple authors
    @NotNull
    @NotEmpty
    @Size(max = 400)
    @Column(length = 400)
    private String authorSort;

    /**
     * Path of the book files
     */
    @NotNull
    @NotEmpty
    @Size(max = 500)
    @Column(length = 500)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", foreignKey = @ForeignKey(name = "fk_books_library_id"))
    private Library library;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "library_books_authors",
            joinColumns = @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "fk_books_authors_book_id")),
            inverseJoinColumns = @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_books_authors_author_id")))
    private Set<Author> authors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "library_books_publishers",
            joinColumns = @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "fk_books_publishers_book_id")),
            inverseJoinColumns = @JoinColumn(name = "publisher_id", foreignKey = @ForeignKey(name = "fk_books_publishers_publisher_id")))
    private Set<Publisher> publishers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", foreignKey = @ForeignKey(name = "fk_books_language_id"))
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rating_id", foreignKey = @ForeignKey(name = "fk_books_rating_id"))
    private Rating rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serie_id", foreignKey = @ForeignKey(name = "fk_books_serie_id"))
    private Serie serie;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "library_books_tags",
            joinColumns = @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "fk_books_tags_book_id")),
            inverseJoinColumns = @JoinColumn(name = "tag_id", foreignKey = @ForeignKey(name = "fk_books_tags_tag_id")))
    private Set<Tag> tags;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book", cascade = CascadeType.ALL)
    private Set<BookData> data;

    public void addData(@NotNull Collection<BookData> dataToAdd) {
        if (data == null) {
            data = new HashSet<>();
        }

        dataToAdd.forEach(d -> {
            d.setBook(this);
            data.add(d);
        });
    }
}