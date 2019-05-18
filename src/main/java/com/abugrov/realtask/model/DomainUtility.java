package com.abugrov.realtask.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

class DomainUtility {
    private static final DateTimeFormatter format =
        DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm", new Locale("ru"));

    static LocalDateTime toDate(String date) {
        return LocalDateTime.parse(date, format);
    }

    static String toString(LocalDateTime date) {
        return date.format(format);
    }
}
