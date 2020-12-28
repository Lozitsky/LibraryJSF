package com.kirilo.beans.repositories;

import com.kirilo.beans.Book;

import java.util.List;

public interface BookRepository {
    List<Book> getAllBooks();

    List<Book> getBookByGenre(int id);

    List<Book> getBooksByLetter(String ch);

    List<Book> getBooksByString(String s_type, String s_string);

    List<Book> getBooksFromSelectedPage();

    void updateBooks();
}
