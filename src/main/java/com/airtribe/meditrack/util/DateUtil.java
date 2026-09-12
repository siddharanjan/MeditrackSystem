package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.exception.InvalidDataException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN);

    private DateUtil() {
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime parse(String text) {
        try {
            return LocalDateTime.parse(text.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidDataException(
                    "Expected a date like 25-12-2025 14:30, got: " + text, e);
        }
    }
}
