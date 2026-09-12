package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.enums.AppointmentStatus;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.util.CSVUtil;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.DateUtil;
import com.airtribe.meditrack.util.IdGenerator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppointmentService {

    private final DataStore<Appointment> store = new DataStore<>();

    public Appointment createAppointment(Patient patient, Doctor doctor, LocalDateTime scheduledAt, String reason) {
        String id = IdGenerator.getInstance().nextAppointmentId();
        Appointment appointment = new Appointment(id, patient, doctor, scheduledAt, reason);
        store.save(id, appointment);
        return appointment;
    }

    public void confirmAppointment(String id) throws AppointmentNotFoundException {
        getOrThrow(id).setStatus(AppointmentStatus.CONFIRMED);
    }

    public void cancelAppointment(String id) throws AppointmentNotFoundException {
        getOrThrow(id).setStatus(AppointmentStatus.CANCELLED);
    }

    public Appointment findAppointment(String id) throws AppointmentNotFoundException {
        return getOrThrow(id);
    }

    public List<Appointment> getAllAppointments() {
        return store.findAll();
    }

    // could just be a for-each, using Iterator explicitly here instead
    public void printAllAppointments() {
        Iterator<Appointment> it = store.findAll().iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    private Appointment getOrThrow(String id) throws AppointmentNotFoundException {
        Appointment appointment = store.find(id);
        if (appointment == null) {
            throw new AppointmentNotFoundException("No appointment found with id " + id);
        }
        return appointment;
    }

    public void saveToCsv() throws IOException {
        List<String> lines = new ArrayList<>();
        for (Appointment a : store.findAll()) {
            lines.add(String.join(",", a.getId(), a.getPatient().getId(), a.getDoctor().getId(),
                    DateUtil.format(a.getScheduledAt()), a.getReason(), a.getStatus().name()));
        }
        CSVUtil.write(Constants.APPOINTMENT_FILE, lines);
    }

    public void loadFromCsv(PatientService patientService, DoctorService doctorService) throws IOException {
        for (String[] row : CSVUtil.read(Constants.APPOINTMENT_FILE)) {
            Patient patient = patientService.searchPatient(row[1]);
            Doctor doctor = doctorService.searchDoctor(row[2]);
            if (patient == null || doctor == null) {
                System.out.println("Skipping appointment " + row[0] + " - patient or doctor no longer exists");
                continue;
            }
            Appointment appointment = new Appointment(row[0], patient, doctor, DateUtil.parse(row[3]), row[4]);
            appointment.setStatus(AppointmentStatus.valueOf(row[5]));
            store.save(row[0], appointment);
            IdGenerator.getInstance().catchUpAppointment(Integer.parseInt(row[0].substring(1)));
        }
    }
}
