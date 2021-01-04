package com.kirilo.beans.commands;

import com.kirilo.controllers.SearchTypeChanger;
import com.kirilo.entities.Book;
import com.kirilo.enums.BookSearch;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name = "bookByString", eager = false)
@SessionScoped
public class ReceiveBooksByString extends BaseCommand {
    @ManagedProperty(value = "#{searchTypeChanger}")
    private SearchTypeChanger searchTypeChanger;

    public SearchTypeChanger getSearchTypeChanger() {
        return searchTypeChanger;
    }

    public void setSearchTypeChanger(SearchTypeChanger searchTypeChanger) {
        this.searchTypeChanger = searchTypeChanger;
    }

    @Override
    public List<Book> execute() {
//        String s_type = (searchTypeChanger.getSearchType() == SearchType.AUTHOR ? "a.full_name" : "b.name");
        String s_string = pager.getSearchString();
        pager.resetParameters();
        pager.setBookSearch(BookSearch.STRING);

        return controller.getBooksByString(searchTypeChanger.getSearchType(), s_string);
    }
}
