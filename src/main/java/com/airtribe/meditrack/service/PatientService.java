package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.util.CSVUtil;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PatientService {

    private final DataStore<Patient> store = new DataStore<>();

    public Patient registerPatient(String name, int age, String phone, String address) {
        String id = IdGenerator.getInstance().nextPatientId();
        Patient patient = new Patient(id, name, age, phone, address);
        store.save(id, patient);
        return patient;
    }

    public List<Patient> getAllPatients() {
        return store.findAll();
    }

    public boolean removePatient(String id) {
        return store.remove(id);
    }

    // --- overloaded searchPatient(): by id, by name, by age ---

    public Patient searchPatient(String id) {
        return store.find(id);
    }

    public List<Patient> searchPatient(String name, boolean fuzzy) {
        List<Patient> results = new ArrayList<>();
        for (Patient p : store.findAll()) {
            boolean isMatch = fuzzy
                    ? p.getName().toLowerCase().contains(name.toLowerCase())
                    : p.getName().equalsIgnoreCase(name);
            if (isMatch) {
                results.add(p);
            }
        }
        return results;
    }

    public List<Patient> searchPatient(int age) {
        return store.findAll().stream()
                .filter(p -> p.getAge() == age)
                .collect(Collectors.toList());
    }

    public void saveToCsv() throws IOException {
        List<String> lines = new ArrayList<>();
        for (Patient p : store.findAll()) {
            lines.add(String.join(",", p.getId(), p.getName(), String.valueOf(p.getAge()),
                    p.getPhone(), p.getAddress()));
        }
        CSVUtil.write(Constants.PATIENT_FILE, lines);
    }

    public void loadFromCsv() throws IOException {
        for (String[] row : CSVUtil.read(Constants.PATIENT_FILE)) {
            Patient patient = new Patient(row[0], row[1], Integer.parseInt(row[2]), row[3], row[4]);
            store.save(row[0], patient);
            IdGenerator.getInstance().catchUpPatient(Integer.parseInt(row[0].substring(1)));
        }
    }
}
