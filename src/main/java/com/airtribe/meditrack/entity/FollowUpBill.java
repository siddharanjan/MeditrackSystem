package com.airtribe.meditrack.entity;

public class FollowUpBill extends Bill {

    private static final double FOLLOW_UP_DISCOUNT = 0.5;

    public FollowUpBill(String billId, Appointment appointment, double consultationFee) {
        // follow ups are half the normal consultation fee
        super(billId, appointment, consultationFee * FOLLOW_UP_DISCOUNT);
    }

    @Override
    public String getBillType() {
        return "Follow-up";
    }

    @Override
    public BillSummary generateBill() {
        return new BillSummary(billId, appointment.getPatient().getName(), getBillType(),
                baseAmount, tax(), calculateTotal());
    }
}
