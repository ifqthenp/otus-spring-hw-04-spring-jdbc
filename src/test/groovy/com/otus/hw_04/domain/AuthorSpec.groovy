package com.otus.hw_04.domain

import spock.lang.Specification

class AuthorSpec extends Specification {

    def "no-args constructor sets attributes to default values"() {
        given:
        Author author = new Author()
        assert author != null

        expect:
        with(author) {
            id == 0
            firstName == null
            lastName == null
        }
    }

    def "all-args constructor sets attributes to the specified ones"() {
        given:
        Author author = new Author(3, 'John', 'Smith')
        assert author != null

        expect:
        with(author) {
            id == 3
            firstName == 'John'
            lastName == 'Smith'
        }
    }

    def "all getters and setters work correctly"() {
        given:
        Author author = new Author(1, 'Johnny', 'Foobar')
        assert author != null

        when:
        author.setId(5)
        author.setFirstName('John')
        author.setLastName('Smith')

        then:
        author.getId() != old(author.getId())
        author.getFirstName() != old(author.getFirstName())
        author.getLastName() != old(author.getLastName())
    }

}
