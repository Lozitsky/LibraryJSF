package com.kirilo.beans.commands;

import com.kirilo.entities.Book;
import com.kirilo.enums.BookSearch;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name = "booksByGenre", eager = false)
@SessionScoped
public class ReceiveBooksByGenre extends BaseCommand {
    @Override
    public List<Book> execute() {
        int id = Integer.parseInt(getRequestParameters().get("genre_id"));
        pager.resetParameters();
        pager.setSelectedGenre(id);
        pager.setBookSearch(BookSearch.GENRE);
        return controller.getBooksByGenre(id);
    }
}
