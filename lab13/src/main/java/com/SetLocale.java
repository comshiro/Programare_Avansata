package com;

import java.util.Locale;
import java.util.ResourceBundle;

public class SetLocale {
    private Locale currentLocale;

    public void execute(String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        Locale.setDefault(locale);
        this.currentLocale = locale;
        ResourceBundle messages = ResourceBundle.getBundle("res.Messages", locale);
        System.out.println(messages.getString("locale.set").replace("{0}", locale.toString()));
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }
}
