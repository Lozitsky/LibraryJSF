package com.kirilo.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;


@Named("person")
@RequestScoped
public class User {
    private String username;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
