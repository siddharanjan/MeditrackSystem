package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.interfaces.Payable;

// base for the different bill types - subclasses mainly differ in how the
// base amount gets worked out and what they're called
public abstract class Bill implements Payable {

    protected final String billId;
    protected final Appointment appointment;
    protected final double baseAmount;

    protected Bill(String billId, Appointment appointment, double baseAmount) {
        this.billId = billId;
        this.appointment = appointment;
        this.baseAmount = baseAmount;
    }

    protected double tax() {
        return baseAmount * Constants.TAX_RATE;
    }

    @Override
    public double calculateTotal() {
        return baseAmount + tax();
    }

    public abstract BillSummary generateBill();

    public abstract String getBillType();

    public String getBillId() {
        return billId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public double getBaseAmount() {
        return baseAmount;
    }
}
