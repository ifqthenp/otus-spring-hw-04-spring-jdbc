package com.otus.hw_04.dao

import com.otus.hw_04.dao.impl.JdbcAuthorDao
import com.otus.hw_04.domain.Author
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.shell.Shell
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere

@JdbcTest
@Import(JdbcAuthorDao)
class AuthorDaoSpec extends Specification {

    @SpringBean
    Shell shell = Mock()

    @Subject
    @Autowired
    AuthorDao authorDao

    @Shared
    String table = 'authors'

    @Autowired
    JdbcTemplate jdbcTemplate

    void setup() {
        assert authorDao != null
        assert jdbcTemplate != null
    }

    def "can find author by id"() {
        given:
        def id = 1
        Author author = authorDao.findById(id)

        and:
        def rowsCount = countRowsInTableWhere(jdbcTemplate, table, "id = $id")

        expect:
        rowsCount == 1
        author.getId() == id
        author.getFirstName() == 'Lewis'
        author.getLastName() == 'Carrol'
    }

    def "can find author either by the first or last name, case insensitive"() {
        given:
        def herbertName = 'rBErt'
        def wellsName = 'WeL'
        def leoName = 'leo'

        and:
        Iterable<Author> herbertAuthors = authorDao.findByName("$herbertName")
        Iterable<Author> wellsAuthors = authorDao.findByName("$wellsName")
        Iterable<Author> leoAuthors = authorDao.findByName("$leoName")

        and:
        def herbertRowsCount = countRowsInTableWhere(jdbcTemplate, table,
            "LOWER(CONCAT(first_name, ' ', last_name)) LIKE LOWER('%$herbertName%')")
        def wellsRowsCount = countRowsInTableWhere(jdbcTemplate, table,
            "LOWER(CONCAT(first_name, ' ', last_name)) LIKE LOWER('%$wellsName%')")
        def leoRowsCount = countRowsInTableWhere(jdbcTemplate, table,
            "LOWER(CONCAT(first_name, ' ', last_name)) LIKE LOWER('%$leoName%')")

        expect:
        herbertAuthors == wellsAuthors
        herbertAuthors.size() == herbertRowsCount
        wellsAuthors.size() == wellsRowsCount
        leoAuthors.size() == leoRowsCount
    }

    def "returns null if author is not found by id"() {
        given:
        def id = 0
        Author author = authorDao.findById(id)

        and:
        def rowsCount = countRowsInTableWhere(jdbcTemplate, table, "id = $id")

        expect:
        author == null
        rowsCount == 0
    }

    def "can find all authors"() {
        given:
        Iterable<Author> authors = authorDao.findAll()

        and:
        def authorsRowCount = countRowsInTable(jdbcTemplate, table)

        expect:
        authors.size() == authorsRowCount
    }

    def "can save an author"() {
        given:
        def firstName = 'Friedrich'
        def lastName = 'Engels'
        Author engels = new Author(firstName, lastName)

        when:
        engels = authorDao.save(engels)

        and:
        def rowsCount = countRowsInTable(jdbcTemplate, table)

        then:
        engels != old(engels)
        engels.getId() == rowsCount
    }

    def "can save a collection of authors"() {
        given:
        Iterable<Author> authors = authorDao.findAll()

        and:
        def authorsCollection = [['Charles', 'Dickens'], ['Virginia', 'Woolf']].collect {
            it -> new Author(it[0], it[1])
        }

        when:
        authors = authorDao.save(authorsCollection)

        then:
        authors != old(authors)
        authors.size() > old(authors.size())
    }

    def "can delete an author"() {
        given:
        def id = 4
        Author author = authorDao.findById(id)
        assert author != null

        and:
        def rowsCount = countRowsInTable(jdbcTemplate, table)

        when:
        authorDao.delete(author)

        and:
        author = authorDao.findById(id)

        and:
        rowsCount = countRowsInTable(jdbcTemplate, table)

        then:
        author == null
        rowsCount == old(rowsCount) - 1
    }

    void cleanup() {
        authorDao = null
        jdbcTemplate = null
    }
}
