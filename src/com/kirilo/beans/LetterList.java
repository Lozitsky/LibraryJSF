package com.kirilo.beans;

import com.kirilo.enums.LangList;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(eager = true)
@ApplicationScoped
public class LetterList {
    private LangList lang;

    private Character[] chars;

    public LetterList() {
        lang = LangList.EN;
    }

    public LangList getLang() {
        return lang;
    }

    public void setLang(LangList lang) {
        this.lang = lang;
    }

    public Character[] getChars() {
        if (chars == null) {
            initChars();
        }
        return chars;
    }

    private void initChars() {

        switch (lang) {
            case UA:
                chars = new Character[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Є', 'Ж', 'З', 'И', 'І',
                        'Ї', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч',
                        'Ш', 'Щ', 'Ь', 'Ю', 'Я'
                };
            case RU:
                chars = new Character[]{'А', 'Б', 'В', 'Г', 'Д', 'Э', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й',
                        'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ',
                        'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'
                };
            case EN:
            default:
                chars = new Character[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                        'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
                };
        }
    }
}
