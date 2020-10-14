package com.kirilo.servlets;

import com.kirilo.db.Database;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

// http://javaonlineguide.net/2015/04/reading-pdf-file-binary-data-stored-in-mysql-database-using-servlet-display-in-browser.html
@WebServlet(name = "readPdf", urlPatterns = "/readPdf")
public class FileReadPdf extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id") != null ? req.getParameter("id") : "NA";

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "inline; filename=" + id + ".pdf");

        try (Statement statement = Database.getConnection().createStatement();
             final ResultSet resultSet = statement.executeQuery("select content from book where id=" + id)
        ) {
            while (resultSet.next()) {
                resp.getOutputStream().write(resultSet.getBytes("content"));
            }
        } catch (SQLException throwables) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't load book content from DB id=" + id, throwables);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
