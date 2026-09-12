package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Searchable;

import java.util.ArrayList;
import java.util.List;

public class Patient extends Person implements Searchable, Cloneable {

    private List<String> medicalHistory;

    public Patient(String id, String name, int age, String phone, String address) {
        super(id, name, age, phone, address);
        this.medicalHistory = new ArrayList<>();
    }

    public void addMedicalNote(String note) {
        medicalHistory.add(note);
    }

    public List<String> getMedicalHistory() {
        return medicalHistory;
    }

    @Override
    public String getRole() {
        return "Patient";
    }

    @Override
    public void displayDetails() {
        System.out.println(this);
        System.out.println("   Medical history entries: " + medicalHistory.size());
        for (String note : medicalHistory) {
            System.out.println("     - " + note);
        }
    }

    @Override
    public boolean matches(String keyword) {
        String k = keyword.toLowerCase();
        return name.toLowerCase().contains(k) || id.equalsIgnoreCase(keyword);
    }

    // super.clone() only does a shallow copy, which would leave the clone
    // and the original sharing the same medicalHistory list - editing one
    // would edit the other too. copying the list here is what makes this
    // an actual deep copy
    @Override
    public Patient clone() {
        try {
            Patient copy = (Patient) super.clone();
            copy.medicalHistory = new ArrayList<>(this.medicalHistory);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Patient implements Cloneable, this can't happen", e);
        }
    }
}
