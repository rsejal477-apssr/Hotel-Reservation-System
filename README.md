# 🏨 Hotel Reservation System

A desktop-based **Hotel Reservation System built with Java Swing** that allows users to search for rooms, make reservations, check availability, manage bookings, and simulate payments through a simple graphical interface.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Java Swing](https://img.shields.io/badge/Java%20Swing-GUI-2563EB?style=for-the-badge)
![OOP](https://img.shields.io/badge/Concepts-OOP-7C3AED?style=for-the-badge)
![Storage](https://img.shields.io/badge/Storage-File%20I%2FO-0F766E?style=for-the-badge)

---

## ✨ Features

- 🔍 **Room Search:** Search available rooms by check-in date, check-out date, and room category.
- 🛏️ **Room Categories:** Choose from Standard, Deluxe, and Suite rooms.
- 📅 **Availability Management:** Prevent overlapping reservations for the same room.
- 📝 **Reservation Booking:** Create bookings with guest details and automatically generated booking IDs.
- 💰 **Price Calculation:** Calculate the total booking cost based on the room rate and number of nights.
- 💳 **Payment Simulation:** Record a simulated payment without processing real money.
- ❌ **Cancellation:** Cancel reservations and make the room available again for the relevant dates.
- 📋 **Booking Details:** View reservation information and browse saved bookings in a table.
- 💾 **Persistent Storage:** Save booking information locally so it remains available after restarting the application.

## 🖥️ Application Overview

The application provides three main sections:

| Section | Purpose |
|---|---|
| Search & Book | Find available rooms and create reservations |
| Manage Booking | View details, simulate payments, and cancel bookings |
| All Bookings | View saved reservation records in a table |

## 🛏️ Room Categories & Pricing

The project uses the following sample room rates:

| Room Category | Room Numbers | Price per Night |
|---|---|---:|
| Standard | 101, 102 | ₹1,800 |
| Deluxe | 201, 202 | ₹2,800 |
| Suite | 301, 302 | ₹4,500 |

*These are demonstration prices and can be modified in `HotelService.java`.*

## 🛠️ Technology Stack

- **Language:** Java
- **GUI:** Java Swing
- **Programming Concepts:** Object-Oriented Programming (OOP)
- **Data Storage:** Java Object Serialization and File I/O
- **IDE:** IntelliJ IDEA
- **Version Control:** Git and GitHub

  ## Application Screenshot
  ![Hotel Reservation System GUI]
  (./hotel.png)

## 📂 Project Structure

```text
Hotel-Reservation-System/
│
├── src/
│   ├── Main.java
│   ├── Room.java
│   ├── Reservation.java
│   └── HotelService.java
│
├── data/
│   └── bookings.dat       # Generated locally at runtime
│
├── .gitignore
└── README.md
```

The `data/` directory and `bookings.dat` file are generated locally when the application saves booking records. They do not need to be committed to GitHub.

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or later
- IntelliJ IDEA or another Java IDE

### Run in IntelliJ IDEA

1. Clone or download this repository.
2. Open the project folder in IntelliJ IDEA.
3. Configure a JDK if IntelliJ prompts you to do so.
4. Mark `src` as **Sources Root** if necessary.
5. Open `Main.java`.
6. Click the green **Run ▶** button next to the `main()` method.

### Run from the Terminal

From the project root directory, compile the source files:

```bash
javac -d out src/*.java
```

Run the application:

```bash
java -cp out Main
```

These commands work with a configured JDK and a terminal opened in the project root.

## 💾 How Data Storage Works

The application stores reservations in a local file named `data/bookings.dat`.

- Booking details are serialized and saved to disk.
- Saved reservations are loaded when the application starts.
- Room availability is calculated from confirmed reservations and their date ranges.
- Cancelled reservations no longer block room availability.
- Simulated payment status is saved with the booking.

**Note:** This project uses local file storage, not MySQL or another database server. It is intended for learning and demonstration.

## 🧠 Object-Oriented Programming Concepts

- **Encapsulation:** Room and reservation information is managed through classes and methods.
- **Abstraction:** `HotelService` handles booking, availability, payment simulation, cancellation, and file storage.
- **Classes and Objects:** The project separates the interface, room information, reservations, and business logic into different classes.

## 🔮 Future Enhancements

- Integrate MySQL for structured database storage.
- Add user registration and login.
- Support multiple guests and room capacity.
- Add a real payment gateway in a properly secured implementation.
- Introduce booking search filters and downloadable booking receipts.
- Improve date selection with a calendar component.

## ⚠️ Disclaimer

This is an educational project. Payments are simulated, and local serialized-file storage is not intended for production use. The application does not connect to a real payment gateway.

---

## 👩‍💻 Author

**Sejal Rai**

B.Tech Computer Science Engineering

[GitHub Profile](https://github.com/rsejal477-apssr)

---

⭐ If you find this project useful, consider starring the repository!
