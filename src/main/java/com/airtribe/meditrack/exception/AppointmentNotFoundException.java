package com.airtribe.meditrack.exception;

// checked on purpose - cancelling or confirming an appointment that
// doesn't exist should be handled explicitly, not just blow up somewhere
public class AppointmentNotFoundException extends Exception {

    public AppointmentNotFoundException(String message) {
        super(message);
    }

    public AppointmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
