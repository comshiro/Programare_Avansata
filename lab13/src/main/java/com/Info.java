package com;

import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.DateFormat;
import java.text.DateFormatSymbols;

public class Info {
    public void execute(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        ResourceBundle messages = ResourceBundle.getBundle("res.Messages", locale);
        System.out.println(messages.getString("info").replace("{0}", locale.toString()));
        // Country and Language
        System.out.println(messages.getString("country") + ": " + locale.getDisplayCountry(Locale.ENGLISH) + " (" + locale.getDisplayCountry(locale) + ")");
        System.out.println(messages.getString("language") + ": " + locale.getDisplayLanguage(Locale.ENGLISH) + " (" + locale.getDisplayLanguage(locale) + ")");
        // Currency
        try {
            Currency currency = Currency.getInstance(locale);
            System.out.println(messages.getString("currency") + ": " + currency.getCurrencyCode() + " (" + currency.getDisplayName(locale) + ")");
        } catch (Exception e) {
            System.out.println(messages.getString("currency") + ": N/A");
        }
        // Week Days
        DateFormatSymbols dfs = new DateFormatSymbols(locale);
        String[] weekdays = dfs.getWeekdays();
        StringBuilder weekDaysStr = new StringBuilder();
        for (int i = 2; i <= 7; i++) {
            weekDaysStr.append(weekdays[i]);
            if (i < 7) weekDaysStr.append(", ");
        }
        weekDaysStr.append(", ").append(weekdays[1]);
        System.out.println(messages.getString("weekdays") + ": " + weekDaysStr);
        // Months
        String[] months = dfs.getMonths();
        StringBuilder monthsStr = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            monthsStr.append(months[i]);
            if (i < 11) monthsStr.append(", ");
        }
        System.out.println(messages.getString("months") + ": " + monthsStr);
        // Today
        Date today = new Date();
        DateFormat dfEn = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH);
        DateFormat dfLoc = DateFormat.getDateInstance(DateFormat.LONG, locale);
        System.out.println(messages.getString("today") + ": " + dfEn.format(today) + " (" + dfLoc.format(today) + ")");
    }
}
