package com.kirilo.entities;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Book {
    private boolean edit;
    private long id;
    private String name;
    private byte[] content;
    private int pageCount;
    private String isbn;
    private long genreId;
    private long authorId;
    private int publishYear;
    private long publisherId;
    private byte[] image;
    private String description;
    private Integer rating;
    private Long voteCount;

    @Transient
    public boolean isEdit() {
        return edit;
    }
    @Transient
    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "content")
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Basic
    @Column(name = "page_count")
    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Basic
    @Column(name = "isbn")
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Basic
    @Column(name = "genre_id")
    public long getGenreId() {
        return genreId;
    }

    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }

    @Basic
    @Column(name = "author_id")
    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    @Basic
    @Column(name = "publish_year")
    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    @Basic
    @Column(name = "publisher_id")
    public long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(long publisherId) {
        this.publisherId = publisherId;
    }

    @Basic
    @Column(name = "image")
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "rating")
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Basic
    @Column(name = "vote_count")
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
        return id == book.id &&
                pageCount == book.pageCount &&
                genreId == book.genreId &&
                authorId == book.authorId &&
                publishYear == book.publishYear &&
                publisherId == book.publisherId &&
                Objects.equals(name, book.name) &&
                Arrays.equals(content, book.content) &&
                Objects.equals(isbn, book.isbn) &&
                Arrays.equals(image, book.image) &&
                Objects.equals(description, book.description) &&
                Objects.equals(rating, book.rating) &&
                Objects.equals(voteCount, book.voteCount);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, pageCount, isbn, genreId, authorId, publishYear, publisherId, description, rating, voteCount);
        result = 31 * result + Arrays.hashCode(content);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }
}
