package com.kirilo.controllers;

import com.kirilo.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@ManagedBean
@SessionScoped
public class Search implements Serializable {
    static final long serialVersionUID = 5977375980978964005L;
    private static Map<String, SearchType> typeMap;
    private SearchType searchType;

    public Search() {
        typeMap = new HashMap<>();
        final ResourceBundle bundle = ResourceBundle.getBundle("com.kirilo.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        typeMap.put(bundle.getString("author.name"), SearchType.AUTHOR);
        typeMap.put(bundle.getString("book.name"), SearchType.TITLE);
    }

    public Map<String, SearchType> getTypeMap() {
        return typeMap;
    }

    public SearchType getSearchType() {
        return searchType;
    }
}
