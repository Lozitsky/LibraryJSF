package com.kirilo.beans.repositories;

import com.kirilo.entities.Genre;
import com.kirilo.entities.Book;
import com.kirilo.enums.SearchType;

import java.util.List;

public interface BookRepository {
//    List<Book> getCurrentBooks();

    List<Book> getAllBooks();

    List<Book> getBooksByGenre(int id);

    List<Book> getBooksByLetter(String ch);

    List<Book> getBooksByString(SearchType s_type, String s_string);

    List<Book> getBooksFromSelectedPage();

    void updateBooks(List<Book> books);

    List<Book> getBookListFromPage(int i);

    List<Genre> getAllGenres();
}
