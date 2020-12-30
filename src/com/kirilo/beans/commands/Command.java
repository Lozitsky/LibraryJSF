package com.kirilo.beans.commands;

import java.util.List;

// https://docs.oracle.com/javase/tutorial/java/generics/types.html

public interface Command<T> {
    void backup(String sql, int booksOnPage);

    void undo();

    public List<T> execute();
}
