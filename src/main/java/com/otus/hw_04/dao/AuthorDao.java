package com.otus.hw_04.dao;

import com.otus.hw_04.domain.Author;

public interface AuthorDao extends CommonRepository<Author> {

    Iterable<Author> findByName(String name);

}
