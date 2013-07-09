package ru.strozh.stego.view;

import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 */
@ManagedBean
@SessionScoped
public class Language implements Serializable {

    private ArrayList<String> lang;
    private ArrayList<String> ruLang;
    private ArrayList<String> engLang;

    public Language() {

        ruLang = new ArrayList<String>();

        ruLang.add("Своё изображение");
        ruLang.add("Ссылка на изображение");
        ruLang.add("Текст");
        ruLang.add("Файл");
        ruLang.add("Текстовое сообщение");
        ruLang.add("Кодовое слово");
        ruLang.add("Назад");
        ruLang.add("Сохранить");

        engLang = new ArrayList<String>();

        engLang.add("Your picture");
        engLang.add("link to picture");
        engLang.add("Text");
        engLang.add("File");
        engLang.add("Your message");
        engLang.add("Secret key");
        engLang.add("Back");
        engLang.add("Download");

        lang = new ArrayList(ruLang);
    }

    public void setEngLang() {
        lang = new ArrayList<String>(engLang);
    }

    public void setRuLang() {
        lang = new ArrayList<String>(ruLang);
    }

    public ArrayList<String> getLang() {
        return lang;
    }
}
