package com.kirilo.beans.commands;

import com.kirilo.beans.Book;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name = "booksByLetter")
@SessionScoped
public class ReceiveBooksByLetter extends BaseCommand {
    @Override
    public List<Book> execute() {
        String ch = getRequestParameters().get("letter_id").substring(0, 1);
        search.resetParameters();
        search.setSelectedChar(ch);
        return bookList.getBooksByLetter(ch);
    }
}
