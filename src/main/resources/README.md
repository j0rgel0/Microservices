# Authentication Service

The Authentication Service is a Spring Boot application that provides user management and authentication functionality. It includes the following features:

## Administrator Profiles:

- CRUD operations for administrator profiles.
- Paginated and optionally filtered list of administrator profiles.
- HATEOAS support for administrator profile endpoints.

## Manager Profiles:

- CRUD operations for manager profiles.
- List of manager profiles.
- HATEOAS support for manager profile endpoints.

## User Management:

- CRUD operations for users.
- Paginated and optionally filtered list of users.
- HATEOAS support for user endpoints.
- User authentication using email and password.

## Exception Handling:

Global exception handling with custom exceptions and appropriate error responses.

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Lombok
- HATEOAS

## Getting Started

1. Clone the repository:
   git clone **git@github.com:j0rgel0/Microservices.git**

2. Navigate to the project directory: **cd Microservices**

3. Run Postgres DB: **docker-compose up -d --build**

4. Build the project: **./gradlew build**

5. Start the application:  **./gradlew bootRun**

The application will be available at http://localhost:8080.

## API Endpoints

The API endpoints are available under the `/api/v1` prefix.

### Administrator Profiles

- `GET /api/v1/admin-profiles`: Retrieve a paginated and optionally filtered list of administrator profiles.
- `GET /api/v1/admin-profiles/{userId}`: Retrieve an administrator profile by the user ID.
- `POST /api/v1/admin-profiles`: Create a new administrator profile.
- `PUT /api/v1/admin-profiles/{userId}`: Update an existing administrator profile.
- `DELETE /api/v1/admin-profiles/{userId}`: Delete an administrator profile.

### Manager Profiles

- `GET /api/v1/manager-profiles`: Retrieve a list of manager profiles.
- `GET /api/v1/manager-profiles/{userId}`: Retrieve a manager profile by the user ID.
- `POST /api/v1/manager-profiles`: Create a new manager profile.
- `PUT /api/v1/manager-profiles/{userId}`: Update an existing manager profile.
- `DELETE /api/v1/manager-profiles/{userId}`: Delete a manager profile.

### Users

- `GET /api/v1/users`: Retrieve a paginated and optionally filtered list of users.
- `GET /api/v1/users/{id}`: Retrieve a user by their ID.
- `POST /api/v1/users`: Create a new user.
- `PUT /api/v1/users/{id}`: Update an existing user.
- `DELETE /api/v1/users/{id}`: Delete a user.
- `POST /api/v1/users/login`: Authenticate a user based on email and password.

## Exception Handling

The application uses a GlobalExceptionHandler to provide appropriate error responses for the following exceptions:

- `ResourceNotFoundException`: Returned when a requested resource is not found.
- `InvalidEmailException`: Returned when an email is in an invalid format.
- `EmptyEmailException`: Returned when an email is required but not provided.
- `DuplicateEmailException`: Returned when an email already exists.
- `EmptyPasswordException`: Returned when a password is required but not provided.
- `InvalidPasswordException`: Returned when a password does not meet the length requirements.
- `MethodArgumentTypeMismatchException`: Returned when an invalid UUID format is provided.
- `HttpMessageNotReadableException`: Returned when the request body is invalid or malformed.
- `Exception`: Returned for any other unexpected errors.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please feel free to create a new issue or submit a pull request.