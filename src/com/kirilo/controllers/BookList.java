package com.kirilo.controllers;

import com.kirilo.beans.Book;
import com.kirilo.db.Database;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
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
    private static final long serialVersionUID = 1353727775955418805L;
    @ManagedProperty(value = "#{search}")
    private Search search;
    private List<Book> books;

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
        if (books.isEmpty()) {
//            books = getAllBooks();
        }
        return books;
    }

    public List<Book> getAllBooks() {
        return getBooksFromDB("select b.id, b.name, b.page_count, b.isbn, b.publish_year, b.description, "
                + "g.name as genre, a.full_name as author, p.name as publisher from book b "
                + "inner join genre g on b.genre_id=g.id "
                + "inner join author a on b.author_id=a.id "
                + "inner join publisher p on b.publisher_id=p.id "
                + " order by b.name"
        );
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

    public List<Book> getBookByGenre() {
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
    }


    public List<Book> getBooksByLetter() {
        String ch = getRequestParameters().get("letter_id").substring(0, 1).toLowerCase();
        return getBooksFromDB(
                "select b.id, b.name, b.page_count, b.isbn, b.publish_year, b.description, "
                        + "g.name as genre, a.full_name as author, p.name as publisher from book b "
                        + "inner join genre g on b.genre_id=g.id "
                        + "inner join author a on b.author_id=a.id "
                        + "inner join publisher p on b.publisher_id=p.id "
                        + "where b.name like '" + ch + "%'"
                        + " order by b.name"
        );
    }

    public List<Book> getBooksByString() {
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
