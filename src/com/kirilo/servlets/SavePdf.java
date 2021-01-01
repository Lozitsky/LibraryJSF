package com.kirilo.servlets;

import com.kirilo.db.DataHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

// http://javaonlineguide.net/2015/04/reading-pdf-file-binary-data-stored-in-mysql-database-using-servlet-display-in-browser.html

@WebServlet(name = "saveOrReadPdf", urlPatterns = "/filePdf")
public class SavePdf extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id") != null ? req.getParameter("id") : "NA";

        String filename = req.getParameter("filename");
        String header = filename != null && !filename.isEmpty() ? "attachment; filename=" + URLEncoder.encode(filename, "UTF-8") : "inline; filename=" + id;

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", header + ".pdf");

/*        try (Statement statement = Database.getConnection().createStatement();
             final ResultSet resultSet = statement.executeQuery("select content from book where id=" + id)
        ) {
            while (resultSet.next()) {
                resp.getOutputStream().write(resultSet.getBytes("content"));
            }
        } catch (SQLException throwables) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't load book content from DB id=" + id, throwables);
        }*/

        final DataHelper dataHelper = (DataHelper) req.getSession().getAttribute("dataHelper");

        try (OutputStream out = resp.getOutputStream()) {
            byte[] content = dataHelper.getContent(Long.valueOf(id));
            ByteArrayInputStream in = new ByteArrayInputStream(content);
            byte[] bytes = new byte[1024];

            int bytesRead;
            while ((bytesRead = in.read(bytes)) != -1) {
                out.write(bytes, 0, bytesRead);
            }
            out.write(content);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't load book content from DB id=" + id, ex);
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
