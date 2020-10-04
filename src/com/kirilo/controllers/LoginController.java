package com.kirilo.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(eager = true)
@SessionScoped
public class LoginController implements Serializable {
    static final long serialVersionUID = 8030681633558230345L;
/*    @ManagedProperty(value = "id_page")
    private String page;*/

    public LoginController() {
    }

    public String goToIndex() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index";
    }

/*    public String goToBooks() {
        return "books";
    }*/
}
