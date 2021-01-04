package com.kirilo.db;

import com.kirilo.beans.repositories.BookRepository;
import com.kirilo.entities.Author;
import com.kirilo.entities.Book;
import com.kirilo.entities.Genre;
import com.kirilo.entities.Publisher;
import com.kirilo.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "dataHelper", eager = false)
@SessionScoped
public class DataHelper implements BookRepository, Serializable {
    private static final long serialVersionUID = 8210136158520619911L;
    private final EntityManagerFactory emf;
    private final CriteriaBuilder cb;

    public DataHelper() {
        emf = Persistence.createEntityManagerFactory("Book");
        cb = emf.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
    }

    @Override
    public List<Book> getAllBooks() {
        return getBooks(0, Integer.MAX_VALUE);
    }

    @Override
    public List<Book> getBooks(int first, int items) {
        final CriteriaQuery<Book> q = cb.createQuery(Book.class);
        final Root<Book> book = q.from(Book.class);
        q.orderBy(cb.desc(book.get("name")));

        return getBooksImageAuthorPublisherByPage(q, first, items);
    }

    @Override
    public List<Book> getBooksByGenre(int id, int first, int items) {
        final CriteriaQuery<Book> q = cb.createQuery(Book.class);
        final Root<Book> book = q.from(Book.class);

        q.where(getEqualGenreId(book, id));
        q.orderBy(cb.desc(book.get("name")));

        return getBooksImageAuthorPublisherByPage(q,first, items);
    }

    @Override
    public List<Book> getBooksByGenre(int id) {
        return getBooksByGenre(id, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Book> getBooksByString(SearchType searchType, String searchString, int first, int items) {
        final CriteriaQuery<Book> q = cb.createQuery(Book.class);
        final Root<Book> book = q.from(Book.class);
        q.where(getLikeType(searchType, searchString, book));
        return getBooksImageAuthorPublisherByPage(q,first, items);
    }

    @Override
    public List<Book> getBooksByString(SearchType searchType, String s_string) {
        return getBooksByString(searchType, s_string, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Book> getBooksByLetter(String ch, int first, int items) {
        final CriteriaQuery<Book> q = cb.createQuery(Book.class);
        final Root<Book> book = q.from(Book.class);
        q.where(getLikeBookName(ch, book));
        return getBooksImageAuthorPublisherByPage(q,first, items);
    }

    @Override
    public List<Book> getBooksByLetter(String ch) {
        return getBooksByLetter(ch, 0, Integer.MAX_VALUE);
    }

    private Predicate getEqualGenreId(Root<?> root, int id) {
        return cb.equal(root.get("genre").get("id"), id);
    }

    private Predicate getLikeBookName(String ch, Root<Book> book) {
        return cb.like(book.get("name"), ch.toLowerCase() + "%");
    }

    private Predicate getLikeType(SearchType searchType, String s_string, Root<Book> book) {
        return cb.like(
                searchType == SearchType.AUTHOR
                        ? book.get("author").get("fullName")
                        : book.get("name"),
                "%" + s_string.toLowerCase() + "%");
    }

    private List<Book> getBookList(CriteriaQuery<Book> q) {
        final EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        final List<Book> resultList = em.createQuery(q).getResultList();
        em.getTransaction().commit();
        return resultList;
    }

    private List<Book> getBookAuthorPublisherGenre(CriteriaQuery<Book> query) {
        return getBookListEager(query, "book-author-genre-publisher");
    }

    private List<Book> getBooksAuthorPublisher(CriteriaQuery<Book> query) {
        return getBookListEager(query, "book-author-publisher");
    }

    private List<Book> getBooksImageAuthorPublisher(CriteriaQuery<Book> query) {
        return getBookListEager(query, "book.image-author-publisher");
    }

    private List<Book> getBooksImageAuthorPublisherByPage(CriteriaQuery<Book> query, int first, int items) {
        return getBookListEager(query, "book.image-author-publisher", first, items);
    }

    private List<Book> getBookListEager(CriteriaQuery<Book> q, String entity_Graph) {
        return getBookListEager(q, entity_Graph, 0, Integer.MAX_VALUE);
    }

    private List<Book> getBookListEager(CriteriaQuery<Book> q, String entity_Graph, int first, int items) {
        final EntityManager em = emf.createEntityManager();
        final EntityGraph<?> entityGraph = em.getEntityGraph(entity_Graph);
        em.getTransaction().begin();

        final TypedQuery<Book> query = em.createQuery(q)
                .setHint("javax.persistence.loadgraph", entityGraph)
                .setMaxResults(items)
                .setFirstResult(first);

        final List<Book> resultList = query.getResultList();
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "list of Books: " + resultList.size());
        em.getTransaction().commit();
        return resultList;
    }

    @Override
    public List<Book> getBooksFromCurrentPage() {
        return null;
    }


    @Override
    public void updateBooks(List<Book> books) {
        final EntityManager em = emf.createEntityManager();
        final CriteriaUpdate<Book> update = cb.createCriteriaUpdate(Book.class);
        final Root<Book> b = update.from(Book.class);

        final CriteriaUpdate<Author> upAuthor = cb.createCriteriaUpdate(Author.class);
        final Root<Author> a = upAuthor.from(Author.class);

        final CriteriaUpdate<Publisher> upPublisher = cb.createCriteriaUpdate(Publisher.class);
        final Root<Publisher> p = upPublisher.from(Publisher.class);

        for (Book book : books) {
            if (!book.isEdit()) {
                continue;
            }
            em.getTransaction().begin();
/*
//            https://stackoverflow.com/questions/39846369/save-entity-using-entitymanager-not-working
//            em.joinTransaction();
            em.persist(book);
            */
            update
                    .set(b.get("name"), book.getName())
                    .set(b.get("pageCount"), book.getPageCount())
                    .set(b.get("isbn"), book.getIsbn())
                    .set(b.get("publishYear"), book.getPublishYear())
                    .where(cb.equal(b.get("id"), book.getId()))
//                    .set(b.get("description"), book.getDescription())
//                    .set(b.get("rating"), book.getVoteCount())
            ;
            Query q = em.createQuery(update);
            q.executeUpdate();

            upAuthor
                    .set(a.get("fullName"), book.getAuthor().getFullName())
                    .where(cb.equal(a.get("id"), book.getAuthor().getId()))
            ;

            q = em.createQuery(upAuthor);
            q.executeUpdate();

            upPublisher
                    .set(p.get("name"), book.getPublisher().getName())
                    .where(cb.equal(b.get("id"), book.getPublisher().getId()))
            ;

            q = em.createQuery(upPublisher);
            q.executeUpdate();

            em.getTransaction().commit();
        }
    }

    @Override
    public List<Book> getBookListFromPage(int number) {
        return null;
    }

    @Override
    public List<Genre> getAllGenres() {
        final CriteriaQuery<Genre> q = cb.createQuery(Genre.class);
        final Root<Genre> genre = q.from(Genre.class);
        q.select(genre);
        final EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        final List<Genre> resultList = em.createQuery(q).getResultList();
        em.getTransaction().commit();
        return resultList;
    }

    public List<byte[]> getImage(Long... id) {
        return getObjectFromField("image", id);
    }

    public List<byte[]> getContent(Long... id) {
        return getObjectFromField("content", id);
    }

    @Override
    public Book getBook(Long id) {
        CriteriaQuery<Book> q = cb.createQuery(Book.class);
        Root<Book> book = q.from(Book.class);
        q.where(cb.equal(book.get("id"), id));
        final EntityManager em = emf.createEntityManager();
        final EntityGraph<?> entityGraph = em.getEntityGraph("book.image-author-publisher");
        em.getTransaction().begin();
        final TypedQuery<Book> query = em.createQuery(q)
                .setHint("javax.persistence.loadgraph", entityGraph);
        final Book result = query.getSingleResult();
        em.getTransaction().commit();
        return result;
    }

    @Override
    public Long getQuantityOfBooksByLetter(String ch) {
        final CriteriaQuery<Long> q = cb.createQuery(Long.class);
        final Root<Book> book = q.from(Book.class);
        return getQuantityOfBooksByPredicate(q, book, getLikeBookName(ch, book));
    }

    @Override
    public Long getQuantityOfBooks() {
        final CriteriaQuery<Long> q = cb.createQuery(Long.class);
        final Root<Book> book = q.from(Book.class);
        return getQuantityOfBooksByPredicate(q, book);
    }

    @Override
    public Long getQuantityOfBooksByGenre(int id) {
        final CriteriaQuery<Long> q = cb.createQuery(Long.class);
        final Root<Book> book = q.from(Book.class);

        return getQuantityOfBooksByPredicate(q, book, getEqualGenreId(book, id));
    }

    @Override
    public Long getQuantityOfBooksByString(SearchType searchType, String sString) {
        final CriteriaQuery<Long> q = cb.createQuery(Long.class);
        final Root<Book> book = q.from(Book.class);

        return getQuantityOfBooksByPredicate(q, book, getLikeType(searchType, sString, book));
    }

    private Long getQuantityOfBooksByPredicate(CriteriaQuery<Long> q, Root<?> root) {
        return getQuantityOfBooksByPredicate(q, root, cb.conjunction());
    }

    private Long getQuantityOfBooksByPredicate(CriteriaQuery<Long> q, Root<?> root, Predicate predicate) {
        final EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        q.select(cb.count(root.get("id")));
        q.where(predicate);
        final Long result = em.createQuery(q).getSingleResult();
        em.getTransaction().commit();
        return result;
    }

    private List<Book> getBooksFromTo(int first, int items, CriteriaQuery<Book> q) {
        final EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        final List<Book> resultList = em.createQuery(q)
                .setMaxResults(items)
                .setFirstResult(first)
                .getResultList();
        em.getTransaction().commit();
        return resultList;
    }

    private List<byte[]> getObjectFromField(String field, Long... ids) {
        final EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        CriteriaQuery<byte[]> q = cb.createQuery(byte[].class);
        Root<Book> book = q.from(Book.class);
        final CriteriaBuilder.In<Long> inClause = cb.in(book.get("id"));
        q.select(book.get(field)).where(inClause);
        for (Long id : ids) {
            inClause.value(id);
        }
        final List<byte[]> resultList = em.createQuery(q).getResultList();

        em.getTransaction().commit();
        return resultList;
    }
}