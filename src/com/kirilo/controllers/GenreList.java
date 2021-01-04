package com.kirilo.controllers;

import com.kirilo.entities.Genre;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(eager = false)
@SessionScoped
public class GenreList implements Serializable {
    private static final long serialVersionUID = 7698775746559496410L;

    private final List<Genre> genres;
    @ManagedProperty(value = "#{bookController}")
    private BookController controller;

    public GenreList() {
        genres = new ArrayList<>();
//        genres.addAll(getGenres());
//        genres.addAll(getGenresFromDB());
    }

    public BookController getController() {
        return controller;
    }

    public void setController(BookController controller) {
        this.controller = controller;
    }

    public List<Genre> getGenres() {
        if (genres.isEmpty()) {
            genres.addAll(getGenresFromDB());
        }
        return genres;
    }

    private List<Genre> getGenresFromDB() {
        return controller.getGenreList();
    }

/*    private List<Genre> getGenresFromDB() {
        final List<Genre> genres;
        try (
//                Connection connection = Database.getConnection();
                Statement statement = (Database.getConnection()).createStatement();
                ResultSet resultSet = statement.executeQuery("select * from genre order by name")
        ) {
            while (resultSet.next()) {
                final Genre genre = new Genre();
                genre.setName(resultSet.getString("name"));
                genre.setId(resultSet.getInt("id"));
                genres.add(genre);
            }

        } catch (SQLException throwables) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't get genres from DB!", throwables);
        }

        return genres;
    }*/
}
