package com.kirilo.beans;

import com.kirilo.controllers.BookController;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean
@SessionScoped
public class User implements Serializable {
    private static final long serialVersionUID = 8621018083323053012L;

    private String username;
    private String password;

    public BookController getController() {
        return controller;
    }

    public void setController(BookController controller) {
        this.controller = controller;
    }

    @ManagedProperty(value = "#{bookController}")
    private BookController controller;

    public User() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            ((HttpServletRequest) (context.getExternalContext().getRequest())).login(username, password);
            controller.changeQuantityOfBooks();
            controller.getBooksFromSelectedPage();
            return "books";
        } catch (ServletException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't login!", e);
            final FacesMessage facesMessage = new FacesMessage("#{msg['login.incorrect']}");
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage("login_fields",facesMessage);
        }
        return "index?faces-redirect=true";
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        final HttpServletRequest request = (HttpServletRequest) (context.getExternalContext().getRequest());
        String redirect = "/pages/index.xhtml?faces-redirect=true";

        try {
            request.logout();
        } catch (ServletException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't logout!", e);
        }

        context.getExternalContext().invalidateSession();
        return redirect;
    }
}
