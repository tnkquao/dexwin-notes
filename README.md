# dexwin-notes
# 📝 Notes Application

A simple **Spring Boot** web application for creating, viewing, and managing notes.  
It supports **JWT-based authentication**, **Thymeleaf UI**, and **PostgreSQL** as the database.

---

## 🚀 Features
- User authentication (login, logout)
- Create, edit, delete, and view notes
- Tags support for categorizing notes
- JWT stored in HTTP-only cookies
- Secure session logout (invalidates token)
- Database migration using Flyway
- Dockerized PostgreSQL setup

---

## 🧰 Tech Stack
- **Backend:** Spring Boot 3.x, Spring Security, Spring Data JPA
- **Frontend:** Thymeleaf, Bootstrap
- **Database:** PostgreSQL
- **Tools:** Docker, Flyway

---

## ⚙️ Prerequisites
Before running the project, make sure you have the following installed:

- [Java 21](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)
- [Postman](https://www.postman.com/) *(optional, for testing API endpoints)*

---

## 🐳 Setting up PostgreSQL with Docker

1. Start the database container:
   ```bash
   docker compose up -d
   docker ps

## 🛠️ Running the Application
Clone the repository:

## 🧩 Configuration
- ### Set environment variables

## 🧹 Cleanup
To stop and remove containers and volumes:



To Run
- 