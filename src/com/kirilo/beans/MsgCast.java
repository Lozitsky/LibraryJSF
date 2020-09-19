package com.kirilo.beans;

import com.kirilo.utils.UTF8Control;

import java.util.ResourceBundle;

//@ManagedBean
//@ApplicationScoped
public class MsgCast {
    public ResourceBundle getBundle() {
        return bundle;
    }

    private final ResourceBundle bundle;
    public MsgCast() {
        bundle = ResourceBundle.getBundle("com.kirilo.messages", new UTF8Control());
    }
}
