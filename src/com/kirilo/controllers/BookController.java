package com.kirilo.controllers;

import com.kirilo.entities.Genre;
import com.kirilo.beans.repositories.BookRepository;
import com.kirilo.entities.Book;
import com.kirilo.enums.SearchType;
import org.jboss.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// https://myfaces.apache.org/wiki/core/user-guide/jsf-and-myfaces-howtos/backend/accessing-one-managed-bean-from-another.html
@ManagedBean(name = "bookController", eager = true)
@SessionScoped
public class BookController implements Serializable {
    private static final long serialVersionUID = 5392526366565724328L;
    private final List<Book> books;

    public BookController() {
        books = new ArrayList<>();
    }


    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    @ManagedProperty(value = "#{search}")
    private Search search;

    @ManagedProperty(value = "#{dataHelper}")
    private BookRepository bookRepository;

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getCurrentBooks() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getCurrentBooks():\n");

        return books;
    }

    public List<Book> getAllBooks() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getAllBooks():\n");

        return changeAndGetBooks(bookRepository.getAllBooks());
    }

    public List<Book> getBooksByGenre(int id) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getBookByGenre():\n");

        return changeAndGetBooks(bookRepository.getBooksByGenre(id));
    }

    public List<Book> getBooksByLetter(String ch) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getBooksByLetter():\n");

        return changeAndGetBooks(bookRepository.getBooksByLetter(ch));
    }

    public List<Book> getBooksByString(SearchType searchType, String sString) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getBooksByString():\n");

        return changeAndGetBooks(bookRepository.getBooksByString(searchType, sString));
    }

    public List<Book> getBooksFromSelectedPage() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getBooksFromSelectedPage():\n");

        return changeAndGetBooks(bookRepository.getBooksFromSelectedPage());
    }

    public void updateBooks() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute updateBooks():\n" + books.size());

//        books.updateBooks(books);
        bookRepository.updateBooks(books);
    }

/*    public void setNumberOfBooksPerPage(int i) {
        search.booksOnPageChanged(i);
        bookRepository.setNumberOfBooksPerPage(i);
    }*/

    public void resetModeForAllBooks() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute resetModeForAllBooks():\n");
        books.forEach(book -> book.setEdit(false));
    }

    public void changeNumberOfBooksPerPage(ValueChangeEvent valueChangeEvent) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute changeNumberOfBooksPerPage():\n");

        int i = Integer.parseInt(valueChangeEvent.getNewValue().toString());
        search.booksOnPageChanged(i);
        changeAndGetBooks(bookRepository.getBookListFromPage(i));
    }

    private List<Book> changeAndGetBooks(List<Book> b) {
        books.clear();
        books.addAll(b);
        return books;
    }

    public List<Genre> getGenreList() {
        return bookRepository.getAllGenres();
    }
}
