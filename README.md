# CareNexus

### Healthcare Operations & Insurance Management System

[![CareNexus CI](https://github.com/rithikamandiv-ux/Clinic-insurance-system/actions/workflows/ci.yml/badge.svg)](https://github.com/rithikamandiv-ux/Clinic-insurance-system/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-17-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-4169E1)
![License](https://img.shields.io/badge/License-MIT-green)

CareNexus is a desktop-based healthcare operations and insurance management system built using a client-server architecture.

The application combines a **JavaFX desktop frontend** with a **Spring Boot REST API** and a **PostgreSQL database**. It supports the management of patients, doctors, appointments, medical records, insurance policies, and insurance claims while enforcing business rules, validation, and relational integrity.

This project was developed as a portfolio-level software engineering project with emphasis on **object-oriented programming, layered architecture, REST API development, relational database design, automated testing, database migrations, and continuous integration**.

---

## Features

### Dashboard

- Displays clinic operational summaries
- Provides navigation to all major application areas
- Presents information across patients, doctors, appointments, medical records, insurance policies, and insurance claims

### Patient Management

- Add new patients
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
- Enforce one medical record per appointment
- Prevent medical-record creation for appointments that are not completed
- Strict medical-record ID format: `MR###`

### Insurance Policy Management

- Create insurance policies for patients
- View policy information
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
- Process pending insurance claims
- Approve or reject claims according to insurance coverage
- Enforce one insurance claim per medical record
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

The JavaFX frontend does not access the PostgreSQL database directly.

All application data flows through the Spring Boot REST API.

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
| Spring Boot 4.1.1 | Backend application framework |
| Spring Web MVC | REST API development |
| Spring Data JPA | Persistence abstraction |
| Hibernate | ORM and database schema validation |
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

The backend follows a layered architecture.

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

- Exposing REST endpoints
- Receiving HTTP requests
- Validating request data
- Mapping request and response DTOs
- Returning appropriate HTTP status codes

### Service Layer

Responsible for:

- Business rules
- Entity relationships
- State transitions
- Resource validation
- Domain operations
- Coordinating repository operations

### Repository Layer

Responsible for:

- Database access
- JPA persistence
- Entity retrieval
- Database queries

---

## REST API

The backend exposes REST endpoints for the main CareNexus resources.

```text
/api/patients
/api/doctors
/api/appointments
/api/medical-records
/api/insurance-policies
/api/insurance-claims
```

The API uses standard HTTP methods.

```text
GET     Read resources
POST    Create resources
PUT     Update resources
PATCH   Perform state-changing operations
DELETE  Delete resources
```

Create and update operations use separate DTO contracts. This prevents immutable resource identifiers from having to be resubmitted when an existing resource is updated.

---

## Validation

CareNexus performs validation at both frontend and backend levels.

Validation includes:

- Required-field validation
- Age validation
- Phone-number validation
- Positive monetary values
- Decimal precision validation
- Strict resource ID formats
- Duplicate-resource prevention
- Relationship validation
- Appointment state validation
- Request-body validation
- Path-variable validation

### Resource ID Formats

| Resource | Format | Example |
|---|---|---|
| Patient | `P###` | `P001` |
| Doctor | `D###` | `D001` |
| Appointment | `A###` | `A001` |
| Medical Record | `MR###` | `MR001` |
| Insurance Policy | `POL###` | `POL001` |
| Insurance Claim | `CL###` | `CL001` |

Malformed resource identifiers are rejected before reaching the service layer.

---

## Error Handling

The backend uses structured API error responses.

The JavaFX frontend interprets these responses and displays user-readable error messages.

Common HTTP responses include:

| Status | Meaning |
|---|---|
| `400 Bad Request` | Invalid input or malformed request |
| `404 Not Found` | Requested resource does not exist |
| `409 Conflict` | Operation conflicts with existing resource relationships |
| `500 Internal Server Error` | Unexpected backend failure |

Foreign-key relationships are protected so dependent healthcare and insurance records cannot be accidentally removed.

---

## Database Design

CareNexus currently uses six primary domain tables.

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

Important database constraints include:

- One insurance policy per patient
- One medical record per appointment
- One insurance claim per medical record
- Appointments reference valid patients
- Appointments reference valid doctors
- Medical records reference valid appointments
- Insurance policies reference valid patients
- Insurance claims reference valid medical records

---

## Database Migrations

Database schema management is handled using **Flyway**.

The initial database migration is located at:

```text
backend/src/main/resources/db/migration/
└── V1__baseline_schema.sql
```

For a new empty database, Flyway creates the required CareNexus schema before Hibernate validates the entity mappings.

Hibernate is configured with:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

This means:

```text
Flyway
└── Owns database schema creation and evolution

Hibernate
└── Validates entity-to-database compatibility
```

Future database changes should be introduced using additional Flyway migrations.

For example:

```text
V2__description.sql
V3__description.sql
V4__description.sql
```

---

## Automated Testing

The CareNexus backend currently contains **83 automated tests**.

The test suite covers:

- Patient service operations
- Doctor service operations
- Appointment lifecycle rules
- Medical-record business rules
- Insurance-policy rules
- Insurance-claim processing
- Request validation
- Path-variable ID validation
- Update DTO validation
- Spring application context startup

Testing technologies include:

- JUnit 5
- Mockito
- MockMvc
- PostgreSQL test database

Development and testing use separate databases.

```text
Development Database
clinic_insurance

Test Database
clinic_insurance_test
```

This prevents automated tests from affecting development data.

---

## Continuous Integration

CareNexus uses **GitHub Actions** for continuous integration.

The workflow is located at:

```text
.github/workflows/ci.yml
```

The workflow runs automatically for:

- Pushes to `main`
- Pull requests targeting `main`
- Manual workflow executions

Two independent CI jobs are executed.

```text
CareNexus CI
├── backend-tests
└── frontend-compile
```

### Backend CI

The backend CI job:

1. Creates a clean Ubuntu environment
2. Starts a PostgreSQL 18 service
3. Creates an empty test database
4. Runs the Flyway migration
5. Validates the resulting schema using Hibernate
6. Executes the complete backend automated test suite

### Frontend CI

The frontend CI job:

1. Creates a clean Ubuntu environment
2. Sets up Java 17
3. Restores Maven dependencies
4. Compiles the JavaFX frontend

This ensures that CareNexus builds and tests successfully in a clean environment rather than relying only on the developer's local machine.

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
│   │   │       ├── db/
│   │   │       │   └── migration/
│   │   │       │       └── V1__baseline_schema.sql
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

Maven does not need to be installed globally because the project includes Maven Wrapper scripts.

Verify Java:

```bash
java -version
```

---

## Local Database Setup

Create a PostgreSQL role for the application.

```sql
CREATE USER clinic_app WITH PASSWORD 'your-secure-password';
```

Create the development database.

```sql
CREATE DATABASE clinic_insurance
    OWNER clinic_app;
```

Create a separate test database.

```sql
CREATE DATABASE clinic_insurance_test
    OWNER clinic_app;
```

Database passwords should never be committed to Git.

Flyway automatically creates the required application tables when CareNexus starts against a new empty database.

---

## Running the Backend

Navigate to the backend module.

```bash
cd backend
```

Set the required development database environment variables.

```bash
export DB_URL="jdbc:postgresql://localhost:5432/clinic_insurance"
export DB_USERNAME="clinic_app"

read -s DB_PASSWORD
export DB_PASSWORD
```

Start the Spring Boot backend.

```bash
./mvnw spring-boot:run
```

The backend runs at:

```text
http://localhost:8080
```

The REST API is available under:

```text
http://localhost:8080/api
```

---

## Running the JavaFX Frontend

Open another terminal and navigate to the frontend module.

```bash
cd frontend
```

Start the JavaFX application.

```bash
./mvnw javafx:run
```

The JavaFX frontend communicates with the local backend at:

```text
http://localhost:8080/api
```

The backend should therefore be running before the frontend is used.

---

## Running Backend Tests

CareNexus uses a dedicated PostgreSQL test database.

Set the test database password.

```bash
read -s TEST_DB_PASSWORD
export TEST_DB_PASSWORD
```

The following values can also be overridden if required.

```bash
export TEST_DB_URL="jdbc:postgresql://localhost:5432/clinic_insurance_test"
export TEST_DB_USERNAME="clinic_app"
```

Navigate to the backend.

```bash
cd backend
```

Run the test suite.

```bash
./mvnw test
```

Expected test result:

```text
Tests run: 83
Failures: 0
Errors: 0
Skipped: 0
```

---

## Building the Frontend

Navigate to the frontend module.

```bash
cd frontend
```

Compile the frontend.

```bash
./mvnw clean compile
```

---

## Typical Application Workflow

A typical workflow through CareNexus is:

```text
1. Add Patient
2. Add Doctor
3. Book Appointment
4. Complete Appointment
5. Create Medical Record
6. Create Insurance Policy
7. Create Insurance Claim
8. Process Insurance Claim
```

This workflow demonstrates the relationships between the major CareNexus domain entities and the business rules enforced by the system.

---

## Engineering Highlights

CareNexus demonstrates practical experience with:

- Object-oriented programming
- Java modular applications
- JavaFX desktop development
- Client-server architecture
- RESTful API design
- Layered backend architecture
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- PostgreSQL relational modelling
- Database relationships and constraints
- Flyway database migrations
- DTO-based API contracts
- Frontend validation
- Backend validation
- Structured exception handling
- Referential-integrity protection
- Asynchronous JavaFX API operations
- JUnit 5
- Mockito
- MockMvc
- Maven
- Git
- GitHub
- GitHub Actions CI

---

## Project Status

The core CareNexus system is complete and operational.

Implemented engineering features include:

- JavaFX desktop frontend
- Spring Boot REST backend
- PostgreSQL persistence
- Six connected healthcare and insurance domains
- Full CRUD operations where appropriate
- Appointment lifecycle management
- Insurance claim processing
- Flyway-managed database schema
- Separate development and test environments
- 83 automated backend tests
- GitHub Actions continuous integration
- Frontend and backend validation
- Strict resource ID validation
- Structured API error handling
- Foreign-key integrity protection
- Asynchronous frontend API operations
- Loading and duplicate-action protection
- UI accessibility and usability improvements

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