package com.kirilo.beans.repositories;

import com.kirilo.controllers.Pager;
import com.kirilo.controllers.SearchTypeChanger;
import com.kirilo.db.Database;
import com.kirilo.entities.Author;
import com.kirilo.entities.Book;
import com.kirilo.entities.Genre;
import com.kirilo.entities.Publisher;
import com.kirilo.enums.SearchType;

import javax.faces.bean.ManagedProperty;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*@ManagedBean(name = "bookList", eager = false)
@SessionScoped*/
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
    private Pager pager;

    public BookList() {
        books = new ArrayList<>();
    }

    public SearchTypeChanger getSearchTypeChanger() {
        return searchTypeChanger;
    }

    public void setSearchTypeChanger(SearchTypeChanger searchTypeChanger) {
        this.searchTypeChanger = searchTypeChanger;
    }

    public Pager getSearch() {
        return pager;
    }

    public void setSearch(Pager pager) {
        this.pager = pager;
    }

//    public List<Book> getCurrentBooks() {
//        return books;
//    }

    private List<Book> getBooksFromDB(String query) {
        books.clear();

        try (
                Statement statement = (Database.getConnection()).createStatement();
                ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                final Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setName(resultSet.getString("name"));
                book.setPageCount(resultSet.getInt("page_count"));
                book.setIsbn(resultSet.getString("isbn"));
                final Genre genre = new Genre();
                genre.setName(resultSet.getString("genre"));
                book.setGenre(genre);
                final Author author = new Author();
                author.setFullName(resultSet.getString("author"));
                book.setAuthor(author);
                book.setPublishYear(resultSet.getInt("publish_year"));
                final Publisher publisher = new Publisher();
                publisher.setName(resultSet.getString("publisher"));
                book.setPublisher(publisher);
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

        final long count = getBooksCount(String.format(
                "select count(*) from book b %s%s",
                searchTypeChanger.getSearchType() == SearchType.AUTHOR ? "inner join author a on b.author_id=a.id " : "", sql
        ));
        long pageCount = count / pager.getBooksOnPage() + (count % pager.getBooksOnPage() > 0 ? 1 : 0);

        if (pageCount > 1) {
            if (pageCount != pager.getListPageNumbers().size()) {
                final ArrayList<Integer> list = new ArrayList<>();
                for (int i = 0; i < pageCount; i++) {
                    list.add(i + 1);
                }
                pager.setListPageNumbers(list);
            }
            query = String.format(
                    "%s limit %d, %d",
                    query, (pager.getSelectedPage() - 1) * pager.getBooksOnPage(), pager.getBooksOnPage()
            );
        }

        if (sql.equals(currentSQL)) {
            if (pager.getTotalQuantityOfBooks() <= pager.getSelectedPage()) {
                return books;
            }
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "sql == search.getCurrentSQL()" + query + "\n" + pager.getSelectedPage());
        } else {
            currentSQL = sql;
            pager.setTotalQuantityOfBooks(count);
            pager.setSelectedPage(1);
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "pagecount == " + pageCount + "\n" + query + "\n" + pager.getSelectedPage());
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Count of pages: " + pager.getListPageNumbers());
        return getBooksFromDB(query);
    }

    public List<Book> getAllBooks() {
        String sql = "";
        return checkAndGetBooks(sql);
    }

    public List<Book> getBooksByGenre(int id) {
        final String sql = String.format("where genre_id=%d", id);
        return checkAndGetBooks(sql);
    }

    public List<Book> getBooksByLetter(String ch) {
        final String sql = String.format("where b.name like '%s%%'", ch.toLowerCase());
        return checkAndGetBooks(sql);
    }

    public List<Book> getBooksByString(SearchType searchType, String s_string) {
//        final SearchType searchType = searchTypeChanger.getSearchType();
        String s_type = (searchType == SearchType.AUTHOR ? "a.full_name" : "b.name");

        final String sql = String.format("where %s like '%%%s%%'", s_type, s_string);
        return checkAndGetBooks(sql);
    }

    public List<Book> getBooksFromCurrentPage() {
        return checkAndGetBooks(currentSQL);
    }

    public List<Book> getBookListFromPage(int number) {
//        search.booksOnPageChanged(number);
        return checkAndGetBooks(currentSQL);
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public List<byte[]> getImage(Long... collect) {
        return null;
    }

    @Override
    public List<byte[]> getContent(Long... array) {
        return null;
    }

    @Override
    public Book getBook(Long value) {
        return null;
    }

    @Override
    public Long getQuantityOfBooks() {
        return null;
    }

    @Override
    public List<Book> getBooks(int first, int items) {
        return null;
    }

    @Override
    public List<Book> getBooksByGenre(int id, int first, int items) {
        return null;
    }

    @Override
    public List<Book> getBooksByString(SearchType searchType, String searchString, int first, int items) {
        return null;
    }

    @Override
    public List<Book> getBooksByLetter(String ch, int first, int items) {
        return null;
    }

    @Override
    public Long getQuantityOfBooksByLetter(String ch) {
        return null;
    }

    @Override
    public Long getQuantityOfBooksByGenre(int id) {
        return null;
    }

    @Override
    public Long getQuantityOfBooksByString(SearchType searchType, String sString) {
        return null;
    }

    public void updateBooks(List<Book> books) {
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
                    statement.setLong(6, book.getId());
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
