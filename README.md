# Clinic Insurance System

A desktop-based application developed using Java and JavaFX to manage clinic operations including patients, doctors, appointments, medical records, insurance policies, and insurance claims.

---

## Features

Patient Management
- Add new patients
- View patient list
- Store patient details (name, age, phone number, insurance status)

Doctor Management
- Add doctors
- View doctors
- Store specialization and consultation fee

Appointment Management
- Book appointments
- Link patients with doctors
- Validate patient and doctor existence

Medical Records
- Add medical records for patients
- Store diagnosis and treatment cost
- Validate patient existence

Insurance Policy Management
- Create insurance policies
- Assign one policy per patient
- Store provider, coverage amount, and policy type

Insurance Claims
- Create claims from existing medical records
- Validate that patient has a policy before creating a claim
- Process claims (approve or reject based on coverage)
- View all claims and their status

Data Persistence
- Save data to files (patients, doctors, appointments)
- Load data automatically when the application starts

Validation
- Prevent empty inputs
- Ensure numeric fields are valid and positive
- Validate phone numbers
- Prevent duplicate IDs
- Ensure proper relationships between entities

---

## Technologies Used

- Java
- JavaFX
- Maven
- File I/O (BufferedWriter, BufferedReader)

---

## Project Structure

src/
 └── main/
     └── java/
         └── com/rithika/clinicsystem/
             ├── model/      # Data classes
             ├── service/    # Business logic
             ├── ui/         # JavaFX UI screens
             └── util/       # Utilities (File handling, Validation)

---

## How to Run

Option 1 (IntelliJ IDEA)
1. Open the project in IntelliJ IDEA
2. Allow Maven to load dependencies
3. Run MainApp.java

Option 2 (Terminal)

mvn clean javafx:run

---

## Functional Flow

1. Add Patient  
2. Add Doctor  
3. Book Appointment  
4. Add Medical Record  
5. Create Insurance Policy  
6. Create Claim (from medical record)  
7. Process Claim  

---

## Notes

- Claims are created using existing medical records
- A patient must have an insurance policy before a claim can be created
- Each patient can only have one insurance policy
- The system uses a layered structure separating UI, logic, and data

---

## Data Storage

- Data is stored in text files inside a data/ directory
- Files are automatically created if they do not exist
- Data is loaded into memory at application startup

---

## Author

Rithika Wickramasinghe  
Software Engineering  

---

## License

This project is licensed under the MIT License.