package com.kirilo.entities;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

// https://ci.apache.org/projects/openjpa/2.2.x/docbook/jpa_overview_meta_field.html/
// https://www.baeldung.com/jpa-basic-annotation#why-use
// https://www.baeldung.com/jpa-entity-graph
@NamedEntityGraph(
        name = "book-author-genre-publisher",
        attributeNodes = {
                @NamedAttributeNode("author"),
                @NamedAttributeNode("publisher"),
                @NamedAttributeNode("genre"),
        }
)
@NamedEntityGraph(
        name = "book-author-publisher",
        attributeNodes = {
                @NamedAttributeNode("author"),
                @NamedAttributeNode("publisher"),
        }
)
@NamedEntityGraph(
        name = "book.image-author-publisher",
        attributeNodes = {
                @NamedAttributeNode("author"),
                @NamedAttributeNode("publisher"),
                @NamedAttributeNode("image"),
        }
)
@NamedEntityGraph(
        name = "book.image",
        attributeNodes = {
                @NamedAttributeNode("image"),
        }
)

@Entity
@Access(AccessType.FIELD)
public class Book {
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Lob
    @Basic(optional = false, fetch = FetchType.LAZY)
    @Column(name = "content")
    private byte[] content;
    @Basic
    @Column(name = "page_count")
    private Integer pageCount;
    @Basic
    @Column(name = "isbn")
    private String isbn;
    @Basic
    @Column(name = "publish_year")
    private Integer publishYear;
    @Lob
    @Basic(optional = false, fetch = FetchType.LAZY)
    @Column(name = "image")
    private byte[] image;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "rating")
    private Integer rating;
    @Transient
    private Long voteCount;

    @Transient
    private boolean edit;
    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;
    @ManyToOne(fetch = FetchType.LAZY)
    private Publisher publisher;
    @ManyToOne(fetch = FetchType.LAZY)
    private Genre genre;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Basic
    @Column(name = "vote_count")
    @Access(AccessType.PROPERTY)
    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(name, book.name) && Arrays.equals(content, book.content) && Objects.equals(pageCount, book.pageCount) && Objects.equals(isbn, book.isbn) && Objects.equals(publishYear, book.publishYear) && Arrays.equals(image, book.image) && Objects.equals(description, book.description) && Objects.equals(rating, book.rating) && Objects.equals(voteCount, book.voteCount);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, pageCount, isbn, publishYear, description, rating, voteCount);
        result = 31 * result + Arrays.hashCode(content);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
}
