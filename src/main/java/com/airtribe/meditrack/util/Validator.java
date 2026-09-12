package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.exception.InvalidDataException;

import java.util.regex.Pattern;

// keeping all the field validation here instead of scattering it across
// every constructor. everything throws InvalidDataException so callers
// only need to catch one thing
public class Validator {

    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{10}");

    private Validator() {
    }

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Name cannot be blank");
        }
    }

    public static void validateAge(int age) {
        if (age < Constants.MIN_AGE || age > Constants.MAX_AGE) {
            throw new InvalidDataException("Age " + age + " doesn't look right");
        }
    }

    public static void validatePhone(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new InvalidDataException("Phone number must be exactly 10 digits");
        }
    }
}
