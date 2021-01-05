package com.kirilo.beans;

import com.kirilo.controllers.BookController;
import com.kirilo.controllers.Pager;
import org.jboss.logging.Logger;

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
    @ManagedProperty("#{pager}")
    private Pager pager;

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }
/*    @ManagedProperty(value = "#{pageCount}")
    private Command command;

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }*/

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
        org.jboss.logging.Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "!!!\n" + pager.getListPageNumbers().size());

        controller.updateBooks();
        controller.resetModeForAllBooks();
        controller.getBooksFromSelectedPage();
        org.jboss.logging.Logger.getLogger(this.getClass().getName()).log(Logger.Level.INFO, "???\n"+ pager.getListPageNumbers().size());

//        command.execute();
        editMode = false;
    }

    public void cancel() {
        controller.resetModeForAllBooks();
        editMode = false;
    }


}
