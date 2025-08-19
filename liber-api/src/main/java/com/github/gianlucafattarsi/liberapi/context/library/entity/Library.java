package com.github.gianlucafattarsi.liberapi.context.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "libraries", indexes = {
        @Index(name = "libraries_name_idx", columnList = "name", unique = true),
        @Index(name = "libraries_path_idx", columnList = "path", unique = true)
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Library {

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
    @Size(max = 500)
    @Column(length = 500)
    private String path;

    public void clearId() {
        this.id = null;
    }
}