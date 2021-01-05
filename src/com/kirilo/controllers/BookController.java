package com.kirilo.controllers;

import com.kirilo.beans.repositories.BookRepository;
import com.kirilo.entities.Book;
import com.kirilo.entities.Genre;
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
@ManagedBean(name = "bookController", eager = false)
@SessionScoped
public class BookController implements Serializable {
    private static final long serialVersionUID = 5392526366565724328L;
    private final List<Book> books;
    //    private final Map<Long, byte[]> images;
    @ManagedProperty(value = "#{pager}")
    private Pager pager;
    @ManagedProperty(value = "#{dataHelper}")
    private BookRepository bookRepository;
    @ManagedProperty(value = "#{searchTypeChanger}")
    private SearchTypeChanger searchType;

    public BookController() {
        books = new ArrayList<>();
//        images = new HashMap<>();
    }

    public SearchTypeChanger getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchTypeChanger searchType) {
        this.searchType = searchType;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

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
        changeQuantityOfBooks();
        return changeAndGetBooks(bookRepository.getAllBooks());
    }

    public List<Book> getBooks() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getAllBooks():\n");
        changeQuantityOfBooks();
        return changeAndGetBooks(bookRepository.getBooks(getFirstItem(), getBooksOnPage()));
    }

    public List<Book> getBooksByGenre(int id) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getBookByGenre():\n" + pager.getListPageNumbers().size());
        changeNumberOfPages(bookRepository.getQuantityOfBooksByGenre(id));
        return changeAndGetBooks(bookRepository.getBooksByGenre(id, getFirstItem(), getBooksOnPage()));
    }

    public List<Book> getBooksByString(SearchType searchType, String sString) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getBooksByString():\n");
        changeNumberOfPages(bookRepository.getQuantityOfBooksByString(searchType, sString));
        return changeAndGetBooks(bookRepository.getBooksByString(searchType, sString, getFirstItem(), getBooksOnPage()));
    }

    public List<Book> getBooksByLetter(String ch) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getBooksByLetter():\n");
        changeNumberOfPages(bookRepository.getQuantityOfBooksByLetter(ch));
        return changeAndGetBooks(bookRepository.getBooksByLetter(ch, getFirstItem(), getBooksOnPage()));
    }

    private int getBooksOnPage() {
        return pager.getBooksOnPage();
    }

    private int getFirstItem() {
        return (pager.getSelectedPage() - 1) * getBooksOnPage();
    }

    public void updateBooks() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute updateBooks():\n" + books.size());

        bookRepository.updateBooks(books);
    }

    public void resetModeForAllBooks() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute resetModeForAllBooks():\n");
        books.forEach(book -> book.setEdit(false));
    }

    public void changeNumberOfBooksPerPage(ValueChangeEvent valueChangeEvent) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute changeNumberOfBooksPerPage():\n");

        int i = Integer.parseInt(valueChangeEvent.getNewValue().toString());
        pager.booksOnPageChanged(i);
    }

    private List<Book> changeAndGetBooks(List<Book> b) {
        books.clear();
        books.addAll(b);
        return books;
    }

    public List<Genre> getGenreList() {
        return bookRepository.getAllGenres();
    }

    //    https://stackoverflow.com/questions/23079003/how-to-convert-a-java-8-stream-to-an-array
    public List<byte[]> getImages() {
        return bookRepository.getImage(books.stream().map(Book::getId).toArray(Long[]::new));
    }

    public byte[] getContent(Long value) {
        return bookRepository.getBook(value).getContent();
    }

    public byte[] getImage(Long value) {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "Execute getImage():\n");
        return bookRepository.getBook(value).getImage();
    }

    public void changeQuantityOfBooks() {
        changeNumberOfPages(bookRepository.getQuantityOfBooks());
    }

    private void changeNumberOfPages(Long count) {
        pager.setNumberOfPages(count);
        pager.setTotalQuantityOfBooks(count);
    }

    public List<Book> getBooksFromSelectedPage() {
        Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "!!!pager.getBookSearch():\n" + pager.getBookSearch());

        switch (pager.getBookSearch()) {
            case GENRE:
                return getBooksByGenre(pager.getSelectedGenre());
            case STRING:
                return getBooksByString(searchType.getSearchType(), pager.getSearchString());
            case ALPHABET:
                return getBooksByLetter(pager.getSelectedChar());
            default:
                ALL:
                return getBooks();
        }

//        return changeAndGetBooks(books);
    }
}
