package com.otus.hw_04.dao;

import com.otus.hw_04.domain.Genre;

public interface GenreDao extends CommonRepository<Genre> {

    Genre findByGenre(String genre);

}
