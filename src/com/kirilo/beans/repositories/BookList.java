package com.kirilo.beans.repositories;

import com.kirilo.beans.Book;
import com.kirilo.controllers.Search;
import com.kirilo.controllers.SearchTypeChanger;
import com.kirilo.db.Database;
import com.kirilo.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "bookList", eager = true)
@SessionScoped
public class BookList implements Serializable, BookRepository {
    public static final String SELECT_ALL_FIELDS = "select b.id, b.name, b.page_count, b.isbn, b.publish_year, b.description, "
            + "g.name as genre, a.full_name as author, p.name as publisher from book b "
            + "inner join genre g on b.genre_id=g.id "
            + "inner join author a on b.author_id=a.id "
            + "inner join publisher p on b.publisher_id=p.id";
    private static final long serialVersionUID = 1353727775955418805L;
    private final List<Book> books;
    private String currentSQL;

    @ManagedProperty(value = "#{searchTypeChanger}")
    private SearchTypeChanger searchTypeChanger;

    @ManagedProperty(value = "#{search}")
    private Search search;

    public BookList() {
        books = new ArrayList<>();
    }

    public SearchTypeChanger getSearchTypeChanger() {
        return searchTypeChanger;
    }

    public void setSearchTypeChanger(SearchTypeChanger searchTypeChanger) {
        this.searchTypeChanger = searchTypeChanger;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public List<Book> getCurrentBooks() {
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, String.format("Can't get books from DB!\n%s", query), throwables);
        }
        return books;
    }

    private int getBooksCount(String query) {
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

        final int count = getBooksCount(String.format(
                "select count(*) from book b %s%s",
                searchTypeChanger.getSearchType() == SearchType.AUTHOR ? "inner join author a on b.author_id=a.id " : "", sql
        ));
        int pageCount = count / search.getBooksOnPage() + (count % search.getBooksOnPage() > 0 ? 1 : 0);

        if (pageCount > 1) {
            if (pageCount != search.getListPageNumbers().size()) {
                final ArrayList<Integer> list = new ArrayList<>();
                for (int i = 0; i < pageCount; i++) {
                    list.add(i + 1);
                }
                search.setListPageNumbers(list);
            }
            query = String.format(
                    "%s limit %d, %d",
                    query, (search.getSelectedPage() - 1) * search.getBooksOnPage(), search.getBooksOnPage()
            );
        }

        if (sql.equals(currentSQL)) {
            if (search.getTotalBooksCount() <= search.getSelectedPage()) {
                return books;
            }
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "sql == search.getCurrentSQL()" + query + "\n" + search.getSelectedPage());
        } else {
            currentSQL = sql;
            search.setTotalBooksCount(count);
            search.setSelectedPage(1);
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "pagecount == " + pageCount + "\n" + query + "\n" + search.getSelectedPage());
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Count of pages: " + search.getListPageNumbers());
        return getBooksFromDB(query);
    }

    public List<Book> getAllBooks() {
        String sql = "";
        return checkAndGetBooks(sql);
    }

    public List<Book> getBookByGenre(int id) {
        final String sql = String.format("where genre_id=%d", id);
        return checkAndGetBooks(sql);
    }

    public List<Book> getBooksByLetter(String ch) {
        final String sql = String.format("where b.name like '%s%%'", ch.toLowerCase());
        return checkAndGetBooks(sql);
    }

    public List<Book> getBooksByString(String s_type, String s_string) {
        final String sql = String.format("where %s like '%%%s%%'", s_type, s_string);
        return checkAndGetBooks(sql);
    }

    public List<Book> getBooksFromSelectedPage() {
        return checkAndGetBooks(currentSQL);
    }

    public void setNumberOfBooksPerPage(int number) {
        search.booksOnPageChanged(number);
        checkAndGetBooks(currentSQL);
    }

    public void updateBooks() {
        try {
            try (PreparedStatement statement = Database.getConnection().prepareStatement(
                    "update book set name=?, isbn=?,  page_count=?, publish_year=?, description=? where id=?"
            )) {
                for (Book book : books) {
                    if (!book.isEdit()) {
                        continue;
                    }
                    statement.setString(1, book.getName());
                    statement.setString(2, book.getIsbn());
                    statement.setInt(3, book.getPageCount());
                    statement.setInt(4, book.getPublishYear());
                    statement.setString(5, book.getDescription());
                    statement.setInt(6, book.getId());
                    statement.addBatch();
                }
                statement.executeBatch();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
//        return "books";
    }

/*    public void resetMode() {
        books.forEach(book -> book.setEdit(false));
    }*/
}
