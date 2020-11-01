package com.kirilo.beans.commands;

import com.kirilo.beans.Book;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name = "booksByGenre")
@SessionScoped
public class ReceiveBooksByGenre extends BaseCommand {
    @Override
    public List<Book> execute() {
        int id = Integer.parseInt(getRequestParameters().get("genre_id"));
        search.resetParameters();
        search.setSelectedGenre(id);
        return bookList.getBookByGenre(id);
    }
}
