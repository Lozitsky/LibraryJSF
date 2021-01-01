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
    private BookController controller;

    public BookController getController() {
        return controller;
    }

    public void setController(BookController controller) {
        this.controller = controller;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void edit() {
        editMode = true;
    }

    public void save() {
        controller.updateBooks();
        controller.resetModeForAllBooks();
        editMode = false;
    }

    public void cancel() {
        controller.resetModeForAllBooks();
        editMode = false;
    }


}
