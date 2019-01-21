package com.otus.hw_04.domain

import spock.lang.Specification

class GenreSpec extends Specification {

    def "no-args constructor sets attributes to default values"() {
        given:
        Genre genre = new Genre()
        assert genre != null

        expect:
        genre.getId() == 0
        genre.getGenre() == null
    }

    def "all-args constructor sets attributes to the specified ones"() {
        given:
        Genre genre = new Genre(3, "Horror")
        assert genre != null

        expect:
        genre.getId() == 3
        genre.getGenre() == 'Horror'
    }

    def "single-arg constructor sets attributes to correct values"() {
        given:
        Genre genre = new Genre("Fiction")
        assert genre != null

        expect:
        genre.getId() == 0
        genre.getGenre() == 'Fiction'

    }

    def "all getters and setters work correctly"() {
        given:
        Genre genre = new Genre(2, 'Women\'s Fiction')
        assert genre != null

        when:
        genre.setId(3)
        genre.setGenre('Fantasy')

        then:
        genre.getId() != old(genre.getId())
        genre.getGenre() != old(genre.getGenre())
    }
}
