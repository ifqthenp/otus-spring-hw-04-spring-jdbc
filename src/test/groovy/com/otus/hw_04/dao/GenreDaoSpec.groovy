package com.otus.hw_04.dao

import com.otus.hw_04.dao.impl.JdbcGenreDao
import com.otus.hw_04.domain.Genre
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.shell.Shell
import org.springframework.test.jdbc.JdbcTestUtils
import spock.lang.Specification
import spock.lang.Subject

@JdbcTest
@Import(JdbcGenreDao)
class GenreDaoSpec extends Specification {

    @SpringBean
    Shell shell = Mock()

    @Subject
    @Autowired
    GenreDao genreDao

    @Autowired
    JdbcTemplate jdbcTemplate

    void setup() {
        assert jdbcTemplate != null
        assert genreDao != null
    }

    def "can find genre by id"() {
        given:
        Genre genre = genreDao.findById(2)

        expect:
        genre.getId() == 2
        genre.getGenre() == 'Fantasy'
    }

    def "can find genre by its name, case insensitive"() {
        given:
        Genre fantasy = genreDao.findByGenre('fant')
        Genre novel = genreDao.findByGenre('nove')

        expect:
        fantasy.getGenre() == 'Fantasy'
        novel.getGenre() == 'Novel'
    }

    def "can find all genres"() {
        given:
        Iterable<Genre> genres = genreDao.findAll()

        and:
        def genresTableRowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "genres")

        expect:
        genres.size() == genresTableRowsCount
    }

    def "returns null if genre is not found"() {
        given:
        Genre genreById = genreDao.findById(10)
        Genre genreByName = genreDao.findByGenre('horror')

        expect:
        genreById == null
        genreByName == null
    }

    def "can save a genre"() {
        given:
        def genreName = 'Horror'
        def savedGenre = genreDao.save(new Genre(genreName))

        and:
        def foundGenre = genreDao.findByGenre(genreName)

        expect:
        foundGenre == savedGenre
        genreDao.findAll().any { it.genre == genreName }
    }

    def "can save a collection of genres"() {
        given:
        def before = genreDao.findAll()

        and:
        def genreCollection = ['Drama', 'Comedy', 'Satire'].collect { new Genre(it) }
        def after = genreDao.save(genreCollection)

        expect:
        after.size() == before.size() + genreCollection.size()
        after.any { it.genre == genreCollection.first().genre }
        after.any() { it.genre == genreCollection.last().genre }
    }

    def "can delete a genre"() {
        given:
        def genreName = 'fantasy'
        def genre = genreDao.findByGenre(genreName)

        and:
        genreDao.delete(genre)

        expect:
        genreDao.findByGenre(genreName) == null
    }

    void cleanup() {
        genreDao = null
        jdbcTemplate = null
    }
}
