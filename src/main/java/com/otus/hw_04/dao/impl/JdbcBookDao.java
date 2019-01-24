package com.otus.hw_04.dao.impl;

import com.otus.hw_04.dao.BookDao;
import com.otus.hw_04.domain.Book;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.otus.hw_04.utils.SqlCommon.*;

@Repository
public class JdbcBookDao implements BookDao {

    private static final String TABLE_NAME = "books";
    private static final String[] TABLE_COLUMNS = {"title", "author_id", "genre_id", "written"};
    private static final String SQL_QUERY_FIND_BY_TITLE = "SELECT * FROM books WHERE LOWER(title) LIKE LOWER(:title)";
    private static final String SQL_QUERY_FIND_BY_GENRE = "SELECT books.id, books.title, books.author_id, books.genre_id, books.written FROM books INNER JOIN genres ON books.genre_id = genres.id WHERE LOWER(genre) LIKE LOWER(:genre)";
    private static final String SQL_QUERY_FIND_BY_AUTHOR = "SELECT books.id, books.title, books.author_id, books.genre_id, books.written FROM books INNER JOIN authors ON books.author_id = authors.id WHERE LOWER(CONCAT(first_name, ' ', last_name)) LIKE LOWER(:name)";
    private static final String SQL_QUERY_FIND_ALL_WITH_DETAILS = "SELECT books.id, books.title, CONCAT(authors.first_name, ' ', authors.last_name) AS author, genres.genre, books.written FROM books INNER JOIN authors INNER JOIN genres ON books.author_id = authors.id AND books.genre_id = genres.id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Book> bookRowMapper = (ResultSet rs, int rowNum) -> {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthorId(rs.getLong("author_id"));
        book.setGenreId(rs.getLong("genre_id"));
        book.setYear(rs.getString("written"));
        return book;
    };

    public JdbcBookDao(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Book> findByTitle(final String title) {
        final String param = getAnyMatchParam(title);
        return jdbcTemplate.query(SQL_QUERY_FIND_BY_TITLE, getNamedParam("title", param), bookRowMapper);
    }

    @Override
    public Iterable<Book> findByAuthor(final String author) {
        final String param = getAnyMatchParam(author);
        return jdbcTemplate.query(SQL_QUERY_FIND_BY_AUTHOR, getNamedParam("name", param), bookRowMapper);
    }

    @Override
    public Iterable<Book> findByGenre(final String genre) {
        final String param = getAnyMatchParam(genre);
        return jdbcTemplate.query(SQL_QUERY_FIND_BY_GENRE, getNamedParam("genre", param), bookRowMapper);
    }

    @Override
    public List<Map<String, Object>> findAllWithDetails() {
        return jdbcTemplate.queryForList(SQL_QUERY_FIND_ALL_WITH_DETAILS, new HashMap<>());
    }

    @Override
    public Book save(final Book domain) {
        Book result = findById(domain.getId());
        if (result != null) {
            result.setTitle(domain.getTitle());
            result.setAuthorId(domain.getAuthorId());
            result.setGenreId(domain.getGenreId());
            result.setYear(domain.getYear());
            return upsert(result, getSqlUpdate(TABLE_NAME, TABLE_COLUMNS));
        }
        return upsert(domain, getSqlInsert(TABLE_NAME, TABLE_COLUMNS));
    }

    @Override
    public Iterable<Book> save(final Collection<Book> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(final Book domain) {
        jdbcTemplate.update(getSqlDelete(TABLE_NAME), getNamedParam("id", domain.getId()));
    }

    @Override
    public Book findById(final long id) {
        try {
            return jdbcTemplate.queryForObject(getSqlFindById(TABLE_NAME), getNamedParam("id", id), bookRowMapper);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Iterable<Book> findAll() {
        return jdbcTemplate.query(getSqlFindAll(TABLE_NAME), bookRowMapper);
    }

    private Book upsert(final Book book, final String sql) {
        final long bookId = book.getId();
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
            .addValue("id", bookId)
            .addValue("title", book.getTitle())
            .addValue("author_id", book.getAuthorId())
            .addValue("genre_id", book.getGenreId())
            .addValue("written", book.getYear());
        jdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        final Number key = keyHolder.getKey();
        return key == null ? findById(bookId) : findById(key.longValue());
    }

}
