package com.kirilo.beans.commands;

import com.kirilo.beans.Book;
import com.kirilo.controllers.SearchTypeChanger;
import com.kirilo.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name = "bookByString")
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
        String s_type = (searchTypeChanger.getSearchType() == SearchType.AUTHOR ? "a.full_name" : "b.name");
        String s_string = search.getSearchString();
        search.resetParameters();
        return bookList.getBooksByString(s_type, s_string);
    }
}
