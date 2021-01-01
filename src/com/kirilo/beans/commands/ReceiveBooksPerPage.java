package com.kirilo.beans.commands;

import com.kirilo.entities.Book;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name = "booksPerPage")
@SessionScoped
public class ReceiveBooksPerPage extends BaseCommand {
    @Override
    public List<Book> execute() {
        search.setSelectedPage(Integer.parseInt(getRequestParameters().get("page_number")));
        return controller.getBooksFromSelectedPage();
    }
}
