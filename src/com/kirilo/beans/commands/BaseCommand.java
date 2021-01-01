package com.kirilo.beans.commands;

import com.kirilo.controllers.BookController;
import com.kirilo.controllers.Search;

import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

public abstract class BaseCommand implements Command, Serializable {
    private static final long serialVersionUID = 6183959340404573695L;

    @ManagedProperty(value = "#{bookController}")
    protected BookController controller;

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    @ManagedProperty(value = "#{search}")
    protected Search search;
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
        itemsOnPage = search.getBooksOnPage();
    }

    @Override
    public void undo() {
//        search.setCurrentSQL(sqlQuery);
        search.setBooksOnPage(itemsOnPage);
    }

    protected Map<String, String> getRequestParameters() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    }
}
