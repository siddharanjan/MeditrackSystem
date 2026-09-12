package com.airtribe.meditrack.util;

import java.util.concurrent.atomic.AtomicInteger;

// eager singleton - instance is created up front since there's nothing
// expensive about it, didn't see the point of lazy init here.
// AtomicInteger is probably overkill for a single threaded console app but
// it doesn't cost anything to have it
public class IdGenerator {

    private static final IdGenerator INSTANCE = new IdGenerator();

    private final AtomicInteger patientSeq = new AtomicInteger(0);
    private final AtomicInteger doctorSeq = new AtomicInteger(0);
    private final AtomicInteger appointmentSeq = new AtomicInteger(0);
    private final AtomicInteger billSeq = new AtomicInteger(0);

    private IdGenerator() {
    }

    public static IdGenerator getInstance() {
        return INSTANCE;
    }

    public String nextPatientId() {
        return String.format("P%03d", patientSeq.incrementAndGet());
    }

    public String nextDoctorId() {
        return String.format("D%03d", doctorSeq.incrementAndGet());
    }

    public String nextAppointmentId() {
        return String.format("A%03d", appointmentSeq.incrementAndGet());
    }

    public String nextBillId() {
        return String.format("B%03d", billSeq.incrementAndGet());
    }

    // called after loading from csv so we don't hand out an id that's
    // already used in the file
    public void catchUpPatient(int seen) {
        patientSeq.updateAndGet(current -> Math.max(current, seen));
    }

    public void catchUpDoctor(int seen) {
        doctorSeq.updateAndGet(current -> Math.max(current, seen));
    }

    public void catchUpAppointment(int seen) {
        appointmentSeq.updateAndGet(current -> Math.max(current, seen));
    }
}
