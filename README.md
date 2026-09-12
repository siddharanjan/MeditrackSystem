# MediTrack

Console-based Clinic & Appointment Management System, built in core Java
for the Airtribe Java mentorship. Register patients/doctors, book
appointments, generate bills - all through a menu-driven CLI. No frameworks,
no database, just plain Java.

Main goal here was practicing OOP (inheritance, polymorphism, abstraction),
collections/generics, exceptions, file I/O, and a couple of design patterns.

## Features

- Full CRUD on patients and doctors (add/search/update/remove)
- Quick keyword search across patients and doctors together (via the `Searchable` interface)
- Book, confirm, cancel appointments (`PENDING` / `CONFIRMED` / `CANCELLED` / `COMPLETED`)
- Billing with tax, two bill types (consultation / follow-up), immutable `BillSummary`
- Deep-copy cloning for patients (so editing a clone doesn't touch the original)
- Save/load everything to CSV
- A small manual test suite (`TestRunner`), runnable from the menu

### Bonus features

Picked two from the brief:

1. **File I/O** - save/load to `data/*.csv`, reload with `--loadData`
2. **Design patterns** - `IdGenerator` is a singleton, `BillFactory` picks
   the right `Bill` subclass

## Project structure

```
src/main/java/com/airtribe/meditrack/
├── Main.java
├── constants/Constants.java
├── entity/       Person, Doctor, Patient, Appointment, Bill (+ subtypes), BillSummary
├── enums/        Specialization, AppointmentStatus
├── service/      DoctorService, PatientService, AppointmentService, BillFactory
├── util/         Validator, DateUtil, CSVUtil, IdGenerator, DataStore<T>
├── exception/    AppointmentNotFoundException, InvalidDataException
├── interfaces/   Searchable, Payable
└── test/TestRunner.java

docs/
├── Setup_Instructions.md
├── JVM_Report.md
└── Design_Decisions.md
```

Note: it's `interfaces/` not `interface/` - the singular is a reserved word
in Java, won't compile as a package name.

## Running it

Plain `javac`/`java`, no build tool:

```bash
find src/main/java -name "*.java" | xargs javac -d out
java -cp out com.airtribe.meditrack.Main

# or load data saved from a previous run
java -cp out com.airtribe.meditrack.Main --loadData
```

In IntelliJ: open the project folder, mark `src/main/java` as the sources
root if it isn't picked up automatically (right-click it > Mark Directory as
> Sources Root), then run `Main`.

See `docs/Setup_Instructions.md` for JDK install steps.

## Sample run

```
1. Patient Management
...
Choose an option: 1
Name: John Doe
Age: 34
Phone (10 digits): 9876543210
Address: Kothrud Pune
Registered with id P001
```

Booking + billing:

```
Patient id: P001
Doctor id: D001
Date and time (dd-MM-yyyy HH:mm): 25-12-2026 10:30
Reason for visit: follow up on bp
Booked as A001
...
Bill type (consultation / followup): consultation
Bill B001 (Consultation) for John Doe -> base Rs.500.00 + tax Rs.90.00 = Rs.590.00
```

Test suite (menu option 8):

```
[PASS] Validator rejects a phone number that isn't 10 digits
...
8 passed, 0 failed
```

## Known limitations

- `CSVUtil` is just `String.split(",")`, no escaping - don't put commas in
  addresses or reasons, it'll shift the columns on reload.
- Everything's in-memory + CSV, single-user console app.

More reasoning in `docs/Design_Decisions.md`.
