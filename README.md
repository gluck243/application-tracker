# 💼 Job Application Tracker

[![Application Tracker CI with Maven](https://github.com/gluck243/application-tracker/actions/workflows/ci.yaml/badge.svg)](https://github.com/gluck243/application-tracker/actions/workflows/ci.yaml)

## [>>>Try application here<<<](https://job-application-tracker.up.railway.app/)
## [>>>View REST Endpoints here<<<](https://job-application-tracker.up.railway.app/swagger-ui/index.html)

*A robust backend project exploring modern Java & Kotlin interoperability with Spring Boot and Vaadin.*

## 📋 Overview

This project was born from a need for a personal job application tracker. It serves as a practical exploration of Production-Grade Backend Development, focusing on clean architecture, strict validation, and a comprehensive testing strategy.

The project handles data persistence, business logic, and error handling for a frontend client (Vaadin), utilizing a full REST API.

## 🛠️ Tech Stack

* **Core Languages:** Java 21 & Kotlin 2.2.0
* **Framework:** Spring Boot 4.0.0
* **Build Tool:** Maven
* **Database:** Postgres (Production & Testing)
* **Testing:** JUnit5, Mockito, Spring Boot Test, MockMvc (Kotlin DSL), AssertJ
* **CI:** GitHub Actions
* **Containerization:** Docker
* **User Interface:** Vaadin
* **Security:** Spring Security

## ✨ Key Features

* **Data Visualization:** Records are fetched to the Frontend and are fully customizable.
* **RESTful API:** Fully compliant REST endpoints for creating, reading, updating, and deleting job applications.
* **Robust Validation:** Data integrity enforced via Jakarta Validation.
* **Automated CI Pipeline:** Every push is verified against a complete test suite via GitHub Actions.
* **Endpoint protection and Login system:** Application REST endpoints and interface are secured utilizing Spring Security

## 🚀 Quick Start
1.  Clone the repository.
    ```bash
    git clone https://github.com/gluck243/application-tracker.git
    ```
2.  Create a `.env` file in the root directory. Copy contents of `dotenv` file into it replace and fill the data in
    ```.env
    POSTGRES_USER=your_username
    POSTGRES_PASSWORD=your_password
    ADMIN_USER=your_username
    ADMIN_PASSWORD=your_password
    ```
3.  Build the application:
    ```bash
    mvn clean package -DskipTests
    ```
4.  Launch the stack (App + Database):
    ```bash
    docker compose up --build
    ```
5. Access the Application
  * [http://localhost:8080/](http://localhost:8080/)
6. Access the API Documentation:
  * [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## ↔️ API Endpoints (For more information on JSON formatting and return values check the link at the top)

* `GET /api/jobs` - Returns the list of all indexed job application.
* `GET /api/jobs/{id}` - Accepts the valid job application id. Returns the job application by the provided id. 
* `POST /api/jobs` - Accepts valid JSON payload. Returns an id of the indexed job application.
* `PUT /api/jobs/{id}` - Accepts the valid job application id and the valid JSON payload. Returns the updated job application.
* `DELETE /api/jobs/{id}` - Accepts the valid job application id. Returns NO CONTENT if deletion is successful

## 🧠 What I Learned

This project was a great exercise in configuration and language interoperability:

* **Mixed-Language Builds:** I learned how to configure the `kotlin-maven-plugin` to compile Kotlin code before Java, allowing me to call Kotlin classes from Java and vice versa seamlessly.
* **UI with Vaadin:** Learned how to build fully functional UI with Vaadin utilizing such components as Grid, Form, Dialog etc.
* **The Testing Pyramid:** I moved beyond simple assertions to build a complete testing strategy:
    * Unit Tests for Logic & Mappers.
    * Slice Tests (@WebMvcTest) for the Controller layer.
    * Integration Tests (@SpringBootTest) for the full database-to-API lifecycle.
    * Spring Architecture: Strictly decoupled layers to ensure maintainability.
* **CI/CD Integration:** Configured GitHub Actions to act as a quality gate using Maven.
* **Security:** Secured the application utilizing full security filter chain functionality including UserCredentialsService, PasswordEncoder, CORS etc.
