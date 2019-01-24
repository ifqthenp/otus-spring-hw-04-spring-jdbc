package com.otus.hw_04.dao.impl;

import com.otus.hw_04.dao.GenreDao;
import com.otus.hw_04.domain.Genre;
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

import static com.otus.hw_04.utils.SqlCommon.*;

@Repository
public class JdbcGenreDao implements GenreDao {

    private static final String TABLE_NAME = "genres";
    private static final String[] TABLE_COLUMNS = {"genre"};
    private static final String SQL_QUERY_FIND_BY_NAME = "SELECT * FROM genres WHERE LOWER(genre) LIKE LOWER(:genre)";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private RowMapper<Genre> genreRowMapper = (ResultSet rs, int rowNum) -> {
        Genre genre = new Genre();
        genre.setId(rs.getLong("id"));
        genre.setGenre(rs.getString("genre"));
        return genre;
    };

    public JdbcGenreDao(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre save(final Genre domain) {
        Genre result = findById(domain.getId());
        if (result != null) {
            result.setGenre(domain.getGenre());
            return upsert(result, getSqlUpdate(TABLE_NAME, TABLE_COLUMNS));
        }
        return upsert(domain, getSqlInsert(TABLE_NAME, TABLE_COLUMNS));
    }

    @Override
    public Iterable<Genre> save(final Collection<Genre> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(final Genre domain) {
        jdbcTemplate.update(getSqlDelete(TABLE_NAME), getNamedParam("id", domain.getId()));
    }

    @Override
    public Genre findById(final long id) {
        try {
            return jdbcTemplate.queryForObject(getSqlFindById(TABLE_NAME), getNamedParam("id", id), genreRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Genre findByGenre(final String genre) {
        try {
            final String param = getAnyMatchParam(genre);
            return jdbcTemplate.queryForObject(SQL_QUERY_FIND_BY_NAME, getNamedParam("genre", param), genreRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Iterable<Genre> findAll() {
        return jdbcTemplate.query(getSqlFindAll(TABLE_NAME), genreRowMapper);
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

}
