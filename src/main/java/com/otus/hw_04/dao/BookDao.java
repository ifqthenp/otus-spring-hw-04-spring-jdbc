package com.otus.hw_04.dao;

import com.otus.hw_04.domain.Book;

import java.util.List;
import java.util.Map;

public interface BookDao extends CommonRepository<Book> {

    Iterable<Book> findByTitle(String title);

    Iterable<Book> findByAuthor(String author);

    Iterable<Book> findByGenre(String genre);

    List<Map<String, Object>> findAllWithDetails();

}
