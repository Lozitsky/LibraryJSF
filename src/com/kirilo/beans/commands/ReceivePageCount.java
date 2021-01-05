package com.kirilo.beans.commands;

import com.kirilo.entities.Book;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "pageCount", eager = false)
@SessionScoped
public class ReceivePageCount extends BaseCommand {
    @Override
    public List<Book> execute() {
        pager.setSelectedPage(1);
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Total pages: "
                + pager.getListPageNumbers().size() + "\n");
        return controller.getBooksFromSelectedPage();
    }
}
