package com.kirilo.controllers;

import com.kirilo.enums.BookSearch;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(eager = true)
@SessionScoped
public class Pager implements Serializable {
    static final long serialVersionUID = -5929977093130714822L;

    private String searchString;
    private long totalQuantityOfBooks;
    private int booksOnPage = 2;
    private List<Integer> listPageNumbers;
    private int selectedPage = 1;
    private int selectedGenre = 0;
    private String selectedChar;
    private BookSearch bookSearch = BookSearch.ALL;

    public Pager() {
        listPageNumbers = new ArrayList<>();
    }

    public BookSearch getBookSearch() {
        return bookSearch;
    }
//    private String currentSQL;

    public void setBookSearch(BookSearch bookSearch) {
        this.bookSearch = bookSearch;
    }

    public void resetParameters() {
        selectedGenre = 0;
        selectedChar = "";
        selectedPage = 1;
    }

/*    public String getCurrentSQL() {
        return currentSQL;
    }*/

//    public void setCurrentSQL(String currentSQL) {
//        this.currentSQL = currentSQL;
//    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public long getTotalQuantityOfBooks() {
        return totalQuantityOfBooks;
    }

    public void setTotalQuantityOfBooks(Long totalBooksCount) {
        this.totalQuantityOfBooks = totalBooksCount;
    }

    public int getBooksOnPage() {
        return booksOnPage;
    }

    public void setBooksOnPage(int booksOnPage) {
        this.booksOnPage = booksOnPage;
    }

    public void setNumberOfPages(long count) {
        long pageCount = count / getBooksOnPage() + (count % getBooksOnPage() > 0 ? 1 : 0);
        if (pageCount > 1) {
            if (pageCount != getListPageNumbers().size()) {
                final ArrayList<Integer> list = new ArrayList<>();
                for (int i = 1; i <= pageCount; ++i) {
                    list.add(i);
                }
                setListPageNumbers(list);
            }
        }
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
