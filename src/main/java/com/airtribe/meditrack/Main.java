package com.airtribe.meditrack;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.enums.Specialization;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.BillFactory;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.test.TestRunner;
import com.airtribe.meditrack.util.DateUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

// entry point + the console menu. kept it all in one class - splitting the
// menu up further didn't feel worth it for a project this size, the real
// logic lives in the service classes anyway
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final PatientService patientService = new PatientService();
    private static final DoctorService doctorService = new DoctorService();
    private static final AppointmentService appointmentService = new AppointmentService();

    public static void main(String[] args) {
        boolean loadData = Arrays.asList(args).contains("--loadData");

        if (loadData) {
            loadAllData();
        } else {
            seedSampleDoctors();
        }

        System.out.println("=== Welcome to MediTrack ===");
        mainMenu();

        System.out.println("Goodbye!");
    }

    private static void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n1. Patient Management");
            System.out.println("2. Doctor Management");
            System.out.println("3. Appointment Management");
            System.out.println("4. Billing");
            System.out.println("5. Patient Statistics");
            System.out.println("6. Quick Search (patients + doctors)");
            System.out.println("7. Save Data To CSV");
            System.out.println("8. Run Test Suite");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            switch (readLine()) {
                case "1":
                    patientMenu();
                    break;
                case "2":
                    doctorMenu();
                    break;
                case "3":
                    appointmentMenu();
                    break;
                case "4":
                    billingMenu();
                    break;
                case "5":
                    printPatientStatistics();
                    break;
                case "6":
                    quickSearch();
                    break;
                case "7":
                    saveAllData();
                    break;
                case "8":
                    TestRunner.runAll();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Not a valid option, try again.");
            }
        }
    }

    // one keyword search across both patients and doctors, using the
    // Searchable interface instead of two separate id/name lookups
    private static void quickSearch() {
        System.out.print("Keyword (matches id, name, or specialization): ");
        String keyword = readLine();

        List<Searchable> everything = new ArrayList<>();
        everything.addAll(patientService.getAllPatients());
        everything.addAll(doctorService.getAllDoctors());

        boolean found = false;
        for (Searchable s : everything) {
            if (s.matches(keyword)) {
                System.out.println("  " + s);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matches.");
        }
    }

    // ----- patients -----

    private static void patientMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n-- Patient Management --");
            System.out.println("1. Register patient");
            System.out.println("2. View all patients");
            System.out.println("3. Search by id");
            System.out.println("4. Search by name");
            System.out.println("5. Search by age");
            System.out.println("6. Add medical note");
            System.out.println("7. Update patient details");
            System.out.println("8. Remove patient");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (readLine()) {
                case "1":
                    registerPatient();
                    break;
                case "2":
                    viewAllPatients();
                    break;
                case "3":
                    System.out.print("Patient id: ");
                    Patient byId = patientService.searchPatient(readLine());
                    System.out.println(byId == null ? "No patient with that id." : byId);
                    break;
                case "4":
                    System.out.print("Name (or part of it): ");
                    List<Patient> byName = patientService.searchPatient(readLine(), true);
                    byName.forEach(System.out::println);
                    if (byName.isEmpty()) {
                        System.out.println("No matches.");
                    }
                    break;
                case "5":
                    System.out.print("Age: ");
                    List<Patient> byAge = patientService.searchPatient(readInt());
                    byAge.forEach(System.out::println);
                    if (byAge.isEmpty()) {
                        System.out.println("No matches.");
                    }
                    break;
                case "6":
                    addMedicalNote();
                    break;
                case "7":
                    updatePatientDetails();
                    break;
                case "8":
                    System.out.print("Patient id to remove: ");
                    System.out.println(patientService.removePatient(readLine()) ? "Removed." : "No such patient.");
                    break;
                case "0":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Not a valid option.");
            }
        }
    }

    private static void registerPatient() {
        try {
            System.out.print("Name: ");
            String name = readLine();
            System.out.print("Age: ");
            int age = readInt();
            System.out.print("Phone (10 digits): ");
            String phone = readLine();
            System.out.print("Address: ");
            String address = readLine();

            Patient patient = patientService.registerPatient(name, age, phone, address);
            System.out.println("Registered with id " + patient.getId());
        } catch (InvalidDataException e) {
            System.out.println("Could not register patient: " + e.getMessage());
        }
    }

    private static void viewAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        patients.sort(Comparator.comparing(Patient::getName));
        if (patients.isEmpty()) {
            System.out.println("No patients registered yet.");
        }
        for (Patient p : patients) {
            p.displayDetails();
        }
    }

    private static void addMedicalNote() {
        System.out.print("Patient id: ");
        Patient patient = patientService.searchPatient(readLine());
        if (patient == null) {
            System.out.println("No patient with that id.");
            return;
        }
        System.out.print("Note: ");
        patient.addMedicalNote(readLine());
        System.out.println("Added.");
    }

    private static void updatePatientDetails() {
        System.out.print("Patient id: ");
        Patient patient = patientService.searchPatient(readLine());
        if (patient == null) {
            System.out.println("No patient with that id.");
            return;
        }
        try {
            System.out.print("New name (leave blank to keep '" + patient.getName() + "'): ");
            String name = readLine();
            if (!name.isEmpty()) {
                patient.setName(name);
            }

            System.out.print("New age (leave blank to keep " + patient.getAge() + "): ");
            String ageInput = readLine();
            if (!ageInput.isEmpty()) {
                patient.setAge(Integer.parseInt(ageInput));
            }

            System.out.print("New phone (leave blank to keep " + patient.getPhone() + "): ");
            String phone = readLine();
            if (!phone.isEmpty()) {
                patient.setPhone(phone);
            }

            System.out.print("New address (leave blank to keep '" + patient.getAddress() + "'): ");
            String address = readLine();
            if (!address.isEmpty()) {
                patient.setAddress(address);
            }

            System.out.println("Updated.");
        } catch (InvalidDataException | NumberFormatException e) {
            System.out.println("Could not update patient: " + e.getMessage());
        }
    }

    // ----- doctors -----

    private static void doctorMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n-- Doctor Management --");
            System.out.println("1. Register doctor");
            System.out.println("2. View all doctors");
            System.out.println("3. Search by id");
            System.out.println("4. Search by specialization");
            System.out.println("5. Update doctor");
            System.out.println("6. Remove doctor");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (readLine()) {
                case "1":
                    registerDoctor();
                    break;
                case "2":
                    doctorService.getAllDoctors().forEach(Doctor::displayDetails);
                    break;
                case "3":
                    System.out.print("Doctor id: ");
                    Doctor byId = doctorService.searchDoctor(readLine());
                    System.out.println(byId == null ? "No doctor with that id." : byId);
                    break;
                case "4":
                    Specialization specialization = chooseSpecialization();
                    doctorService.searchDoctor(specialization).forEach(Doctor::displayDetails);
                    break;
                case "5":
                    updateDoctorDetails();
                    break;
                case "6":
                    System.out.print("Doctor id to remove: ");
                    System.out.println(doctorService.removeDoctor(readLine()) ? "Removed." : "No such doctor.");
                    break;
                case "0":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Not a valid option.");
            }
        }
    }

    private static void registerDoctor() {
        try {
            System.out.print("Name: ");
            String name = readLine();
            System.out.print("Age: ");
            int age = readInt();
            System.out.print("Phone (10 digits): ");
            String phone = readLine();
            System.out.print("Address: ");
            String address = readLine();
            Specialization specialization = chooseSpecialization();
            System.out.print("Consultation fee: ");
            double fee = readDouble();

            Doctor doctor = doctorService.registerDoctor(name, age, phone, address, specialization, fee);
            System.out.println("Registered with id " + doctor.getId());
        } catch (InvalidDataException e) {
            System.out.println("Could not register doctor: " + e.getMessage());
        }
    }

    private static Specialization chooseSpecialization() {
        Specialization[] values = Specialization.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i + 1) + ". " + values[i]);
        }
        System.out.print("Pick specialization: ");
        int choice = readInt();
        if (choice < 1 || choice > values.length) {
            System.out.println("Invalid choice, defaulting to GENERAL_PHYSICIAN.");
            return Specialization.GENERAL_PHYSICIAN;
        }
        return values[choice - 1];
    }

    private static void updateDoctorDetails() {
        System.out.print("Doctor id: ");
        Doctor doctor = doctorService.searchDoctor(readLine());
        if (doctor == null) {
            System.out.println("No doctor with that id.");
            return;
        }

        System.out.print("New consultation fee (leave blank to keep " + doctor.getConsultationFee() + "): ");
        String feeInput = readLine();
        if (!feeInput.isEmpty()) {
            try {
                doctor.setConsultationFee(Double.parseDouble(feeInput));
            } catch (NumberFormatException e) {
                System.out.println("That wasn't a number, leaving the fee as is.");
            }
        }

        System.out.print("Change specialization? (y/n): ");
        if (readLine().equalsIgnoreCase("y")) {
            doctor.setSpecialization(chooseSpecialization());
        }

        System.out.println("Updated.");
    }

    // ----- appointments -----

    private static void appointmentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n-- Appointment Management --");
            System.out.println("1. Book appointment");
            System.out.println("2. View all appointments");
            System.out.println("3. Confirm appointment");
            System.out.println("4. Cancel appointment");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (readLine()) {
                case "1":
                    bookAppointment();
                    break;
                case "2":
                    appointmentService.printAllAppointments();
                    break;
                case "3":
                    updateAppointmentStatus(true);
                    break;
                case "4":
                    updateAppointmentStatus(false);
                    break;
                case "0":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Not a valid option.");
            }
        }
    }

    private static void bookAppointment() {
        System.out.print("Patient id: ");
        Patient patient = patientService.searchPatient(readLine());
        if (patient == null) {
            System.out.println("No patient with that id.");
            return;
        }
        System.out.print("Doctor id: ");
        Doctor doctor = doctorService.searchDoctor(readLine());
        if (doctor == null) {
            System.out.println("No doctor with that id.");
            return;
        }
        try {
            System.out.print("Date and time (dd-MM-yyyy HH:mm): ");
            LocalDateTime scheduledAt = DateUtil.parse(readLine());
            System.out.print("Reason for visit: ");
            String reason = readLine();

            Appointment appointment = appointmentService.createAppointment(patient, doctor, scheduledAt, reason);
            System.out.println("Booked as " + appointment.getId());
        } catch (InvalidDataException e) {
            System.out.println("Could not book appointment: " + e.getMessage());
        }
    }

    private static void updateAppointmentStatus(boolean confirm) {
        System.out.print("Appointment id: ");
        String id = readLine();
        try {
            if (confirm) {
                appointmentService.confirmAppointment(id);
                System.out.println("Confirmed.");
            } else {
                appointmentService.cancelAppointment(id);
                System.out.println("Cancelled.");
            }
        } catch (AppointmentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // ----- billing -----

    private static void billingMenu() {
        System.out.print("Appointment id to bill: ");
        String appointmentId = readLine();
        Appointment appointment;
        try {
            appointment = appointmentService.findAppointment(appointmentId);
        } catch (AppointmentNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("Bill type (consultation / followup): ");
        String billType = readLine();

        Bill bill = BillFactory.createBill(billType, appointment, appointment.getDoctor().getConsultationFee());
        System.out.println(bill.generateBill());
        bill.printReceipt();
    }

    // ----- misc -----

    private static void printPatientStatistics() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients yet, nothing to report.");
            return;
        }
        double averageAge = patients.stream().mapToInt(Patient::getAge).average().orElse(0);
        System.out.println("Total patients   : " + patients.size());
        System.out.println("Average age      : " + (int) averageAge + " years");
    }

    private static void saveAllData() {
        try {
            patientService.saveToCsv();
            doctorService.saveToCsv();
            appointmentService.saveToCsv();
            System.out.println("Saved to the data/ folder.");
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    private static void loadAllData() {
        // csv parsing is just split(","), no escaping - a stray comma in an
        // old file shouldn't take the whole app down, so just fall back to
        // starting fresh
        try {
            patientService.loadFromCsv();
            doctorService.loadFromCsv();
            appointmentService.loadFromCsv(patientService, doctorService);
            System.out.println("Loaded " + patientService.getAllPatients().size() + " patients, "
                    + doctorService.getAllDoctors().size() + " doctors, "
                    + appointmentService.getAllAppointments().size() + " appointments from CSV.");
        } catch (IOException | RuntimeException e) {
            System.out.println("Could not load saved data, starting fresh instead: " + e.getMessage());
        }
    }

    private static void seedSampleDoctors() {
        doctorService.registerDoctor("Dr. Asha Menon", 42, "9900011122", "Andheri West Mumbai",
                Specialization.GENERAL_PHYSICIAN, 500);
        doctorService.registerDoctor("Dr. Karan Shah", 51, "9900033344", "Baner Pune",
                Specialization.CARDIOLOGY, 900);
    }

    // ----- input helpers -----

    private static String readLine() {
        return scanner.nextLine().trim();
    }

    private static int readInt() {
        try {
            return Integer.parseInt(readLine());
        } catch (NumberFormatException e) {
            System.out.println("That wasn't a number, using 0.");
            return 0;
        }
    }

    private static double readDouble() {
        try {
            return Double.parseDouble(readLine());
        } catch (NumberFormatException e) {
            System.out.println("That wasn't a number, using 0.");
            return 0;
        }
    }
}
