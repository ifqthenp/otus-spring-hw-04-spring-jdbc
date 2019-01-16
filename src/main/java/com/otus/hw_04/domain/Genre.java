package com.otus.hw_04.domain;

public class Genre {

    private long id;
    private String genre;

    public Genre(final long id, final String genre) {
        this.genre = genre;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Genre{" +
            "id=" + id +
            ", genre='" + genre + '\'' +
            '}';
    }

}
