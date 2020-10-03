package com.kirilo.servlets;

import com.kirilo.db.Database;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

// https://stackoverflow.com/a/2341322/9586230
public class ShowImage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/jpeg");
        String id = req.getParameter("id");
        try (Statement statement = (Database.getConnection()).createStatement();
             ResultSet resultSet = statement.executeQuery("select image from book where id=" + id)) {
            while (resultSet.next()) {
                resp.getOutputStream().write(resultSet.getBytes("image"));
            }
        } catch (SQLException throwables) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't load book picture from DB id=" + id, throwables);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
