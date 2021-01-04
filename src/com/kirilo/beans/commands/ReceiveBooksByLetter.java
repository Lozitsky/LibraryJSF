package com.kirilo.beans.commands;

import com.kirilo.entities.Book;
import com.kirilo.enums.BookSearch;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name = "booksByLetter", eager = false)
@SessionScoped
public class ReceiveBooksByLetter extends BaseCommand {
    @Override
    public List<Book> execute() {
        String ch = getRequestParameters().get("letter_id").substring(0, 1);
        pager.resetParameters();
        pager.setSelectedChar(ch);
        pager.setBookSearch(BookSearch.ALPHABET);
        return controller.getBooksByLetter(ch);
    }
}
