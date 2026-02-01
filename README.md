# 🔵 Socialify - JavaFX Social Media Application

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-2B5797?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)

A fully functional, desktop-based **Social Media Platform** built with **Java** and **JavaFX**. This project demonstrates advanced Software Engineering concepts including **Database Connectivity (JDBC)**, **Session Management**, and **Modern UI Design** with custom CSS.

## 📸 GUI Screenshots

### 🔐 Authentication (Login & Sign Up)
<p align="center">
  <img src="screenshots/s1.png" width="100%" />
</p>

<p align="center">
  <img src="screenshots/s2.png" width="100%" />
</p>

### 📱 User Dashboard & Feed
<p align="center">
  <img src="screenshots/s3.png" width="100%" />
</p>

<p align="center">
  <img src="screenshots/s4.png" width="100%" />
</p>

---

## ✨ Key Features

### 🔐 Authentication & Security
* **Secure Login/Signup:** Robust authentication system verifying credentials against a MySQL database.
* **Session Handling:** Manages active user sessions to keep data persistent across different screens.

### 📱 Social Features
* **Post Creation:** Users can create, edit, and delete their own posts using the `EditPostDialog`.
* **Real-time Feed:** Fetches and displays the latest updates from the database instantly.
* **Interactive Dashboard:** A centralized hub for viewing user profiles and network activity.

### 🎨 Modern UI/UX
* **Custom Notifications:** Implements a `Toast` class for non-intrusive, Android-style popup messages (e.g., "Login Successful").
* **CSS Styling:** Custom `styles.css` overrides default JavaFX looks for a sleek, modern, web-like aesthetic.
* **Responsive Layouts:** Dynamic resizing and clean component organization.

### 🛠 Technical Highlights
* **JDBC Integration:** Direct SQL queries handle complex data relationships (Users, Posts, Likes).
* **Modular Navigation:** Seamless switching between Login, Signup, and Dashboard views.

---

## 🏗 Architecture & Design Pattern

This project follows a structured **Model-View-Controller (MVC)** inspired approach to ensure separation of concerns between the backend logic and the frontend interface.

### 1. Model (Data Layer)
* **`DatabaseConnection.java`**: The backbone of the app. Establishes the connection to MySQL and executes SQL queries.
* **Data Objects**: Handles raw data retrieval for user profiles and posts.

### 2. View (UI Layer)
* **`LoginScreen.java` / `SignUpScreen.java`**: Classes responsible for rendering the authentication forms.
* **`styles.css`**: Contains all visual styling rules (colors, fonts, button effects).
* **`Toast.java`**: A custom UI component created from scratch to show temporary feedback messages.

### 3. Controller (Logic Layer)
* **`HelloController.java`**: Manages the initial interaction points.
* **`Dashboard.java`**: Acts as the main controller for the user feed, handling interactions like refreshing data or opening dialogs.
* **`EditPostDialog.java`**: Controls the logic for modifying user content.

---

## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK)** 17 or higher.
* **MySQL Server** & **MySQL Workbench**.
* **IntelliJ IDEA** (Recommended).
* **MySQL Connector JAR** (Added to project libraries).

### Installation
1.  **Clone the repository**
    ```bash
    git clone https://github.com/Hassan-khan-5535/SocialMedia.git
    ```
2.  **Database Setup**
    * Open **MySQL Workbench**.
    * Run the provided SQL script (located in `sql/` folder) to initialize tables.
    * Update credentials in `DatabaseConnection.java`.
3.  **Open in IntelliJ IDEA**
    * File -> Open -> Select project folder.
    * Add MySQL Connector JAR to **Project Structure -> Libraries**.
4.  **Run the Project**
    * Navigate to `src/main/java/com/example/socialmedia/Launcher.java`.
    * Right-click -> **Run 'Launcher.main()'**.

---

## 📂 Project Structure

```text
src
└── main
    ├── java
    │   └── com.example.socialmedia
    │       ├── Dashboard.java           # Main Feed Controller
    │       ├── DatabaseConnection.java  # JDBC Connection Logic
    │       ├── EditPostDialog.java      # Dialog for editing posts
    │       ├── HelloApplication.java    # Main JavaFX Entry
    │       ├── HelloController.java     # Base Controller
    │       ├── Launcher.java            # App Bootstrap (Jar Fix)
    │       ├── LoginScreen.java         # Login View Logic
    │       ├── SignUpScreen.java        # Signup View Logic
    │       └── Toast.java               # Custom Notification
    └── resources
        ├── com.example.socialmedia      # FXML Files
        ├── images                       # Assets (Icons/Logos)
        └── styles.css                   # Global Stylesheet
```
---
University: COMSATS University Islamabad, Abbottabad Campus

This project was developed for academic purposes to demonstrate proficiency in Java, My Sql Workbech, JDBC, and GUI development.