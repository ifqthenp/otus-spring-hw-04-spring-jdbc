package com.otus.hw_04.domain;

public class Book {

    private long id;
    private String title;
    private long authorId;
    private long genreId;
    private String written;

    public Book(final long id, final String title, final long authorId, final long genreId, final String written) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
        this.written = written;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(final long authorId) {
        this.authorId = authorId;
    }

    public long getGenreId() {
        return genreId;
    }

    public void setGenreId(final long genreId) {
        this.genreId = genreId;
    }

    public String getWritten() {
        return written;
    }

    public void setWritten(final String written) {
        this.written = written;
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", authorId=" + authorId +
            ", genreId=" + genreId +
            ", written='" + written + '\'' +
            '}';
    }

}
