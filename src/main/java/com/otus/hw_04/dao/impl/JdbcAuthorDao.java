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

import static com.otus.hw_04.utils.SqlCommon.getNamedParam;

@Repository
public class JdbcAuthorDao implements AuthorDao {

    private static final String SQL_INSERT = "INSERT INTO authors (first_name, last_name) VALUES (:first_name, :last_name)";
    private static final String SQL_UPDATE = "UPDATE authors SET first_name = :first_name AND last_name = :last_name WHERE id = :id";
    private static final String SQL_DELETE = "DELETE FROM authors WHERE id = :id";
    private static final String SQL_QUERY_FIND_ALL = "SELECT * FROM authors";
    private static final String SQL_QUERY_FIND_BY_ID = "SELECT * FROM authors WHERE id = :id";
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
            return upsert(result, SQL_UPDATE);
        }
        return upsert(domain, SQL_INSERT);
    }

    @Override
    public Iterable<Author> save(final Collection<Author> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(final Author domain) {
        jdbcTemplate.update(SQL_DELETE, getNamedParam("id", domain.getId()));
    }

    @Override
    public Author findById(final long id) {
        try {
            return jdbcTemplate.queryForObject(SQL_QUERY_FIND_BY_ID, getNamedParam("id", id), authorRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Iterable<Author> findAll() {
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL, authorRowMapper);
    }

    @Override
    public Iterable<Author> findByName(final String name) {
        final String param = "%" + name + "%";
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
