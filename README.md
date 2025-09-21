# SSO Spring Boot Application

## Overview
This project is a Single Sign-On (SSO) web application built with Spring Boot. It demonstrates secure user registration, login, OTP-based account activation, and automatic cleanup of inactive accounts. Passwords are securely hashed using BCrypt. The application uses H2 as the database and Thymeleaf for the frontend.

## Features
- **User Registration:**
  - Registers new users with email, username, and password.
  - Passwords are hashed before storage.
  - After registration, users must log in and activate their account via OTP within 30 seconds, or their account will be deleted.
- **Login:**
  - Users log in with username and password.
  - Passwords are verified using BCrypt.
  - On successful login, users are redirected to the OTP page.
- **OTP Activation:**
  - Users must enter a correct OTP to activate their account.
  - After successful OTP, users are redirected to the home page.
- **Account Cleanup:**
  - A scheduled task runs every 10 seconds to delete users who have not activated their account within 30 seconds of registration.
- **Logging:**
  - All key actions (registration, login, OTP, cleanup) are logged using SLF4J.

## Technologies Used
- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- H2 Database
- Thymeleaf
- SLF4J (logging)
- BCrypt (password hashing)

## Project Structure
```
src/main/java/org/example/
  ├── config/              # Security and authorization configuration
  ├── controller/          # MVC controllers (Home, Login, Register)
  ├── model/               # Entity and response models
  ├── repository/          # Spring Data JPA repositories
  ├── scheduler/           # Scheduled tasks (user cleanup)
  ├── service/             # Service interfaces and implementations
src/main/resources/
  ├── templates/           # Thymeleaf HTML templates
  ├── static/css/          # CSS files
  ├── application.properties # App configuration
```

## How to Run
1. **Build the project:**
   ```bash
   mvn clean install
   ```
2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
3. **Access the app:**
   - Registration/Login: [http://localhost:8086/login](http://localhost:8086/login)
   - H2 Console: [http://localhost:8086/h2-console](http://localhost:8086/h2-console)
     - JDBC URL: `jdbc:h2:file:./data/sso-db`
     - User: `sa`
     - Password: (leave blank)

## Usage
- **Register:** Go to the login page and click "Register here". Fill in your details.
- **Login:** Enter your username and password. If correct, you will be redirected to the OTP page.
- **OTP:** Enter the OTP (default: `686856`). On success, you will be redirected to the home page.
- **Account Cleanup:** If you do not activate your account within 30 seconds of registration, your account will be deleted automatically.

## Security Notes
- Passwords are hashed using BCrypt before storage.
- All sensitive actions are logged for auditing.
- Spring Security is configured to allow custom login and registration flows.

## Customization
- Change the OTP logic in `UserServiceImpl.java` as needed.
- Update scheduled cleanup interval in `UserCleanupTask.java`.
- Modify templates in `src/main/resources/templates/` for UI changes.

## License
This project is for educational/demo purposes.

