package com.kirilo.beans.repositories;

import com.kirilo.entities.Book;
import com.kirilo.entities.Genre;
import com.kirilo.enums.SearchType;

import java.util.List;

public interface BookRepository {

    List<Book> getAllBooks();

    List<Book> getBooksByGenre(int id);

    List<Book> getBooksByLetter(String ch);

    List<Book> getBooksByString(SearchType s_type, String s_string);

    List<Book> getBooksFromCurrentPage();

    void updateBooks(List<Book> books);

    List<Book> getBookListFromPage(int i);

    List<Genre> getAllGenres();

    List<byte[]> getImage(Long... array);

    List<byte[]> getContent(Long... array);

    Book getBook(Long value);

    Long getQuantityOfBooks();

    Long getQuantityOfBooksByGenre(int id);

    Long getQuantityOfBooksByString(SearchType searchType, String sString);

    List<Book> getBooks(int first, int items);

    List<Book> getBooksByGenre(int id, int first, int items);

    List<Book> getBooksByString(SearchType searchType, String searchString, int first, int items);

    List<Book> getBooksByLetter(String ch, int first, int items);

    Long getQuantityOfBooksByLetter(String ch);
}
