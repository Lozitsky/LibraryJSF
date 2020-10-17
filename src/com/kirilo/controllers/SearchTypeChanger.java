package com.kirilo.controllers;

import com.kirilo.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@ManagedBean(eager = true)
@SessionScoped
public class SearchTypeChanger implements Serializable {
    private static final long serialVersionUID = 8340836521715812470L;
    private static Map<String, SearchType> typeMap;
    private SearchType searchType;

    public SearchTypeChanger() {
        typeMap = new HashMap<>();
        ChangeLocale();
    }

    private void ChangeLocale() {
        typeMap.clear();
        ResourceBundle bundle = ResourceBundle.getBundle("com.kirilo.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        typeMap.put(bundle.getString("author.name"), SearchType.AUTHOR);
        typeMap.put(bundle.getString("book.name"), SearchType.TITLE);
    }

    public Map<String, SearchType> getTypeMap() {
        return typeMap;
    }

    public SearchType getSearchType() {
        ChangeLocale();
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }
}
