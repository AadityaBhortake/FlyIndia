# Airline Reservation System

A comprehensive Java-based Airline Reservation System that consists of a client-side application for users to book flights and a server-side application for administration and backend processing.

## 🚀 Features

### Client Application (`airline_reservation`)
*   **User Authentication**: Secure login system for users.
*   **Flight Search**: Search for flights based on source, destination, date, and passenger count.
*   **Booking System**: Book tickets for available flights.
*   **Real-time Availability**: Checks seat availability before booking.

### Server Application (`airline_server`)
*   **Admin Dashboard**: GUI for administrators to manage the system.
*   **Flight Management**: Add, delete, and view flight schedules.
*   **Socket Server**: Handles concurrent client connections on port `6666`.
*   **Database Integration**: Manages all data persistence using MySQL.

## 🛠️ Tech Stack
*   **Language**: Java (Swing/AWT for GUI, Networking for Client-Server communication)
*   **Database**: MySQL
*   **IDE**: NetBeans (Project structure is NetBeans-compatible)

## 📋 Prerequisites
*   Java Development Kit (JDK) 8 or higher
*   MySQL Server
*   NetBeans IDE (Recommended for easy setup)

## 🗄️ Database Setup

1.  Install and start MySQL Server.
2.  Create a database named `airline`.
3.  Configure the database connection in `airline_server/src/airline_server/Database.java` if your credentials differ from the default:
    *   **URL**: `jdbc:mysql://127.0.0.1:3306/airline`
    *   **User**: `root`
    *   **Password**: `password`

### Database Schema
You need to create the following tables in the `airline` database:

**1. `user_login` Table**
```sql
CREATE TABLE user_login (
    iduser_login INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50)
);
```

**2. `flight` Table**
```sql
CREATE TABLE flight (
    flight_code VARCHAR(20) PRIMARY KEY,
    Airline VARCHAR(50),
    source VARCHAR(50),
    destination VARCHAR(50),
    departure VARCHAR(20),
    arrival VARCHAR(20),
    day INT,
    month VARCHAR(20),
    seat_available INT,
    price INT
);
```

**3. `booking` Table**
```sql
CREATE TABLE booking (
    flight_code VARCHAR(20),
    user_id INT,
    passenger_name VARCHAR(100),
    age INT,
    gender VARCHAR(10),
    phone VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES user_login(iduser_login)
);
```

**4. `admin_login` Table**
```sql
CREATE TABLE admin_login (
    idadmin_login INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(50),
    Password VARCHAR(50)
);
```

*(Note: Adjust column types and lengths as necessary based on your specific requirements.)*

## ⚙️ Installation & Running

### 1. Server Application
1.  Open the `airline_server` project in NetBeans or your preferred IDE.
2.  Ensure the MySQL JDBC driver is added to the project libraries.
3.  Run the `Login.java` file.
4.  Log in with your admin credentials (ensure you have inserted an admin into the `admin_login` table).
5.  Upon successful login, the Admin Console will open, and the Socket Server will start automatically on port `6666`.

### 2. Client Application
1.  Open the `airline_reservation` project in NetBeans or your preferred IDE.
2.  Run the `LoginPage.java` file.
3.  Login with a valid user (ensure you have inserted a user into the `user_login` table).
4.  Search and book flights!

## 📂 Project Structure

```
src/
├── airline_reservation/      # Client-side application
│   └── src/                  # Source code (GUI forms, Logic)
│       ├── LoginPage.java    # Entry point for client
│       └── ...
└── airline_server/           # Server-side application
    └── src/
        └── airline_server/   # Source code
            ├── Server.java   # Socket server implementation
            ├── Database.java # Database connection logic
            └── ...
```
