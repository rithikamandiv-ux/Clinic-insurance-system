```markdown
# CareNexus

### Healthcare Operations & Insurance Management System

[![CareNexus CI](https://github.com/rithikamandiv-ux/Clinic-insurance-system/actions/workflows/ci.yml/badge.svg)](https://github.com/rithikamandiv-ux/Clinic-insurance-system/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-17-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-4169E1)
![License](https://img.shields.io/badge/License-MIT-green)

CareNexus is a desktop-based healthcare operations and insurance management system built using a client-server architecture.

The application combines a **JavaFX desktop frontend** with a **Spring Boot REST API** and a **PostgreSQL database**. It supports the management of patients, doctors, appointments, medical records, insurance policies, and insurance claims while enforcing domain rules, validation, and relational integrity.

This project was developed as a portfolio-level software engineering project with emphasis on object-oriented programming, layered architecture, REST APIs, relational database design, automated testing, database migrations, and continuous integration.

---

## Features

### Dashboard

- Displays clinic operational summaries
- Provides navigation to all major application areas
- Presents patient, doctor, appointment, medical-record, policy, and claim information

### Patient Management

- Add patients
- View all patients
- Update patient information
- Delete patients when no dependent records exist
- Store:
  - Patient name
  - Age
  - Phone number
  - Insurance status
- Strict patient ID format: `P###`

### Doctor Management

- Add doctors
- View doctors
- Update doctor details
- Delete doctors when no appointments depend on them
- Store:
  - Doctor name
  - Specialization
  - Consultation fee
- Strict doctor ID format: `D###`

### Appointment Management

- Book appointments
- Assign patients to doctors
- Update appointment details
- Complete appointments
- Cancel appointments
- Delete appointments when no medical record depends on them
- Strict appointment ID format: `A###`

Appointment lifecycle:

```text
BOOKED
├── COMPLETED
└── CANCELLED
```

### Medical Records

- Create medical records for completed appointments
- Store:
    - Diagnosis
    - Treatment
    - Treatment cost
- Update existing medical records
- One medical record per appointment
- Prevent creation for appointments that are not completed
- Strict medical-record ID format: `MR###`

### Insurance Policy Management

- Create insurance policies for patients
- Update policy information
- Store:
    - Insurance provider
    - Coverage amount
    - Policy type
- Enforce one insurance policy per patient
- Strict policy ID format: `POL###`

### Insurance Claims

- Create claims from existing medical records
- Automatically derive claim amount from treatment cost
- Process pending claims
- Approve or reject claims according to available insurance coverage
- Enforce one claim per medical record
- Strict claim ID format: `CL###`

Claim statuses:

```text
PENDING
APPROVED
REJECTED
```

---

## System Architecture

CareNexus follows a client-server architecture.

```text
┌─────────────────────────────┐
│       JavaFX Frontend       │
│                             │
│ Views • API Services • DTOs │
└──────────────┬──────────────┘
               │
               │ HTTP / JSON
               ▼
┌─────────────────────────────┐
│     Spring Boot REST API    │
│                             │
│ Controllers • Services      │
│ Validation • Error Handling │
└──────────────┬──────────────┘
               │
               │ JPA / Hibernate
               ▼
┌─────────────────────────────┐
│         PostgreSQL          │
│                             │
│     Managed with Flyway     │
└─────────────────────────────┘
```

The frontend does not access PostgreSQL directly.

All data operations are performed through the Spring Boot REST API.

---

## Technology Stack

### Frontend

| Technology | Purpose |
|---|---|
| Java 17 | Core programming language |
| JavaFX 21.0.6 | Desktop user interface |
| Java HTTP Client | REST API communication |
| Jackson | JSON serialization and deserialization |
| Maven | Dependency and build management |

### Backend

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming language |
| Spring Boot 4.1.1 | Backend framework |
| Spring Web MVC | REST API development |
| Spring Data JPA | Persistence abstraction |
| Hibernate | ORM and schema validation |
| Jakarta Validation | Request validation |
| Flyway | Database schema migrations |
| Maven | Dependency and build management |

### Database

| Technology | Purpose |
|---|---|
| PostgreSQL | Relational database |
| Flyway | Version-controlled schema management |

### Testing and DevOps

| Technology | Purpose |
|---|---|
| JUnit 5 | Automated testing |
| Mockito | Service-layer mocking |
| MockMvc | Controller and validation testing |
| GitHub Actions | Continuous Integration |
| Git | Version control |
| GitHub | Repository hosting |

---

## Backend Architecture

The backend follows a layered architecture:

```text
HTTP Request
     │
     ▼
Controller
     │
     ▼
Service
     │
     ▼
Repository
     │
     ▼
JPA / Hibernate
     │
     ▼
PostgreSQL
```

### Controller Layer

Responsible for:

- HTTP endpoints
- Request validation
- Request and response DTOs
- HTTP status handling

### Service Layer

Responsible for:

- Business rules
- Entity relationships
- State transitions
- Resource validation
- Domain operations

### Repository Layer

Responsible for:

- Database access
- JPA persistence
- Entity queries

---

## REST API

The backend exposes REST endpoints for:

```text
/api/patients
/api/doctors
/api/appointments
/api/medical-records
/api/insurance-policies
/api/insurance-claims
```

HTTP methods used:

```text
GET     Read resources
POST    Create resources
PUT     Update resources
PATCH   Perform state transitions or processing
DELETE  Delete resources
```

Create and update operations use separate DTO contracts so immutable resource identifiers do not need to be included again during updates.

---

## Validation

CareNexus validates data at both frontend and backend levels.

Examples include:

- Required-field validation
- Age limits
- Phone-number validation
- Positive monetary values
- Decimal precision validation
- Strict resource ID formats
- Duplicate-resource prevention
- Relationship validation
- Appointment state validation

### Resource ID Formats

| Resource | Format | Example |
|---|---|---|
| Patient | `P###` | `P001` |
| Doctor | `D###` | `D001` |
| Appointment | `A###` | `A001` |
| Medical Record | `MR###` | `MR001` |
| Insurance Policy | `POL###` | `POL001` |
| Insurance Claim | `CL###` | `CL001` |

Malformed identifiers are rejected before reaching the service layer.

---

## Error Handling

The backend provides structured API error responses.

The frontend converts those responses into user-readable messages.

Common HTTP responses include:

| Status | Meaning |
|---|---|
| `400 Bad Request` | Invalid input or malformed request |
| `404 Not Found` | Requested resource does not exist |
| `409 Conflict` | Operation conflicts with current resource relationships |
| `500 Internal Server Error` | Unexpected backend failure |

Foreign-key relationships are protected so important healthcare records cannot be accidentally removed.

---

## Database Design

CareNexus uses six main domain tables:

```text
patients
doctors
appointments
medical_records
insurance_policies
insurance_claims
```

### Main Relationships

```text
Patient
├── Appointments
└── Insurance Policy

Doctor
└── Appointments

Appointment
└── Medical Record

Medical Record
└── Insurance Claim
```

Important constraints include:

- One insurance policy per patient
- One medical record per appointment
- One insurance claim per medical record
- Appointments reference valid patients and doctors
- Medical records reference valid appointments
- Policies reference valid patients
- Claims reference valid medical records

---

## Database Migrations

Database schema management is handled using **Flyway**.

Initial migration:

```text
backend/src/main/resources/db/migration/
└── V1__baseline_schema.sql
```

For a new empty database, Flyway creates the CareNexus schema before Hibernate validates the entity mappings.

Hibernate is configured with:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

This means:

```text
Flyway
└── owns schema creation and schema evolution

Hibernate
└── validates entity-to-database compatibility
```

Future schema changes should be introduced as new migrations:

```text
V2__description.sql
V3__description.sql
```

---

## Automated Testing

The backend currently contains **83 automated tests**.

The test suite covers:

- Patient service operations
- Doctor service operations
- Appointment lifecycle rules
- Medical-record rules
- Insurance-policy rules
- Insurance-claim processing
- Request validation
- Path-variable ID validation
- Update DTO validation
- Spring application context startup

Testing uses:

- JUnit 5
- Mockito
- MockMvc
- PostgreSQL test database

Development and automated testing use separate databases:

```text
Development:
clinic_insurance

Testing:
clinic_insurance_test
```

---

## Continuous Integration

CareNexus uses **GitHub Actions** for continuous integration.

Workflow:

```text
.github/workflows/ci.yml
```

The workflow runs on:

- Pushes to `main`
- Pull requests targeting `main`
- Manual workflow runs

Two independent CI jobs are used:

```text
CareNexus CI
├── backend-tests
└── frontend-compile
```

### Backend CI

The backend CI job:

1. Starts PostgreSQL 18
2. Creates an empty test database
3. Runs Flyway migration V1
4. Validates the schema using Hibernate
5. Runs the complete backend test suite

### Frontend CI

The frontend CI job:

1. Sets up Java 17
2. Restores Maven dependencies
3. Compiles the JavaFX frontend

This ensures that the committed project builds and tests successfully on a clean environment rather than relying only on the developer's local machine.

---

## Project Structure

```text
Clinic-insurance-system/
│
├── .github/
│   └── workflows/
│       └── ci.yml
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/rithika/clinicinsurance/
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── enums/
│   │   │   │   ├── exception/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── db/migration/
│   │   │       ├── application.properties
│   │   │       └── application-dev.properties
│   │   │
│   │   └── test/
│   │       ├── java/
│   │       └── resources/
│   │           └── application-test.properties
│   │
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/rithika/clinicsystem/
│   │       │   ├── api/
│   │       │   ├── dto/
│   │       │   ├── ui/
│   │       │   └── util/
│   │       │
│   │       └── resources/
│   │           └── styles/
│   │
│   └── pom.xml
│
├── .gitignore
├── LICENSE
└── README.md
```

---

## Prerequisites

Before running CareNexus locally, install:

- Java 17
- PostgreSQL
- Git

Maven does not need to be installed globally because both modules include the Maven Wrapper.

Check Java:

```bash
java -version
```

---

## Local Database Setup

Create the development database and role.

Example:

```sql
CREATE USER clinic_app WITH PASSWORD 'your-secure-password';

CREATE DATABASE clinic_insurance
    OWNER clinic_app;
```

For automated testing, create a separate database:

```sql
CREATE DATABASE clinic_insurance_test
    OWNER clinic_app;
```

Do not commit database passwords to Git.

Flyway creates the required CareNexus tables automatically in a new empty database.

---

## Running the Backend

Navigate to the backend module:

```bash
cd backend
```

Set the development database environment variables:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/clinic_insurance"
export DB_USERNAME="clinic_app"

read -s DB_PASSWORD
export DB_PASSWORD
```

Run the backend:

```bash
./mvnw spring-boot:run
```

Backend URL:

```text
http://localhost:8080
```

API base URL:

```text
http://localhost:8080/api
```

---

## Running the JavaFX Frontend

Open a second terminal:

```bash
cd frontend
```

Run:

```bash
./mvnw javafx:run
```

The frontend communicates with:

```text
http://localhost:8080/api
```

The backend should therefore be running before the frontend is used.

---

## Running Backend Tests

The test profile uses:

```text
clinic_insurance_test
```

Set the test database password:

```bash
read -s TEST_DB_PASSWORD
export TEST_DB_PASSWORD
```

Optional test database overrides:

```bash
export TEST_DB_URL="jdbc:postgresql://localhost:5432/clinic_insurance_test"
export TEST_DB_USERNAME="clinic_app"
```

Run:

```bash
cd backend
./mvnw test
```

Expected result:

```text
Tests run: 83
Failures: 0
Errors: 0
Skipped: 0
```

---

## Building the Frontend

```bash
cd frontend
./mvnw clean compile
```

---

## Typical Application Workflow

A typical CareNexus workflow is:

```text
1. Add Patient
2. Add Doctor
3. Book Appointment
4. Complete Appointment
5. Create Medical Record
6. Create Insurance Policy
7. Create Insurance Claim
8. Process Claim
```

This workflow demonstrates the relationships between the major domain entities and business rules.

---

## Engineering Highlights

This project demonstrates experience with:

- Object-oriented programming
- Java modular applications
- JavaFX desktop development
- RESTful API design
- Client-server architecture
- Layered backend architecture
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL relational modelling
- Database constraints
- Flyway migrations
- DTO-based API contracts
- Frontend and backend validation
- Structured exception handling
- Asynchronous JavaFX API calls
- JUnit testing
- Mockito
- MockMvc
- Git
- GitHub
- GitHub Actions CI

---

## Project Status

Core CareNexus functionality is complete and operational.

Current engineering features include:

- JavaFX desktop frontend
- Spring Boot REST backend
- PostgreSQL persistence
- Flyway-managed database schema
- Separate development and test environments
- 83 automated backend tests
- GitHub Actions continuous integration
- Backend and frontend validation
- Structured API error handling
- Referential-integrity protection
- Asynchronous frontend API operations

---

## Author

**Rithika Mandiv**  
Software Engineering Undergraduate

- GitHub: [rithikamandiv-ux](https://github.com/rithikamandiv-ux)
- Portfolio: [rithikamandiv.vercel.app](https://rithikamandiv.vercel.app)
- LinkedIn: [rithika-mandiv](https://www.linkedin.com/in/rithika-mandiv/)

---

## License

This project is licensed under the [MIT License](LICENSE).
```