package com.kirilo.controllers;

import com.kirilo.beans.Book;
import com.kirilo.db.Database;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
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
@RequestScoped
public class BookList implements Serializable {
    static final long serialVersionUID = 2492147478586933878L;
    @ManagedProperty(value = "#{search}")
    private Search search;
    private List<Book> books;

    public BookList() {
        books = new ArrayList<>();
//        books.addAll(getAllBooks());
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public List<Book> getBooks() {
        if (books.isEmpty()) {
//            books.addAll(getAllBooks());
            return getAllBooks();
        }
        return books;
    }

    public List<Book> getAllBooks() {
//        if (books.isEmpty()) {
//        books = getBooksFromDB("select * from book");

        books = getBooksFromDB("select b.id, b.name, b.page_count, b.isbn, b.publish_year, b.description, "
                + "g.name as genre, a.full_name as author, p.name as publisher from book b "
                + "inner join genre g on b.genre_id=g.id "
                + "inner join author a on b.author_id=a.id "
                + "inner join publisher p on b.publisher_id=p.id "
                + " order by b.name"
        );
//        }
        return books;
    }

    private List<Book> getBooksFromDB(String query) {
        books.clear();
        try (
//                Connection connection = Database.getConnection();
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

    /*int id*/
    public List<Book> getBooksByGenre() {
//        final Map<String, String> parameterMap = getRequestParameters();
        int id = Integer.parseInt(getRequestParameters().get("genre_id"));
        return getBooksFromDB(
                "select b.id, b.name, b.page_count, b.isbn, b.publish_year, b.description, "
                        + "g.name as genre, a.full_name as author, p.name as publisher from book b "
                        + "inner join genre g on b.genre_id=g.id "
                        + "inner join author a on b.author_id=a.id "
                        + "inner join publisher p on b.publisher_id=p.id "
                        + "where genre_id=" + id
                        + " order by b.name"
        );
    }

    private Map<String, String> getRequestParameters() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//        return parameterMap;
    }

    /*char c*/
    public List<Book> getBooksByLetter() {
        final String ch = getRequestParameters().get("letter").substring(0, 0).toLowerCase();
        return getBooksFromDB(
                "select b.id, b.name, b.page_count, b.isbn, b.publish_year, b.description, "
                        + "g.name as genre, a.full_name as author, p.name as publisher from book b "
                        + "inner join genre g on b.genre_id=g.id "
                        + "inner join author a on b.author_id=a.id "
                        + "inner join publisher p on b.publisher_id=p.id "
                        + "where b.name like '" + /*String.valueOf(c).toLowerCase()*/ ch + "%'"
                        + " order by b.name"
        );
    }

    /*String s, String type*/
    public List<Book> getBooksByString() {
//        final String s_type = (SearchType.valueOf(type.toUpperCase()) == SearchType.AUTHOR ? "a.full_name" : "b.name");
        final String s_type = String.valueOf(search.getSearchType());
        final String s = getRequestParameters().get("search_atr");
        return getBooksFromDB(
                "select b.id, b.name, b.page_count, b.isbn, b.publish_year, b.description, "
                        + "g.name as genre, a.full_name as author, p.name as publisher from book b "
                        + "inner join genre g on b.genre_id=g.id "
                        + "inner join author a on b.author_id=a.id "
                        + "inner join publisher p on b.publisher_id=p.id "
                        + "where " + s_type + " like '%" + s + "%'"
                        + " order by b.name"
        );
    }
}
