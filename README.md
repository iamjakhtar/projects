
# Projects Management Application

This is a Spring Boot application designed for managing fictional projects. Each project has attributes such as name, description, budget, and an associated image. The application supports basic CRUD operations (Create, Read, Update, Delete) through RESTful APIs. Additionally, it provides functionality to upload and manage images for each project.

## Table of Contents

1. [Overview](#overview)
2. [Setup Instructions](#setup-instructions)
3. [API Endpoints](#api-endpoints)
4. [Code Explanation](#code-explanation)
5. [Future Features](#future-features)

## Overview

This project is a comprehensive Spring Boot application designed to manage a collection of fictional projects. The key features of this application include:

- **Project Management**: Create, Read, Update, and Delete projects.
- **Image Upload**: Upload images associated with projects, and manage them efficiently.
- **RESTful APIs**: Expose endpoints for various CRUD operations, making it easy to interact with the application.
- **MySQL Integration**: Store project data in a MySQL database.
- **CORS Configuration**: Allow cross-origin requests from a specific frontend application running on `http://localhost:3000`.

The application is structured to separate concerns, with controllers handling HTTP requests, services containing business logic, and repositories interacting with the database.

## Setup Instructions

### Prerequisites

- Java 17 or later
- Maven 3.6.3 or later
- MySQL 8.0 or later

### Configuration

1. **Database Configuration**:
   Ensure you have a MySQL database running and update the `application.properties` file with your database credentials.

   ```properties
   spring.datasource.url=jdbc:mysql://sql8.freesqldatabase.com:3306/sql8720980
   spring.datasource.username=sql8720980
   spring.datasource.password=8UZxGMVu4z
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.open-in-view=false
   spring.jpa.database=mysql
   spring.servlet.multipart.max-file-size=10MB
   spring.servlet.multipart.max-request-size=10MB
   ```

2. **File Upload Configuration**:
   Adjust the maximum file upload size as needed.

   ```properties
   spring.servlet.multipart.max-file-size=10MB
   spring.servlet.multipart.max-request-size=10MB
   ```

### Running the Application

1. **Clone the Repository**:
   ```sh
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Build the Project**:
   ```sh
   mvn clean install
   ```

3. **Run the Application**:
   ```sh
   mvn spring-boot:run
   ```

   Alternatively, you can run the `ProjectsApplication` class directly from your IDE.

4. **Access the Application**:
   The application will be running at `http://localhost:8080`.

### Accessing the API

- Base URL: `http://localhost:8080`

## API Endpoints

### Project Endpoints

- **Get all projects**: `GET /projects`
- **Get a project by ID**: `GET /projects/{id}`
- **Create a new project**: `POST /projects/add-project`
  - Request Parameters:
    - `name` (String)
    - `description` (String)
    - `budget` (int)
    - `image` (MultipartFile)
- **Update an existing project**: `PUT /projects/{id}`
  - Request Parameters:
    - `name` (String)
    - `description` (String)
    - `budget` (int)
    - `image` (MultipartFile, optional)
    - `imageUrl` (String, optional)
- **Delete a project**: `DELETE /projects/{id}`

## Code Explanation

### application.properties

This file contains the configuration properties for the Spring Boot application, including database connection details and file upload size limits.

### WebConfig.java

This configuration class handles CORS and resource handling for file uploads.

### ProjectRepository.java

This interface extends `JpaRepository` to provide CRUD operations for the `Project` entity.

### ProjectService.java

This service class contains the business logic for managing projects, including saving images.

### ProjectDto.java

This is a Data Transfer Object (DTO) for transferring project data.

### Project.java

This is the Project entity class mapped to the database.

### ProjectController.java

This controller class handles HTTP requests for project-related operations.

### ProjectsApplication.java

This is the main entry point of the Spring Boot application.

## Future Features


- **Security**:
  - Integrate Spring Security to handle authentication and authorization.
  - Implement role-based access control.

- **Testing**:
  - Add unit tests and integration tests using JUnit and Mockito.
  - Ensure comprehensive test coverage for all components of the application.

Feel free to contribute to this project by submitting pull requests or opening issues.

---
