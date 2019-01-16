package com.otus.hw_04.domain;

import java.time.LocalDate;

public class Book {

    private long id;
    private Author author;
    private Genre genre;
    private LocalDate publishingDate;

    public Book(final long id, final Author author, final Genre genre, final LocalDate publishingDate) {
        this.id = id;
        this.author = author;
        this.genre = genre;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(final Author author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(final Genre genre) {
        this.genre = genre;
    }

    public LocalDate getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(final LocalDate publishingDate) {
        this.publishingDate = publishingDate;
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", author=" + author +
            ", genre=" + genre +
            ", publishingDate=" + publishingDate +
            '}';
    }

}
