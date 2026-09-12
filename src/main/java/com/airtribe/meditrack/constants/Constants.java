package com.airtribe.meditrack.constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// just a bag of app-wide constants, nothing here should ever change at
// runtime
public final class Constants {

    public static final double TAX_RATE = 0.18; // flat GST-ish rate, keeps billing math simple

    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 120;

    public static final String DATA_DIR = "data";
    public static final String PATIENT_FILE = DATA_DIR + "/patients.csv";
    public static final String DOCTOR_FILE = DATA_DIR + "/doctors.csv";
    public static final String APPOINTMENT_FILE = DATA_DIR + "/appointments.csv";

    public static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm";

    // make sure the data folder actually exists before any service tries
    // to write a csv file into it
    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Warning: could not create data directory - " + e.getMessage());
        }
    }

    private Constants() {
    }
}
