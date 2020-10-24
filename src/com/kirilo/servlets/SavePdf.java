package com.kirilo.servlets;

import com.kirilo.db.Database;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

// http://javaonlineguide.net/2015/04/reading-pdf-file-binary-data-stored-in-mysql-database-using-servlet-display-in-browser.html

@WebServlet(name = "saveOrReadPdf", urlPatterns = "/filePdf")
public class SavePdf extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String id = httpServletRequest.getParameter("id") != null ? httpServletRequest.getParameter("id") : "NA";

        String filename = httpServletRequest.getParameter("filename");
        String header = filename != null && !filename.isEmpty() ? "attachment; filename=" + URLEncoder.encode(filename, "UTF-8") : "inline; filename=" + id;

        httpServletResponse.setContentType("application/pdf");
        httpServletResponse.setHeader("Content-Disposition", header + ".pdf");

        try (Statement statement = Database.getConnection().createStatement();
             final ResultSet resultSet = statement.executeQuery("select content from book where id=" + id)
        ) {
            while (resultSet.next()) {
                httpServletResponse.getOutputStream().write(resultSet.getBytes("content"));
            }
        } catch (SQLException throwables) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't load book content from DB id=" + id, throwables);
        }
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doPost(httpServletRequest, httpServletResponse);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
