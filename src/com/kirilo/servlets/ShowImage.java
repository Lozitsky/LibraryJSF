package com.kirilo.servlets;

import com.kirilo.controllers.BookController;
import com.kirilo.db.DataHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// https://stackoverflow.com/a/2341322/9586230
public class ShowImage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/jpeg");
        String id = req.getParameter("id");
//        final DataHelper dataHelper = (DataHelper) req.getSession().getAttribute("dataHelper");
        final BookController controller = (BookController) req.getSession().getAttribute("bookController");

        try (OutputStream out = resp.getOutputStream()) {
//            final List<byte[]> image = controller.getImage(Long.valueOf(id));
            final byte[] image = controller.getImage(Long.valueOf(id));
//            https://stackoverflow.com/questions/55709/streaming-large-files-in-a-java-servlet
//            https://stackoverflow.com/questions/1802123/can-we-convert-a-byte-array-into-an-inputstream-in-java
            ByteArrayInputStream in = new ByteArrayInputStream(image);
                byte[] bytes = new byte[1024];
                int bytesRead;

                while ((bytesRead = in.read(bytes)) != -1) {
                    out.write(bytes, 0, bytesRead);
                }
                out.write(image);

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't load book picture from DB id=" + id, ex);
        }
    }

/*        try (Statement statement = (Database.getConnection()).createStatement();
             ResultSet resultSet = statement.executeQuery("select image from book where id=" + id)) {
            while (resultSet.next()) {
                resp.getOutputStream().write(resultSet.getBytes("image"));
            }
        } catch (SQLException throwables) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't load book picture from DB id=" + id, throwables);
        }*/

/*        final DataHelper dataHelper = (DataHelper) req.getSession().getAttribute("dataHelper");
        resp.getOutputStream().write(dataHelper.getImage(Long.valueOf(id)));*/

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
