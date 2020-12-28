package com.kirilo.controllers;

import com.kirilo.beans.Book;
import com.kirilo.beans.repositories.BookList;
import com.kirilo.beans.repositories.BookRepository;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import java.util.List;

// https://myfaces.apache.org/wiki/core/user-guide/jsf-and-myfaces-howtos/backend/accessing-one-managed-bean-from-another.html
@ManagedBean(name = "bookController", eager = true)
@SessionScoped
public class BookController implements BookRepository {

    @ManagedProperty(value = "#{bookList}")
    private BookList books;

    public BookList getBooks() {
        return books;
    }

    public void setBooks(BookList books) {
        this.books = books;
    }

    public List<Book> getCurrentBooks() {
        return books.getCurrentBooks();
    }

    @Override
    public List<Book> getAllBooks() {
        return books.getAllBooks();
    }

    @Override
    public List<Book> getBookByGenre(int id) {
        return books.getBookByGenre(id);
    }

    @Override
    public List<Book> getBooksByLetter(String ch) {
        return books.getBooksByLetter(ch);
    }

    @Override
    public List<Book> getBooksByString(String s_type, String s_string) {
        return books.getBooksByString(s_type, s_string);
    }

    @Override
    public List<Book> getBooksFromSelectedPage() {
        return books.getBooksFromSelectedPage();
    }

    @Override
    public void updateBooks() {
        books.updateBooks();
    }

    public void resetMode() {
        books.getCurrentBooks().forEach(book -> book.setEdit(false));
    }

    public void ChangeNumberOfBooksPerPage(ValueChangeEvent valueChangeEvent) {
        int i = Integer.parseInt(valueChangeEvent.getNewValue().toString());
        books.setNumberOfBooksPerPage(i);
    }
}
