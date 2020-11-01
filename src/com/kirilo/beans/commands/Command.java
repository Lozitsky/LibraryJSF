package com.kirilo.beans.commands;

import com.kirilo.beans.Book;

import java.util.List;

public interface Command {
    void backup(String sql, int booksOnPage);

    void undo();

    public List<Book> execute();
}
