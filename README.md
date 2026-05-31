# MoodNotes
A smart personal journal app that tracks your mood and sends weekly sentiment email reports.

## Features
- JWT Authentication & Authorization
-  Role-Based Access (User & Admin)
-  Journal Entry CRUD
-  Mood/Sentiment Tracking (HAPPY, SAD, ANGRY, ANXIOUS)
-  Weekly Mood Email every Sunday
-  Live Weather on Greeting
-  Google OAuth2 Login
-  Swagger API Documentation

## Tech Stack
Java 21 | Spring Boot 2.7 | MongoDB | JWT | Spring Security | Spring Mail | OpenWeatherMap API | Google OAuth2 | Swagger | Lombok

## Setup
1. Install Java 21, Maven, MongoDB
2. Update `application.yml` with your credentials
3. Run: `mvn spring-boot:run`
4. Swagger: `http://localhost:8080/moodnotes/swagger-ui/index.html`
