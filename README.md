# 💼 Job Application Tracker

*A robust backend project exploring modern Java & Kotlin interoperability with Spring Boot and Vaadin.*

## 📋 Overview

This project was born from a need for a personal job application tracker. It serves as a practical exploration of Production-Grade Backend Development, focusing on clean architecture, strict validation, and a comprehensive testing strategy.

The project handles data persistence, business logic, and error handling for a frontend client (Vaadin), utilizing a full REST API.

## 🛠️ Tech Stack

* **Core Languages:** Java & Kotlin
* **Framework:** Spring Boot 4.0.0
* **Build Tool:** Maven
* **Database:** Postgres (Production), H2 (Testing)
* **Testing:** JUnit5, Mockito, Spring Boot Test, MockMvc (Kotlin DSL), AssertJ
* **CI/CD:** GitHub Actions
* **User Interface:** Vaadin

## ✨ Key Features

* **Data Visualization:** Records are fetched to the Frontend and are fully customizable.
* **RESTful API:** Fully compliant REST endpoints for creating, reading, updating, and deleting job applications.
* **Robust Validation:** Data integrity enforced via Jakarta Validation.
* **Automated CI Pipeline:** Every push is verified against a complete test suite via GitHub Actions.

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
