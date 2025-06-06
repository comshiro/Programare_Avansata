package com;

import java.util.Locale;

public class DisplayLocales {
    public void execute() {
        System.out.println("Available locales:");
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            System.out.println(locale.toString() + " - " + locale.getDisplayName(locale));
        }
    }
}
