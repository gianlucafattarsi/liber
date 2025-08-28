package com.github.gianlucafattarsi.liberapi.context.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "library_series", indexes = {
        @Index(name = "tags_series_idx", columnList = "name"),
        @Index(name = "tags_sort_idx", columnList = "sort")
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Size(max = 100)
    @Column(length = 100)
    private String name;

    @NotNull
    @NotEmpty
    @Size(max = 100)
    @Column(length = 100)
    private String sort;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serie")
    @OrderBy("series_index ASC")
    private List<Book> books;
}