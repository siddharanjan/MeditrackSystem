package com.airtribe.meditrack.entity;

public class ConsultationBill extends Bill {

    public ConsultationBill(String billId, Appointment appointment, double baseAmount) {
        super(billId, appointment, baseAmount);
    }

    @Override
    public String getBillType() {
        return "Consultation";
    }

    @Override
    public BillSummary generateBill() {
        return new BillSummary(billId, appointment.getPatient().getName(), getBillType(),
                baseAmount, tax(), calculateTotal());
    }
}
