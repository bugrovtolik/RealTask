package com.abugrov.helpinghand.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DomainUtility {
    private static DateTimeFormatter format =
        DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm", new Locale("ru"));

    public static LocalDateTime toDate(String date) {
        return LocalDateTime.parse(date, format);
    }

    public static String toString(LocalDateTime date) {
        return date.format(format);
    }
}
