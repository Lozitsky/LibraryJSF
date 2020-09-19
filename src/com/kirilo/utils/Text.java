package com.kirilo.utils;

import javax.faces.context.FacesContext;
import java.util.Enumeration;
import java.util.ResourceBundle;

// https://stackoverflow.com/questions/3645491/i18n-with-utf-8-encoded-properties-files-in-jsf-2-0-application
// https://support.assetbank.co.uk/hc/en-gb/articles/115005205928-How-to-save-a-tab-delimited-UTF-8-encoded-file-in-Excel
public class Text extends ResourceBundle {

    protected static final String BUNDLE_NAME = "com.kirilo.messages";
    protected static final Control UTF8_CONTROL = new UTF8Control();
    public static ResourceBundle bundle;

    public Text() {
        bundle = ResourceBundle.getBundle(BUNDLE_NAME,
                FacesContext.getCurrentInstance().getViewRoot().getLocale(), UTF8_CONTROL);
        setParent(bundle);
    }

    @Override
    protected Object handleGetObject(String key) {
        return parent.getObject(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return parent.getKeys();
    }
}
