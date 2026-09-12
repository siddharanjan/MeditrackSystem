# Design Decisions

Quick notes on a few choices that might not be obvious from the code alone.

## No separate MedicalEntity class

The brief mentions an abstract `MedicalEntity`, but the actual expected
package layout only lists `Person`/`Doctor`/`Patient`/etc, no
`MedicalEntity.java`. Made `Person` itself abstract instead - it covers the
same "shared behaviour" role without adding an extra class that isn't in
the layout.

## searchPatient(String, boolean)

`searchPatient()` needed to be overloaded by id/name/age. ID and name are
both `String` though, and Java overloads by parameter types, not names - so
`searchPatient(String id)` and `searchPatient(String name)` can't both
exist. Added a second param instead: `searchPatient(String name, boolean
fuzzy)`, and made the boolean actually do something (substring vs exact
match) instead of being pointless.

## Why only Patient/Appointment get deep clones

`Patient` holds a mutable list (`medicalHistory`) and `Appointment` holds a
reference to a `Patient`, so a shallow copy would leak edits between the
clone and original. `Doctor` doesn't have that problem, nothing about it is
mutated in place, so it didn't need `Cloneable`.

## Bonus picks: CSV + Singleton/Factory

Went with File I/O (CSV save/load) and Design Patterns
(`IdGenerator`=singleton, `BillFactory`=factory) out of the available bonus
options. Skipped Observer - it's marked optional in the brief and felt like
overkill for a single-user CLI with nobody to notify.

## CSV is intentionally dumb

`CSVUtil` is just `String.split(",")`, no quoting. A comma in an address
would break the columns on reload. Could've written a smarter parser but
the brief asks for plain `split(",")` specifically, so just documenting the
limitation (no commas in free text fields) instead.

## DataStore<T>

Every service needed the same save/find/remove-by-id logic, so it's pulled
into one generic class instead of each service writing its own
`Map<String, X>` handling.

## Bill / ConsultationBill / FollowUpBill

`generateBill()` is abstract on `Bill`, overridden per subclass - this is
the overriding requirement from the brief. `FollowUpBill` just halves the
fee in its constructor, so `calculateTotal()` stays correct without any
`instanceof` checks anywhere.
