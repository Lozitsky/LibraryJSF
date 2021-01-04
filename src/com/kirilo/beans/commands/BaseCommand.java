package com.kirilo.beans.commands;

import com.kirilo.controllers.BookController;
import com.kirilo.controllers.Pager;

import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

public abstract class BaseCommand implements Command, Serializable {
    private static final long serialVersionUID = 6183959340404573695L;

    @ManagedProperty(value = "#{bookController}")
    protected BookController controller;

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    @ManagedProperty(value = "#{pager}")
    protected Pager pager;
    private String sqlQuery;
    private int itemsOnPage;

    public BaseCommand() {
    }

    public BookController getController() {
        return controller;
    }

    public void setController(BookController controller) {
        this.controller = controller;
    }

    @Override
    public void backup(String sql, int booksOnPage) {
//        sqlQuery = search.getCurrentSQL();
        itemsOnPage = pager.getBooksOnPage();
    }

    @Override
    public void undo() {
//        search.setCurrentSQL(sqlQuery);
        pager.setBooksOnPage(itemsOnPage);
    }

    protected Map<String, String> getRequestParameters() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    }
}
