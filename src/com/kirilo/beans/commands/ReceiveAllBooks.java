package com.kirilo.beans.commands;

import com.kirilo.entities.Book;
import com.kirilo.enums.BookSearch;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name = "allBooks", eager = false)
@SessionScoped
public class ReceiveAllBooks extends BaseCommand{
    @Override
    public List<Book> execute() {
        pager.resetParameters();
        pager.setBookSearch(BookSearch.ALL);
        return controller.getAllBooks();
    }
}
