package com.kirilo.controllers;

import com.kirilo.beans.Book;
import com.kirilo.db.Database;
import com.kirilo.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

// https://myfaces.apache.org/wiki/core/user-guide/jsf-and-myfaces-howtos/backend/accessing-one-managed-bean-from-another.html
@ManagedBean(eager = true)
@SessionScoped
public class BookList implements Serializable {
    public static final String SELECT_ALL_FIELDS = "select b.id, b.name, b.page_count, b.isbn, b.publish_year, b.description, "
            + "g.name as genre, a.full_name as author, p.name as publisher from book b "
            + "inner join genre g on b.genre_id=g.id "
            + "inner join author a on b.author_id=a.id "
            + "inner join publisher p on b.publisher_id=p.id";
    private static final long serialVersionUID = 1353727775955418805L;
    private final List<Book> books;
    @ManagedProperty(value = "#{search}")
    private Search search;

    public BookList() {
        books = new ArrayList<>();
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public List<Book> getBooks() {
        return books;
    }

    private List<Book> getBooksFromDB(String query) {
        books.clear();
        try (
                Statement statement = (Database.getConnection()).createStatement();
                ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                final Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setName(resultSet.getString("name"));
                book.setPageCount(resultSet.getInt("page_count"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setGenre(resultSet.getString("genre"));
                book.setAuthor(resultSet.getString("author"));
                book.setPublishYear(resultSet.getInt("publish_year"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setDescription(resultSet.getString("description"));
                books.add(book);
            }
        } catch (SQLException throwables) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't get books from DB!", throwables);
        }
        return books;
    }

    public int getBooksCount(String query) {
        try (
                Statement statement = (Database.getConnection()).createStatement();
                ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't get books from DB!", throwables);
        }
        return 0;
    }

    private List<Book> checkAndGetBooks(String sql) {
        String query = String.format("%s %s order by b.name", SELECT_ALL_FIELDS, sql);

        if (sql.equals(search.getCurrentSQL())) {
            if (search.getTotalBooksCount() <= search.getSelectedPage()) {
                return books;
            }
            query = String.format(
                    "%s limit %d, %d", query, (search.getSelectedPage() - 1) * search.getBooksOnPage(), search.getBooksOnPage());
        } else {
            search.setCurrentSQL(sql);
            final int count = getBooksCount(String.format("select count(*) from book b %s%s", search.getSearchType() == SearchType.AUTHOR ? "inner join author a on b.author_id=a.id " : "", sql));
            search.setTotalBooksCount(count);

            int pageCount = count / search.getBooksOnPage() + (count % search.getBooksOnPage() > 0 ? 1 : 0);

            if (pageCount > 1) {
                final ArrayList<Integer> list = new ArrayList<>();
                for (int i = 0; i < pageCount; i++) {
                    list.add(i + 1);
                }
                search.setListPageNumbers(list);
                search.setSelectedPage(1);
                query = String.format(
                        "%s limit %d, %d", query, (search.getSelectedPage() - 1) * search.getBooksOnPage(), search.getBooksOnPage());
            }
        }
        return getBooksFromDB(query);
    }


    public List<Book> getAllBooks() {
        String sql = "";
//        final String query = String.format("%s %s order by b.name", SELECT_ALL_FIELDS, sql);
        return checkAndGetBooks(sql);
    }


    public List<Book> getBookByGenre() {
        search.imitatSlowLoading();
        search.resetParameters();
        int id = Integer.parseInt(getRequestParameters().get("genre_id"));
        final String sql = String.format("where genre_id=%d", id);
        search.setSelectedGenre(id);
//        final String query = String.format("%s %s order by b.name", SELECT_ALL_FIELDS, sql);
        return checkAndGetBooks(sql);
    }


    private Map<String, String> getRequestParameters() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    }


    public List<Book> getBooksByLetter() {
        search.resetParameters();
        String ch = getRequestParameters().get("letter_id").substring(0, 1);
        final String sql = String.format("where b.name like '%s%%'", ch.toLowerCase());
        search.setSelectedChar(ch);
//        final String query = String.format("%s %s order by b.name", SELECT_ALL_FIELDS, sql);
        return checkAndGetBooks(sql);
    }

    public List<Book> getBooksByString() {
        search.resetParameters();
        //    private String currentSQL;
        String s_type = (search.getSearchType() == SearchType.AUTHOR ? "a.full_name" : "b.name");
        String s_string = search.getSearchString();
        final String sql = String.format("where %s like '%%%s%%'", s_type, s_string);
//        final String query = String.format("%s %s order by b.name", SELECT_ALL_FIELDS, sql);
        return checkAndGetBooks(sql);
    }

    public void selectPage() {
        search.resetParameters();
        search.setSelectedPage(Integer.parseInt(getRequestParameters().get("page_number")));
//        getBooksFromDB(currentSQL);
        checkAndGetBooks(search.getCurrentSQL());
//        return "books";
    }
}
