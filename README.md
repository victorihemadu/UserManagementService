User Management Service
A microservice for user management built with Java and Spring Boot.
Features

RESTful API for user management (CRUD operations)
JWT-based authentication
Role-based authorization
In-memory H2 database for testing
Comprehensive unit tests
API documentation with Swagger/OpenAPI

Technologies Used

Java 11
Spring Boot 2.7
Spring Security
Spring Data JPA
H2 Database
JWT Authentication
Lombok
Swagger/OpenAPI
JUnit 5

API Endpoints
Authentication

POST /api/auth/login - Authenticate user and get JWT token

User Management

POST /api/users - Create a new user (Admin only)
GET /api/users/{id} - Get user by ID
PUT /api/users/{id} - Update user
DELETE /api/users/{id} - Delete user (Admin only)
GET /api/users - Get all users (Admin only)

Running the Application

Clone the repository:

bashgit clone https://github.com/yourusername/user-management-service.git
cd user-management-service

Build the application:

bash./mvnw clean install

Run the application:

bash./mvnw spring-boot:run

Access the application:

API: http://localhost:8080/api
Swagger UI: http://localhost:8080/swagger-ui.html
H2 Console: http://localhost:8080/h2-console



Default Credentials
The application is pre-configured with two users:

Admin User:

Username: admin
Password: password
Role: ADMIN


Regular User:

Username: user
Password: password
Role: USER



Testing
Run the tests using:
bash./mvnw test
API Documentation
The API documentation is available at:

http://localhost:8080/swagger-ui.html
http://localhost:8080/api-docs
