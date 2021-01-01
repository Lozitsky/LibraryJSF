package com.kirilo.beans.commands;

import com.kirilo.entities.Book;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name = "allBooks")
@SessionScoped
public class ReceiveAllBooks extends BaseCommand{
    @Override
    public List<Book> execute() {
        search.resetParameters();
        return controller.getAllBooks();
    }
}
