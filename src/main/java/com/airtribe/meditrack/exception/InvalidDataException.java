package com.airtribe.meditrack.exception;

// thrown by Validator when something coming in from the console or a csv
// file doesn't make sense. unchecked on purpose so it doesn't have to be
// declared everywhere - the menu loop just catches it and prints a message
public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
