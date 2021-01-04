package com.kirilo.beans.commands;

import com.kirilo.entities.Book;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name = "pageCount", eager = false)
@SessionScoped
public class ReceivePageCount extends BaseCommand {
    @Override
    public List<Book> execute() {
        pager.setSelectedPage(1);

        return controller.getBooksFromSelectedPage();
    }
}
