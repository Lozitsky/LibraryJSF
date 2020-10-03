package com.kirilo.controllers;

import com.kirilo.beans.Genre;
import com.kirilo.db.Database;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(eager = true)
@ApplicationScoped
public class GenreList {
    private final List<Genre> genres;

    public GenreList() {
        genres = new ArrayList<>();
//        genres.addAll(getGenres());
//        genres.addAll(getGenresFromDB());
    }

    public List<Genre> getGenres() {
        if (genres.isEmpty()) {
            return getGenresFromDB();
        }
        return genres;
    }

    private List<Genre> getGenresFromDB() {
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
    }
}
