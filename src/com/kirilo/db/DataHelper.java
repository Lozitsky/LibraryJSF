package com.kirilo.db;

import com.kirilo.beans.repositories.BookRepository;
import com.kirilo.entities.Author;
import com.kirilo.entities.Book;
import com.kirilo.entities.Genre;
import com.kirilo.entities.Publisher;
import com.kirilo.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/*@Stateless
@PersistenceContext(name = "jdbc/Library")*/
@ManagedBean(name = "dataHelper", eager = true)
@SessionScoped
public class DataHelper implements BookRepository, Serializable {
    private static final long serialVersionUID = 8210136158520619911L;
    private final List<Book> books;
    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final CriteriaBuilder cb;

    public DataHelper() {
        books = new ArrayList<>();

        emf = Persistence.createEntityManagerFactory("Book");
        em = emf.createEntityManager();
        cb = emf.getCriteriaBuilder();
    }

    @Override
    public List<Book> getCurrentBooks() {
        return books;
    }

    @Override
    public List<Book> getAllBooks() {
        books.clear();

        CriteriaQuery<Book> q = cb.createQuery(Book.class);
        Root<Book> book = q.from(Book.class);
        /*return (List<Book>)em.createQuery("select b from Book b left join fetch b.author a left join fetch b.genre g left join fetch b.publisher p " +
                "where b.author.id = a.id and b.publisher.id = p.id and b.genre.id = g.id order by b.name").getResultList();
        */
        Join<List<Book>, Author> author = book.join("author", JoinType.LEFT);
        Join<Book, Genre> genre = book.join("genre", JoinType.LEFT);
        Join<Book, Publisher> publisher = book.join("publisher", JoinType.LEFT);

        q.select(book);
        books.addAll(em.createQuery(q).getResultList());
        return books;

    }

    @Override
    public List<Book> getBooksByGenre(int id) {
        books.clear();
        CriteriaQuery<Book> q = cb.createQuery(Book.class);
        Root<Book> book = q.from(Book.class);

        q.where(cb.equal(book.get("genre").get("id"), id));
        books.addAll(em.createQuery(q).getResultList());
        return books;
    }

    @Override
    public List<Book> getBooksByLetter(String ch) {
        CriteriaQuery<Book> q = cb.createQuery(Book.class);
        Root<Book> book = q.from(Book.class);
        q.where(cb.like(book.get("name"), ch.toLowerCase() + "%"));
        books.addAll(em.createQuery(q).getResultList());
        return books;
    }

    @Override
    public List<Book> getBooksByString(SearchType searchType, String s_string) {

        CriteriaQuery<Book> q = cb.createQuery(Book.class);
        Root<Book> book = q.from(Book.class);
        Path<String> expression;

        switch (searchType) {
            case AUTHOR:
                expression = book.get("author").get("fullName");
                break;
            default:
                expression = book.get("name");
                break;
        }
        q.where(cb.like(expression, "%" + s_string + "%"));

        return changeAndGetBooks(q);
    }

    private List<Book> changeAndGetBooks(CriteriaQuery<Book> q) {
        books.clear();
        books.addAll(em.createQuery(q).getResultList());
        return books;
    }

    @Override
    public List<Book> getBooksFromSelectedPage() {
        return null;
    }

    @Override
    public void updateBooks() {

        for (Book book : books) {
            if (!book.isEdit()) {
                continue;
            }

            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
        }
    }

    @Override
    public void setNumberOfBooksPerPage(int number) {
        if (books.isEmpty()) {
            getAllBooks();
        }
    }
}