# ATLAS

> **ATLAS** is a modern Android application built to simplify event organization and volunteer management. It provides organizers with a centralized platform to create events, manage departments, assign tasks, monitor volunteer activity, and track progress through an intuitive interface.

---

## Features

* Event management dashboard
* Volunteer and organizer roles
* Department-wise event organization
* Task assignment and progress tracking
* Activity log for event updates
* Event details and status monitoring
* Modern Jetpack Compose UI
* Local data persistence using Room Database
* Navigation using Navigation Compose
* MVVM-inspired architecture with Repository pattern

---

## Tech Stack

| Category              | Technology                |
| --------------------- | ------------------------- |
| Language              | Kotlin                    |
| UI                    | Jetpack Compose           |
| Architecture          | MVVM + Repository Pattern |
| Database              | Room                      |
| Navigation            | Navigation Compose        |
| State Management      | ViewModel                 |
| Dependency Management | Gradle                    |

---

## Project Structure

```text
app
│
├── data
│   ├── AtlasRepository.kt
│   ├── Models.kt
│   ├── SampleData.kt
│   └── db
│       ├── AtlasDatabase.kt
│       ├── Dao.kt
│       └── Entities.kt
│
├── navigation
│   └── AppNavGraph.kt
│
├── ui
│   ├── components
│   ├── screens
│   └── theme
│
├── viewmodel
│   └── AtlasViewModel.kt
│
├── MainActivity.kt
└── AtlasApplication.kt
```

---

## Architecture

ATLAS follows a clean separation of responsibilities:

```text
UI (Jetpack Compose)
        │
        ▼
ViewModel
        │
        ▼
Repository
        │
        ▼
Room Database
```

* **UI** renders the application screens and collects user input.
* **ViewModel** manages UI state and business logic.
* **Repository** acts as the single source of truth for application data.
* **Room Database** stores application data locally.

---

## Current Modules

* Authentication
* Home Dashboard
* Event Details
* Activity Log
* Profile
* Role Selection
* Reusable UI Components
* Local Database Layer

---

## Getting Started

### Clone the repository

```bash
git clone https://github.com/HSR-009/ATLAS.git
```

### Open the project

1. Open Android Studio.
2. Select **Open**.
3. Choose the cloned project.
4. Allow Gradle Sync to complete.
5. Run the application on an emulator or Android device.

---

## Future Improvements

* Firebase Authentication
* Cloud synchronization
* Push notifications
* QR code check-in for volunteers
* Analytics dashboard
* Real-time event updates
* Offline-first synchronization
* Dark mode customization
* Admin dashboard enhancements

---

## Learning Objectives

This project was developed to strengthen practical Android development skills, including:

* Modern Android architecture
* Jetpack Compose UI development
* Room Database integration
* State management with ViewModel
* Navigation between Compose screens
* Building scalable and maintainable Android applications

---

## Author

**Harshraj Singh Rao**

GitHub: https://github.com/HSR-009

