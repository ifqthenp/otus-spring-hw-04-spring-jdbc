package com.otus.hw_04.dao.impl;

import com.otus.hw_04.dao.AuthorDao;
import com.otus.hw_04.domain.Author;
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
public class JdbcAuthorDao implements AuthorDao {

    private static final String TABLE_NAME = "authors";
    private static final String[] TABLE_COLUMNS = {"first_name", "last_name"};
    private static final String SQL_QUERY_FIND_BY_NAME = "SELECT * FROM authors WHERE LOWER(CONCAT(first_name, ' ', last_name)) LIKE LOWER(:name)";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Author> authorRowMapper = (ResultSet rs, int rowNum) -> {
        Author author = new Author();
        author.setId(rs.getLong("id"));
        author.setFirstName(rs.getString("first_name"));
        author.setLastName(rs.getString("last_name"));
        return author;
    };

    public JdbcAuthorDao(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author save(final Author domain) {
        Author result = findById(domain.getId());
        if (result != null) {
            result.setFirstName(domain.getFirstName());
            result.setLastName(domain.getLastName());
            return upsert(result, getSqlUpdate(TABLE_NAME, TABLE_COLUMNS));
        }
        return upsert(domain, getSqlInsert(TABLE_NAME, TABLE_COLUMNS));
    }

    @Override
    public Iterable<Author> save(final Collection<Author> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(final Author domain) {
        jdbcTemplate.update(getSqlDelete(TABLE_NAME), getNamedParam("id", domain.getId()));
    }

    @Override
    public Author findById(final long id) {
        try {
            return jdbcTemplate.queryForObject(getSqlFindById(TABLE_NAME), getNamedParam("id", id), authorRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Iterable<Author> findAll() {
        return jdbcTemplate.query(getSqlFindAll(TABLE_NAME), authorRowMapper);
    }

    @Override
    public Iterable<Author> findByName(final String name) {
        final String param = getAnyMatchParam(name);
        return jdbcTemplate.query(SQL_QUERY_FIND_BY_NAME, getNamedParam("name", param), authorRowMapper);
    }

    private Author upsert(final Author author, final String sql) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
            .addValue("id", author.getId())
            .addValue("first_name", author.getFirstName())
            .addValue("last_name", author.getLastName());
        jdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        final Number key = keyHolder.getKey();
        return key == null ? findById(author.getId()) : findById(key.longValue());
    }

}
