# Job Application Tracker

*A practice project exploring Java & Kotlin interoperability with Spring Boot and Vaadin.*

## 📋 Overview

This project was born from a need for a personal job application tracker. Application features a frontend fully built on the backend side of the project using Vaadin, connected to a PostgreSQL database for data persistence.

The primary goal of this project was to practice:
* Setting up a mixed-language build environment with **Maven**.
* Implementing UI build fully on server side utilizing **Vaadin**.
* Writing clean, maintainable code for a common real-world use case.

## 🛠️ Tech Stack

* **Core Languages:** Java & Kotlin
* **Framework:** Spring Boot 4.0.0
* **Build Tool:** Maven
* **Database:** Postgres
* **Testing:** JUnit5 and Mockkito
* **User Interface:** Vaadin

## ✨ Key Features

* **Data Visualization:** Records are fetched straight from a database into a table.
* **Customization:** Allows for addition of new records and change to existing ones.

## 🧠 What I Learned

This project was a great exercise in configuration and language interoperability:

* **Mixed-Language Builds:** I learned how to configure the `kotlin-maven-plugin` to compile Kotlin code before Java, allowing me to call Kotlin classes from Java and vice versa seamlessly.
* **UI with Vaadin:** Learned how to build fully functional UI with Vaadin utilising such components as Grid, Form, Dialog etc.
* **Clean Code:** Focused on keeping the UI layer logic decoupled both from it's own components and from the JPA layer.
