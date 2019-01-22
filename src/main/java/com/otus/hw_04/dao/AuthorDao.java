package com.otus.hw_04.dao;

import com.otus.hw_04.domain.Author;

public interface AuthorDao extends CommonRepository<Author> {

    Author findByName(String name);

}
