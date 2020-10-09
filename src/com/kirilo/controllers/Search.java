package com.kirilo.controllers;

import com.kirilo.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

@ManagedBean(eager = true)
@SessionScoped
public class Search implements Serializable {
    static final long serialVersionUID = -5929977093130714822L;
    private static Map<String, SearchType> typeMap;
    private SearchType searchType;
    private String searchString;
    private int totalBooksCount;
    private int booksOnPage = 2;
    private List<Integer> listPageNumbers;
    private int selectedPage = 1;
    private int selectedGenre = 0;
    private String selectedChar;
    private String currentSQL;

    public void resetParameters() {
        selectedPage = 0;
        selectedGenre = 0;
        selectedChar = "";
    }

    public Search() {
        listPageNumbers = new ArrayList<>();
        typeMap = new HashMap<>();
        final ResourceBundle bundle = ResourceBundle.getBundle("com.kirilo.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        typeMap.put(bundle.getString("author.name"), SearchType.AUTHOR);
        typeMap.put(bundle.getString("book.name"), SearchType.TITLE);
    }

    public String getCurrentSQL() {
        return currentSQL;
    }

    public void setCurrentSQL(String currentSQL) {
        this.currentSQL = currentSQL;
    }

    public Map<String, SearchType> getTypeMap() {
        return typeMap;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public int getTotalBooksCount() {
        return totalBooksCount;
    }

    public void setTotalBooksCount(int totalBooksCount) {
        this.totalBooksCount = totalBooksCount;
    }

    public int getBooksOnPage() {
        return booksOnPage;
    }

    public void setBooksOnPage(int booksOnPage) {
        this.booksOnPage = booksOnPage;
    }

    public List<Integer> getListPageNumbers() {
        return listPageNumbers;
    }

    public void setListPageNumbers(List<Integer> ListPageNumbers) {
        this.listPageNumbers = ListPageNumbers;
    }

/*    public int selectPage() {

        return 1;
    }*/

    public int getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(int selectedPage) {
        this.selectedPage = selectedPage;
    }

    public void setSelectedGenre(int id) {
        this.selectedGenre = id;
    }

    public int getSelectedGenre() {
        return selectedGenre;
    }

    public void setSelectedChar(String ch) {
        this.selectedChar = ch;
    }

    public String getSelectedChar() {
        return selectedChar;
    }
}
