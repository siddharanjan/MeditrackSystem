package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.enums.Specialization;
import com.airtribe.meditrack.interfaces.Searchable;

public class Doctor extends Person implements Searchable {

    private Specialization specialization;
    private double consultationFee;

    public Doctor(String id, String name, int age, String phone, String address,
                  Specialization specialization, double consultationFee) {
        super(id, name, age, phone, address);
        this.specialization = specialization;
        this.consultationFee = consultationFee;
    }

    @Override
    public String getRole() {
        return "Doctor";
    }

    @Override
    public void displayDetails() {
        System.out.println(this);
        System.out.println("   Specialization : " + specialization);
        System.out.printf("   Consultation fee: Rs. %.2f%n", consultationFee);
    }

    @Override
    public boolean matches(String keyword) {
        String k = keyword.toLowerCase();
        return name.toLowerCase().contains(k)
                || id.equalsIgnoreCase(keyword)
                || specialization.name().toLowerCase().contains(k);
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }
}
