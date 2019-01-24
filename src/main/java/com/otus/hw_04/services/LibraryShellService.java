package com.otus.hw_04.services;

import com.otus.hw_04.dao.AuthorDao;
import com.otus.hw_04.dao.BookDao;
import com.otus.hw_04.dao.GenreDao;
import com.otus.hw_04.domain.Genre;
import org.h2.tools.Console;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@ShellComponent
public class LibraryShellService {

    private final GenreDao genreDao;
    private final AuthorDao authorDao;
    private final BookDao bookDao;

    @Autowired
    public LibraryShellService(final GenreDao genreDao, final AuthorDao authorDao, final BookDao bookDao) {
        this.genreDao = genreDao;
        this.authorDao = authorDao;
        this.bookDao = bookDao;
    }

    @ShellMethod("Display all available genres.")
    public void displayGenres() {
        genreDao.findAll().forEach(g -> System.out.println(g.toString()));
    }

    @ShellMethod("Find genre by id.")
    public void findGenreById(final long id) {
        System.out.println(genreDao.findById(id));
    }

    @ShellMethod("Find genre by name.")
    public void findGenreByName(final String genre) {
        System.out.println(genreDao.findByGenre(genre));
    }

    @ShellMethod("Delete a genre by name.")
    public void deleteGenreByName(final String name) {
        final Genre genre = genreDao.findByGenre(name);
        if (genre != null) {
            genreDao.delete(genre);
            System.out.println(name + " genre successfully deleted!");
        } else {
            System.out.println(name + " genre is not found");
        }
    }

    @ShellMethod("Save a genre.")
    public void saveGenre(final Genre genre) {
        genreDao.save(genre);
    }

    @ShellMethod("Display all available authors.")
    public void displayAuthors() {
        authorDao.findAll().forEach(g -> System.out.println(g.toString()));
    }

    @ShellMethod("Display all available books.")
    public void displayBooksWithDetails() {
        bookDao.findAllWithDetails().forEach(g -> System.out.println(g.toString()));
    }

    @ShellMethod("Runs database console")
    public void dbConsole() throws SQLException {
        Console.main();
    }

}
