# Contact Manager

A Java contact management application built with **Test-Driven Development (TDD)**, developed for the Advanced Techniques and Tools for Software Development (ATTSW) course at the University of Florence.

[![Build Status](https://github.com/mohamedjama-farah/Contact-Manager-/actions/workflows/build.yml/badge.svg)](https://github.com/mohamedjama-farah/Contact-Manager-/actions/workflows/build.yml)
[![Coverage Status](https://coveralls.io/repos/github/mohamedjama-farah/Contact-Manager-/badge.svg?branch=main)](https://coveralls.io/github/mohamedjama-farah/Contact-Manager-?branch=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mohamedjama-farah_Contact-Manager-&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mohamedjama-farah_Contact-Manager-)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=mohamedjama-farah_Contact-Manager-&metric=coverage)](https://sonarcloud.io/summary/new_code?id=mohamedjama-farah_Contact-Manager-)

## Description

Contact Manager allows users to:
- Add contacts (name, phone, email)
- View all contacts in a list
- Delete contacts
- Persist contacts in a MongoDB database

## Architecture — MVP Pattern

The project follows the **Model-View-Presenter (MVP)** pattern:

```
ContactManagerApp (main)
        |
        +--> ContactController  (business logic)
                |          |
        ContactRepository  ContactView
                |                |
  MongoContactRepository   ContactFrame (Swing GUI)
```

- **Model**: `Contact`, `ContactGroup`
- **View**: `ContactView` (interface), `ContactFrame` (Swing implementation)
- **Controller**: `ContactController` — the only class with business logic
- **Repository**: `ContactRepository` (interface), `MongoContactRepository` (MongoDB implementation)

## Technologies

| Tool | Purpose |
|---|---|
| Java 17 | Programming language |
| Maven | Build automation |
| JUnit 4 | Unit testing framework |
| Mockito | Mocking framework for unit tests |
| AssertJ | Fluent assertion library |
| AssertJ-Swing | GUI testing library |
| MongoDB | Database |
| Testcontainers | Real MongoDB in Docker for integration tests |
| JaCoCo | Code coverage (enforces 100%) |
| PIT | Mutation testing |
| SonarCloud | Static analysis and quality gate |
| Coveralls | Coverage reporting |
| GitHub Actions | Continuous Integration |

## Test Strategy

The project uses three levels of testing:

### Unit Tests (`src/test/java`)
- `ContactTest` — tests the Contact model
- `ContactGroupTest` — tests the ContactGroup model
- `ContactControllerTest` — tests business logic with Mockito fakes
- `ContactFrameTest` — tests the Swing GUI with AssertJ-Swing

### Integration Tests (`src/it/java`)
- `MongoContactRepositoryIT` — tests real MongoDB operations with Testcontainers

### End-to-End Tests (`src/it/java`)
- `ContactManagerE2EIT` — tests the full application stack (GUI + Controller + real MongoDB)

## Build and Run

### Prerequisites
- Java 17
- Maven 3.x
- Docker (for integration tests)

### Run all tests
```bash
mvn verify
```

### Run unit tests only
```bash
mvn test
```

### Run mutation testing
```bash
mvn test-compile && mvn org.pitest:pitest-maven:mutationCoverage
```

### Run the application
```bash
# Start MongoDB first
docker run -d -p 27017:27017 mongo:6.0

# Then run
mvn compile exec:java -Dexec.mainClass="com.example.contactmanager.app.ContactManagerApp"
```

## Coverage Results

- **Line coverage**: 100%
- **Branch coverage**: 100%
- **Mutation score**: 100% (5/5 mutations killed, 0 survived)

## CI/CD Pipeline

Every push to any branch triggers GitHub Actions which:
1. Compiles the project
2. Runs unit tests (Surefire)
3. Runs integration and E2E tests (Failsafe) inside Docker with a real MongoDB
4. Enforces 100% line and branch coverage via JaCoCo
5. Sends analysis to SonarCloud
6. Reports coverage to Coveralls

## Author

Mohamed Jama Farah  
University of Florence — ATTSW Course  
Professor: Lorenzo Bettini
