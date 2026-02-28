# HotelReservationSystem

## Overview
The Hotel Reservation System is a console-based Java application that allows users to create, view, update, and delete hotel room reservations. It also provides an admin feature to view all reservations in a tabular format. The system uses MySQL as the backend database for storing reservation details.

## Features
### User Features
**1. Create Room Reservation**
- Enter guest name, room number, and contact number.
- System generates a unique reservation ID.

**2. View Reservation Details**
  - Retrieve reservation details by entering the reservation ID.

**3.Get Room Number**
- Find the room number associated with the reservation ID.

**4. Update Reservation**
- Update guest name, room number, or contact number.
- Leave fields blank to retain previous values.

**5. Delete Reservation**
- Delete a reservation using its reservation ID.

### Admin Feature
**6. View all Reservations**
- Display all reservations in a table with reservation ID, guest name, room number, contact number, and reservation date.

### Exit
**7. Exit System**
- Gracefully closes the application and database connection.

## Technologies Used
- **Programming language:** Java
- **Database:** MySQL
- **JDBC:** Java Databse Connectivity for database operations
- **Scanner:** For console-based user input

## Database Schema
**Database:** `hotel_db`

**Table:** `hotel_reservations`

| Column Name       | Data Type                         | Notes                             |
|------------------|----------------------------------|----------------------------------|
| reservation_id    | INT AUTO_INCREMENT PRIMARY KEY    | Unique reservation identifier     |
| guest_name        | VARCHAR(50)                       | Name of the guest                 |
| room_number       | INT                               | Reserved room number              |
| contact_number    | VARCHAR(15)                       | Guest contact number              |
| reservation_date  | TIMESTAMP DEFAULT CURRENT_TIMESTAMP | Reservation creation date        |

**SQL to create table:**

```sql
CREATE DATABASE IF NOT EXISTS hotel_db;

USE hotel_db;

CREATE TABLE IF NOT EXISTS hotel_reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    guest_name VARCHAR(50) NOT NULL,
    room_number INT NOT NULL,
    contact_number VARCHAR(15) NOT NULL,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
## How to Run

1. Ensure MySQL is installed and running on your system.
2. Create the database and table as shown above.
3. Update the database connection details in `HotelReservationSystem.java` :

```Java
String url = "jdbc:mysql://localhost:3306/hotel_db";
String username = "root";
String password = "your_password_here";
```
4. Compile and run the Java program:

```Bash
javac HotelReservationSystem.java
java Reservation.HotelReservationSystem
```

5. Follow the console prompts to interact with the system.

## Notes
- Ensure the room number is entered as a valid integer.
- When updating reservations, press Enter without typing to keep existing values.
- Only admin should use the View All Reservations feature.
- Input validation and error handling are implemented for smooth user experience.
