package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.enums.Specialization;
import com.airtribe.meditrack.util.CSVUtil;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorService {

    private final DataStore<Doctor> store = new DataStore<>();

    public Doctor registerDoctor(String name, int age, String phone, String address,
                                  Specialization specialization, double consultationFee) {
        String id = IdGenerator.getInstance().nextDoctorId();
        Doctor doctor = new Doctor(id, name, age, phone, address, specialization, consultationFee);
        store.save(id, doctor);
        return doctor;
    }

    public List<Doctor> getAllDoctors() {
        return store.findAll();
    }

    public boolean removeDoctor(String id) {
        return store.remove(id);
    }

    public Doctor searchDoctor(String id) {
        return store.find(id);
    }

    public List<Doctor> searchDoctor(Specialization specialization) {
        return store.findAll().stream()
                .filter(d -> d.getSpecialization() == specialization)
                .collect(Collectors.toList());
    }

    public void saveToCsv() throws IOException {
        List<String> lines = new ArrayList<>();
        for (Doctor d : store.findAll()) {
            lines.add(String.join(",", d.getId(), d.getName(), String.valueOf(d.getAge()),
                    d.getPhone(), d.getAddress(), d.getSpecialization().name(),
                    String.valueOf(d.getConsultationFee())));
        }
        CSVUtil.write(Constants.DOCTOR_FILE, lines);
    }

    public void loadFromCsv() throws IOException {
        for (String[] row : CSVUtil.read(Constants.DOCTOR_FILE)) {
            Doctor doctor = new Doctor(row[0], row[1], Integer.parseInt(row[2]), row[3], row[4],
                    Specialization.valueOf(row[5]), Double.parseDouble(row[6]));
            store.save(row[0], doctor);
            IdGenerator.getInstance().catchUpDoctor(Integer.parseInt(row[0].substring(1)));
        }
    }
}
