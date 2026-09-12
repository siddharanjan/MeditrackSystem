package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.ConsultationBill;
import com.airtribe.meditrack.entity.FollowUpBill;
import com.airtribe.meditrack.util.IdGenerator;

// basic factory - the menu code just asks for a bill "type" and doesn't
// need to know which Bill subclass it actually gets back
public class BillFactory {

    private BillFactory() {
    }

    public static Bill createBill(String billType, Appointment appointment, double consultationFee) {
        String billId = IdGenerator.getInstance().nextBillId();
        if ("followup".equalsIgnoreCase(billType) || "follow-up".equalsIgnoreCase(billType)) {
            return new FollowUpBill(billId, appointment, consultationFee);
        }
        return new ConsultationBill(billId, appointment, consultationFee);
    }
}
