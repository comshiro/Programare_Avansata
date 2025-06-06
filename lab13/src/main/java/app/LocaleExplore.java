package app;

import com.DisplayLocales;
import com.SetLocale;
import com.Info;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LocaleExplore {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Locale currentLocale = Locale.getDefault();
        ResourceBundle messages = ResourceBundle.getBundle("res.Messages", currentLocale);
        DisplayLocales displayLocales = new DisplayLocales();
        SetLocale setLocale = new SetLocale();
        Info info = new Info();

        while (true) {
            System.out.print(messages.getString("prompt") + " ");
            String command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("display locales")) {
                displayLocales.execute();
            } else if (command.toLowerCase().startsWith("set locale")) {
                String[] parts = command.split("\\s+");
                if (parts.length >= 3) {
                    String langTag = parts[2];
                    setLocale.execute(langTag);
                    currentLocale = Locale.forLanguageTag(langTag);
                    messages = ResourceBundle.getBundle("res.Messages", currentLocale);
                } else {
                    System.out.println("Usage: set locale <languageTag>");
                }
            } else if (command.equalsIgnoreCase("info")) {
                info.execute(currentLocale);
            } else if (command.equalsIgnoreCase("exit")) {
                break;
            } else {
                System.out.println(messages.getString("invalid"));
            }
        }
        scanner.close();
    }
}
