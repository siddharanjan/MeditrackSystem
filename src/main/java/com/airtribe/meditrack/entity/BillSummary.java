package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;

// read-only snapshot of a bill, handed out once a Bill is generated. every
// field is final and there's no setters, so it can't be changed after
// the fact
public final class BillSummary {

    private final String billId;
    private final String patientName;
    private final String billType;
    private final double baseAmount;
    private final double taxAmount;
    private final double totalAmount;
    private final LocalDateTime generatedOn;

    public BillSummary(String billId, String patientName, String billType,
                        double baseAmount, double taxAmount, double totalAmount) {
        this.billId = billId;
        this.patientName = patientName;
        this.billType = billType;
        this.baseAmount = baseAmount;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.generatedOn = LocalDateTime.now();
    }

    public String getBillId() {
        return billId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getBillType() {
        return billType;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getGeneratedOn() {
        return generatedOn;
    }

    @Override
    public String toString() {
        return String.format("Bill %s (%s) for %s -> base Rs.%.2f + tax Rs.%.2f = Rs.%.2f",
                billId, billType, patientName, baseAmount, taxAmount, totalAmount);
    }
}
