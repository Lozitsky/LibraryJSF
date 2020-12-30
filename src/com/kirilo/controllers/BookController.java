package com.kirilo.controllers;

import com.kirilo.beans.repositories.BookRepository;
import com.kirilo.entities.Book;
import com.kirilo.enums.SearchType;
import org.jboss.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.List;

// https://myfaces.apache.org/wiki/core/user-guide/jsf-and-myfaces-howtos/backend/accessing-one-managed-bean-from-another.html
@ManagedBean(name = "bookController", eager = true)
@SessionScoped
public class BookController implements BookRepository, Serializable {
    private static final long serialVersionUID = 5392526366565724328L;

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    @ManagedProperty(value = "#{search}")
    private Search search;

    @ManagedProperty(value = "#{dataHelper}")
    private BookRepository books;

    public BookRepository getBooks() {
        return books;
    }

    public void setBooks(BookRepository books) {
        this.books = books;
    }

    @Override
    public List<Book> getCurrentBooks() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getCurrentBooks():\n");

        return books.getCurrentBooks();
    }

    @Override
    public List<Book> getAllBooks() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getAllBooks():\n");

        return books.getAllBooks();
    }

    @Override
    public List<Book> getBooksByGenre(int id) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getBookByGenre():\n");

        return books.getBooksByGenre(id);
    }

    @Override
    public List<Book> getBooksByLetter(String ch) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getBooksByLetter():\n");

        return books.getBooksByLetter(ch);
    }

    @Override
    public List<Book> getBooksByString(SearchType searchType, String sString) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getBooksByString():\n");

        return books.getBooksByString(searchType, sString);
    }

    @Override
    public List<Book> getBooksFromSelectedPage() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getBooksFromSelectedPage():\n");

        return books.getBooksFromSelectedPage();
    }

    @Override
    public void updateBooks() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute updateBooks():\n");

        books.updateBooks();
    }

    @Override
    public void setNumberOfBooksPerPage(int i) {
        search.booksOnPageChanged(i);
        books.setNumberOfBooksPerPage(i);
    }

    public void resetModeForAllBooks() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute resetModeForAllBooks():\n");
        books.getCurrentBooks().forEach(book -> book.setEdit(false));
    }

    public void changeNumberOfBooksPerPage(ValueChangeEvent valueChangeEvent) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute changeNumberOfBooksPerPage():\n");

        int i = Integer.parseInt(valueChangeEvent.getNewValue().toString());
        books.setNumberOfBooksPerPage(i);
    }
}
