package com.otus.hw_04.dao.impl;

import com.otus.hw_04.dao.GenreDao;
import com.otus.hw_04.domain.Genre;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Repository
public class JdbcGenreDao implements GenreDao {

    private static final String SQL_INSERT = "INSERT INTO genres (genre) VALUES (:genre)";
    private static final String SQL_UPDATE = "UPDATE genres SET genre = :genre WHERE id = :id";
    private static final String SQL_DELETE = "DELETE FROM genres WHERE id = :id";
    private static final String SQL_QUERY_FIND_ALL = "SELECT id, genre FROM genres";
    private static final String SQL_QUERY_FIND_BY_ID = "SELECT * FROM genres WHERE id = :id";
    private static final String SQL_QUERY_FIND_BY_NAME = "SELECT * FROM genres WHERE LOWER(genre) LIKE LOWER(:genre)";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcGenreDao(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre save(final Genre domain) {
        Genre result = findById(domain.getId());
        if (result != null) {
            result.setGenre(domain.getGenre());
            return upsert(result, SQL_UPDATE);
        }
        return upsert(domain, SQL_INSERT);
    }

    @Override
    public Iterable<Genre> save(final Collection<Genre> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(final Genre domain) {
        Map<String, Object> namedParameters = Collections.singletonMap("id", domain.getId());
        jdbcTemplate.update(SQL_DELETE, namedParameters);
    }

    @Override
    public Genre findById(final long id) {
        try {
            Map<String, Object> namedParameters = Collections.singletonMap("id", id);
            return jdbcTemplate.queryForObject(SQL_QUERY_FIND_BY_ID, namedParameters, genreRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Genre findByGenre(final String genre) {
        try {
            Map<String, Object> namedParameters = Collections.singletonMap("genre", "%" + genre + "%");
            return jdbcTemplate.queryForObject(SQL_QUERY_FIND_BY_NAME, namedParameters, genreRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Iterable<Genre> findAll() {
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL, genreRowMapper);
    }

    private Genre upsert(final Genre genre, final String sql) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
            .addValue("id", genre.getId())
            .addValue("genre", genre.getGenre());
        jdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        final Number key = keyHolder.getKey();
        return key == null ? findById(genre.getId()) : findById(key.longValue());
    }

    private RowMapper<Genre> genreRowMapper = (ResultSet rs, int rowNum) -> {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setGenre(rs.getString("genre"));
        return genre;
    };

}