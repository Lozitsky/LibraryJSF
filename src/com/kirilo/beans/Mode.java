package com.kirilo.beans;

import com.kirilo.controllers.BookController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

// https://stackoverflow.com/a/7167535/9586230
@ManagedBean(eager = true)
@SessionScoped
public class Mode implements Serializable {
    private static final long serialVersionUID = 8876614600694579454L;

    private boolean editMode;
    @ManagedProperty(value = "#{bookController}")
    private BookController bookList;

    public BookController getBookList() {
        return bookList;
    }

    public void setBookList(BookController bookList) {
        this.bookList = bookList;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void edit() {
        editMode = true;
    }

    public void save() {
        bookList.updateBooks();
        bookList.resetModeForAllBooks();
        editMode = false;
    }

    public void cancel() {
        bookList.resetModeForAllBooks();
        editMode = false;
    }


}
