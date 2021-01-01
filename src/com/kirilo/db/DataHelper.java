package com.kirilo.db;

import com.kirilo.entities.Genre;
import com.kirilo.beans.repositories.BookRepository;
import com.kirilo.entities.Author;
import com.kirilo.entities.Book;
import com.kirilo.entities.Publisher;
import com.kirilo.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "dataHelper", eager = true)
@SessionScoped
public class DataHelper implements BookRepository, Serializable {
    private static final long serialVersionUID = 8210136158520619911L;
    private final EntityManagerFactory emf;
    //    private final EntityManager em;
    private final CriteriaBuilder cb;

    public DataHelper() {
        emf = Persistence.createEntityManagerFactory("Book");
//        em = emf.createEntityManager();
        cb = emf.getCriteriaBuilder();
    }

    @Override
    public List<Book> getAllBooks() {
        CriteriaQuery<Book> q = cb.createQuery(Book.class);
        Root<Book> book = q.from(Book.class);
        /*return (List<Book>)em.createQuery("select b from Book b left join fetch b.author a left join fetch b.genre g left join fetch b.publisher p " +
                "where b.author.id = a.id and b.publisher.id = p.id and b.genre.id = g.id order by b.name").getResultList();
        */
/*        Fetch<List<Book>, Author> author = book.fetch("author", JoinType.LEFT);
        Fetch<Book, Genre> genre = book.fetch("genre", JoinType.LEFT);
        Fetch<Book, Publisher> publisher = book.fetch("publisher", JoinType.LEFT);*/

        q.select(book);
        return getBookAuthorPublisher(q);
    }

    @Override
    public List<Book> getBooksByGenre(int id) {
        CriteriaQuery<Book> q = cb.createQuery(Book.class);
        Root<Book> book = q.from(Book.class);

        q.where(cb.equal(book.get("genre").get("id"), id));
        return getBookAuthorPublisher(q);
    }

    @Override
    public List<Book> getBooksByLetter(String ch) {
        CriteriaQuery<Book> q = cb.createQuery(Book.class);
        Root<Book> book = q.from(Book.class);

        q.where(cb.like(book.get("name"), ch.toLowerCase() + "%"));
        return getBookAuthorPublisher(q);
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
        q.where(cb.like(expression, "%" + s_string.toLowerCase() + "%"));

        return getBookAuthorPublisher(q);
    }

    private List<Book> getBookList(CriteriaQuery<Book> q) {
        final EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        final List<Book> resultList = em.createQuery(q).getResultList();
        em.getTransaction().commit();
        em.close();
        return resultList;
    }

    private List<Book> getBookAuthorPublisherGenre(CriteriaQuery<Book> query) {
        return getBookListEager(query, "book-author-genre-publisher");
    }

    private List<Book> getBookAuthorPublisher(CriteriaQuery<Book> query) {
        return getBookListEager(query, "book-author-publisher");
    }

    private List<Book> getBookListEager(CriteriaQuery<Book> q, String entity_Graph) {
        final EntityManager em = emf.createEntityManager();
        final EntityGraph<?> entityGraph = em.getEntityGraph(entity_Graph);
        em.getTransaction().begin();
        final TypedQuery<Book> query = em.createQuery(q)
                .setHint("javax.persistence.loadgraph", entityGraph);
        final List<Book> resultList = query.getResultList();
        em.getTransaction().commit();
        em.close();
        return resultList;
    }

    @Override
    public List<Book> getBooksFromSelectedPage() {
        return null;
    }

    @Override
    public void updateBooks(List<Book> books) {
        final EntityManager em = emf.createEntityManager();
        final CriteriaUpdate<Book> update = cb.createCriteriaUpdate(Book.class);
        final Root<Book> b = update.from(Book.class);

        final CriteriaUpdate<Author> upAuthor = cb.createCriteriaUpdate(Author.class);
        final Root<Author> a = upAuthor.from(Author.class);

//        final CriteriaUpdate<Genre> upGenre = cb.createCriteriaUpdate(Genre.class);
//        final Root<Genre> g = upGenre.from(Genre.class);

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
        em.close();
    }

    @Override
    public List<Book> getBookListFromPage(int number) {
        return null;
    }

    @Override
    public List<Genre> getAllGenres() {
//        em.createQuery.("select g from Genre g")
        final CriteriaQuery<Genre> q = cb.createQuery(Genre.class);
        final Root<Genre> genre = q.from(Genre.class);
        q.select(genre);
        final EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        final List<Genre> resultList = em.createQuery(q).getResultList();
        em.getTransaction().commit();
        em.close();
        return resultList;
    }

    public byte[] getImage(Long id) {
        return getObjectFromField("image", id);
    }

    public byte[] getContent(Long id) {
        return getObjectFromField("content", id);
    }

    private byte[] getObjectFromField(String field, Long id) {
        final EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        CriteriaQuery<byte[]> q = cb.createQuery(byte[].class);
        Root<Book> book = q.from(Book.class);
        q.select(book.get(field)).where(cb.equal(book.get("id"), id));
        byte[] singleResult = em.createQuery(q).getSingleResult();

/*        byte[] singleResult = em.createQuery(
                "select b.image from Book b where b.id = :id", byte[].class)
                .setParameter("id", id)
                .getSingleResult();*/


/*        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Is null? " + (singleResult == null) + "\n" + id +
                "\n" + (singleResult != null ? "not null" : null));*/
        em.getTransaction().commit();
        em.close();
        return singleResult;
    }
}