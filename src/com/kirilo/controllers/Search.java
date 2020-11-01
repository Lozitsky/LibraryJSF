package com.kirilo.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(eager = true)
@SessionScoped
public class Search implements Serializable {
    static final long serialVersionUID = -5929977093130714822L;

    private String searchString;
    private int totalBooksCount;
    private int booksOnPage = 2;
    private List<Integer> listPageNumbers;
    private int selectedPage = 1;
    private int selectedGenre = 0;
    private String selectedChar;
    private String currentSQL;

    public Search() {
        listPageNumbers = new ArrayList<>();
    }

    public void resetParameters() {
        selectedGenre = 0;
        selectedChar = "";
        selectedPage = 1;
    }

    public String getCurrentSQL() {
        return currentSQL;
    }

    public void setCurrentSQL(String currentSQL) {
        this.currentSQL = currentSQL;
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

    public int getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(int selectedPage) {
        this.selectedPage = selectedPage;
    }

    public int getSelectedGenre() {
        return selectedGenre;
    }

    public void setSelectedGenre(int id) {
        this.selectedGenre = id;
    }

    public String getSelectedChar() {
        return selectedChar;
    }

    public void setSelectedChar(String ch) {
        this.selectedChar = ch;
    }

    public void imitationSlowLoading() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void booksOnPageChanged(int booksOnPage) {
        this.booksOnPage = booksOnPage;
        selectedPage = 1;
    }
}
