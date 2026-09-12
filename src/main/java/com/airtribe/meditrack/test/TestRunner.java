package com.airtribe.meditrack.test;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.enums.AppointmentStatus;
import com.airtribe.meditrack.enums.Specialization;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.BillFactory;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.time.LocalDateTime;

// no JUnit here, just plain methods that print PASS/FAIL. not as clean as
// a real test framework but it does the job for this project
public class TestRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        runAll();
    }

    public static void runAll() {
        passed = 0;
        failed = 0;

        System.out.println("Running MediTrack test suite...\n");

        testValidatorRejectsBadPhone();
        testValidatorRejectsBadAge();
        testIdGeneratorProducesUniqueIds();
        testDataStoreCrud();
        testPatientCloneIsADeepCopy();
        testAppointmentCancelFlow();
        testCancellingUnknownAppointmentThrows();
        testBillFactoryAppliesFollowUpDiscount();

        System.out.println("\n" + passed + " passed, " + failed + " failed");
    }

    private static void check(String label, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + label);
        } else {
            failed++;
            System.out.println("[FAIL] " + label);
        }
    }

    private static void testValidatorRejectsBadPhone() {
        boolean threw = false;
        try {
            Validator.validatePhone("12345");
        } catch (InvalidDataException e) {
            threw = true;
        }
        check("Validator rejects a phone number that isn't 10 digits", threw);
    }

    private static void testValidatorRejectsBadAge() {
        boolean threw = false;
        try {
            Validator.validateAge(-5);
        } catch (InvalidDataException e) {
            threw = true;
        }
        check("Validator rejects a negative age", threw);
    }

    private static void testIdGeneratorProducesUniqueIds() {
        String first = IdGenerator.getInstance().nextBillId();
        String second = IdGenerator.getInstance().nextBillId();
        check("IdGenerator never hands out the same bill id twice", !first.equals(second));
    }

    private static void testDataStoreCrud() {
        DataStore<String> store = new DataStore<>();
        store.save("k1", "hello");
        boolean foundAfterSave = "hello".equals(store.find("k1"));
        boolean removed = store.remove("k1");
        boolean goneAfterRemove = store.find("k1") == null;
        check("DataStore save/find/remove works as expected", foundAfterSave && removed && goneAfterRemove);
    }

    private static void testPatientCloneIsADeepCopy() {
        Patient original = new Patient("P900", "Test Patient", 30, "9876543210", "Pune");
        original.addMedicalNote("allergic to penicillin");

        Patient clone = original.clone();
        clone.addMedicalNote("note added only on the clone");

        check("Cloning a patient copies medical history instead of sharing it",
                original.getMedicalHistory().size() == 1 && clone.getMedicalHistory().size() == 2);
    }

    private static void testAppointmentCancelFlow() {
        AppointmentService appointmentService = new AppointmentService();
        Patient patient = new Patient("P901", "Ravi Kumar", 40, "9876543211", "Mumbai");
        Doctor doctor = new Doctor("D901", "Dr. Mehta", 45, "9876543212", "Mumbai",
                Specialization.GENERAL_PHYSICIAN, 500);

        Appointment appointment = appointmentService.createAppointment(
                patient, doctor, LocalDateTime.now().plusDays(1), "fever");

        try {
            appointmentService.cancelAppointment(appointment.getId());
            check("Cancelling an appointment updates its status", appointment.getStatus() == AppointmentStatus.CANCELLED);
        } catch (AppointmentNotFoundException e) {
            check("Cancelling an appointment updates its status", false);
        }
    }

    private static void testCancellingUnknownAppointmentThrows() {
        AppointmentService appointmentService = new AppointmentService();
        boolean threw = false;
        try {
            appointmentService.cancelAppointment("A999");
        } catch (AppointmentNotFoundException e) {
            threw = true;
        }
        check("Cancelling an appointment id that doesn't exist throws", threw);
    }

    private static void testBillFactoryAppliesFollowUpDiscount() {
        Patient patient = new Patient("P902", "Anita Rao", 28, "9876543213", "Nashik");
        Doctor doctor = new Doctor("D902", "Dr. Iyer", 50, "9876543214", "Nashik",
                Specialization.CARDIOLOGY, 800);
        Appointment appointment = new Appointment("A902", patient, doctor, LocalDateTime.now(), "checkup");

        Bill consultationBill = BillFactory.createBill("consultation", appointment, 800);
        Bill followUpBill = BillFactory.createBill("followup", appointment, 800);

        check("A follow-up bill is cheaper than a fresh consultation for the same fee",
                followUpBill.calculateTotal() < consultationBill.calculateTotal());
    }
}
