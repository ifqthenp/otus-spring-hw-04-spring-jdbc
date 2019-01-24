package com.otus.hw_04.dao

import com.otus.hw_04.dao.impl.JdbcBookDao
import com.otus.hw_04.domain.Book
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
@Import(JdbcBookDao)
class BookDaoSpec extends Specification {

    @SpringBean
    Shell shell = Mock()

    @Subject
    @Autowired
    BookDao bookDao

    @Shared
    String table = 'books'

    @Autowired
    JdbcTemplate jdbcTemplate

    void setup() {
        assert bookDao != null
        assert jdbcTemplate != null
    }

    def "can find book by id"() {
        given:
        def bookId = 2

        and:
        Book book = bookDao.findById(bookId)
        assert book != null

        and:
        def rowsCount = countRowsInTableWhere(jdbcTemplate, table, "id = $bookId")

        expect:
        rowsCount == 1
        with(book) {
            id == bookId
            title == 'Jane Eyre'
            authorId == 2
            genreId == 3
            year == '1847'
        }
    }

    def "can find books by title, case insensitive"() {
        given:
        def bookTitle = 'time'
        def emptyTest = 'test'

        and:
        Iterable<Book> books = bookDao.findByTitle(bookTitle)
        Iterable<Book> emptyBooks = bookDao.findByTitle(emptyTest)

        and:
        def rowsCount = countRowsInTableWhere(jdbcTemplate, table,
            "LOWER(title) LIKE LOWER('%$bookTitle%')")

        expect:
        rowsCount == 2
        books.size() == 2
        emptyBooks.isEmpty()
    }

    def "returns null if book is not found by id"() {
        given:
        def id = 0

        and:
        Book book = bookDao.findById(id)
        def rowsCount = countRowsInTableWhere(jdbcTemplate, table, "id = $id")

        expect:
        book == null
        rowsCount == 0
    }

    def "can find all books"() {
        given:
        Iterable<Book> books = bookDao.findAll()

        and:
        def rowsCount = countRowsInTable(jdbcTemplate, table)

        expect:
        books.size() == rowsCount
    }

    def "can save a book"() {
        given:
        def bookTitle = 'War and Peace'
        def authorId = 5
        def genreId = 1
        def year = '1869'

        and:
        Book book = new Book(bookTitle, authorId, genreId, year)

        and:
        def rowsCount = countRowsInTable(jdbcTemplate, table)

        when:
        book = bookDao.save(book)

        and:
        rowsCount = countRowsInTable(jdbcTemplate, table)

        then:
        book != old(book)
        rowsCount == old(rowsCount) + 1

    }

    def "can save a collection of books"() {
        given:
        Iterable<Book> books = bookDao.findAll()

        and:
        def booksCollection = [['War and Peace', 5, 1, '1869'], ['The Invisible Man', 4, 5, '1933']].collect {
            new Book(it[0] as String, it[1] as Long, it[2] as Long, it[3] as String)
        }

        when:
        books = bookDao.save(booksCollection)

        then:
        books != old(books)
        books.size() == old(books.size()) + booksCollection.size()
    }

    def "can delete a book"() {
        given:
        def bookId = 3
        Book book = bookDao.findById(bookId)
        assert book != null

        and:
        def rowsCount = countRowsInTable(jdbcTemplate, table)

        when:
        bookDao.delete(book)

        and:
        rowsCount = countRowsInTable(jdbcTemplate, table)

        then:
        rowsCount == old(rowsCount) - 1
        bookDao.findById(bookId) == null
    }

    def "can find books by author"() {
        given:
        def author = 'leo'
        def emptyTest = 'test'

        when:
        Iterable<Book> books = bookDao.findByAuthor(author)
        Iterable<Book> emptyBooks = bookDao.findByAuthor(emptyTest)

        then:
        books != null
        books.size() > 0
        emptyBooks.isEmpty()
    }

    def "can find books by genre"() {
        given:
        def genre = 'biography'
        def emptyTest = 'test'

        when:
        Iterable<Book> books = bookDao.findByGenre(genre)
        Iterable<Book> emptyBooks = bookDao.findByGenre(emptyTest)

        then:
        books.size() > 0
        emptyBooks.isEmpty()
    }

    def "can find books with details on author and genre"() {
        given:
        def books = bookDao.findAllWithDetails()

        and:
        def rowsCount = countRowsInTable(jdbcTemplate, table)

        expect:
        books.size() == rowsCount
        books[0].get('author') == 'Lewis Carrol'
        books[0].get('genre') == 'Fantasy'
    }

    void cleanup() {
        bookDao = null
        jdbcTemplate == null
    }
}
