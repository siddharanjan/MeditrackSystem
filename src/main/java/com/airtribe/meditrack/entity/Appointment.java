package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.enums.AppointmentStatus;
import com.airtribe.meditrack.util.DateUtil;

import java.time.LocalDateTime;

public class Appointment implements Cloneable {

    private String id;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime scheduledAt;
    private String reason;
    private AppointmentStatus status;

    public Appointment(String id, Patient patient, Doctor doctor, LocalDateTime scheduledAt, String reason) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.scheduledAt = scheduledAt;
        this.reason = reason;
        this.status = AppointmentStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public String getReason() {
        return reason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    // not bothering to clone the doctor - two appointments sharing the same
    // Doctor object is fine since nothing mutates it. the patient does get
    // cloned since it holds mutable state (medical history) that shouldn't
    // leak between the original appointment and the copy
    @Override
    public Appointment clone() {
        try {
            Appointment copy = (Appointment) super.clone();
            copy.patient = this.patient.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Appointment implements Cloneable, this can't happen", e);
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s with Dr. %s on %s - %s (%s)",
                id, patient.getName(), doctor.getName(), DateUtil.format(scheduledAt), reason, status);
    }
}
