package com.otus.hw_04.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private long id;
    private String title;
    private long authorId;
    private long genreId;
    private String year;

    public Book(final String title, final long authorId, final long genreId, final String year) {
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
        this.year = year;
    }

}
