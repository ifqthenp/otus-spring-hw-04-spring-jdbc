package com.otus.hw_04.domain

import spock.lang.Specification

class BookSpec extends Specification {

    def "no-args constructor sets attributes to default values"() {
        given:
        Book book = new Book()

        expect:
        with(book) {
            id == 0
            title == null
            authorId == 0
            genreId == 0
            year == null
        }
    }

    def "all-args constructor sets attributes to the specified values"() {
        given:
        def bookId = 10L
        def bookTitle = 'War and Peace'
        def aId = 5L
        def gId = 1L
        def bookYear = '1869'

        and:
        Book book = new Book(bookId, bookTitle, aId, gId, bookYear)

        expect:
        with(book) {
            id == bookId
            title == bookTitle
            authorId == aId
            genreId == gId
            year == bookYear
        }
    }

    def "custom-args constructor sets the attributes to the specified values, except id"() {
        given:
        def bookTitle = 'War and Peace'
        def aId = 5L
        def gId = 1L
        def bookYear = '1869'

        and:
        Book book = new Book(bookTitle, aId, gId, bookYear)

        expect:
        with(book) {
            id == 0
            title == bookTitle
            authorId == aId
            genreId == gId
            year == bookYear
        }
    }

    def "all getters and setters work correctly"() {
        given:
        def bookId = 10L
        def bookTitle = 'War and Peace'
        def aId = 5L
        def gId = 1L
        def bookYear = '1869'

        and:
        Book book = new Book()

        when:
        book.setId(bookId)
        book.setTitle(bookTitle)
        book.setAuthorId(aId)
        book.setGenreId(gId)
        book.setYear(bookYear)

        then:
        with(book) {
            it.getId() == bookId
            it.getTitle() == bookTitle
            it.getAuthorId() == aId
            it.getGenreId() == gId
            it.getYear() == bookYear
        }
    }
}
