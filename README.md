# ATLAS — Automated Task & Logistics Assignment System

## 📌 Description
ATLAS is an Android app built to replace the fragmented group-messaging workflows (WhatsApp threads, verbal instructions, sticky notes) that event organizers and volunteers typically rely on. It centralizes task assignment and logistics coordination in one place, so organizers can assign work and volunteers can see exactly what's expected of them.

## 🛠️ Features
- **Centralized task assignment** — organizers create and assign tasks instead of coordinating over scattered chat threads.
- **Role-based flows** — organizers and volunteers see different views and actions appropriate to their role.
- **Event/task detail screens** — clear drill-down views for individual events and tasks.
- **Activity logs** — a visible history of actions taken, useful for accountability during live events.
- **Authentication** — role-aware login so the right person sees the right dashboard.
- **Fully offline** — all data (events, tasks, volunteers, activity logs) is persisted locally with Room; no backend or network dependency.

## 🧱 Tech Stack
Kotlin · Jetpack Compose (Material 3) · MVVM · Room

## ⚙️ Architecture
- **UI layer** — Jetpack Compose screens following Material 3 design guidelines.
- **MVVM** — ViewModels expose UI state to Compose screens and handle business logic, keeping screens declarative and testable.
- **Room** — local database for persisting tasks, events, volunteers, and activity logs on-device.

## 🚀 Usage
* 1. Clone the repo
git clone https://github.com/HSR-009/ATLAS.git
cd ATLAS

* 2. Open in Android Studio
    File > Open > select the ATLAS project folder

* 3. Build and run
    Select a device/emulator and hit Run in Android Studio

## 📸 Screenshots
(Yet to upload)

## 📽️ Demo Link
https://drive.google.com/file/d/1_BM7yYX8_ria0mpg7qiKp0-WzKc2W0Tb/view?usp=drive_link

## 🔒 Notes
This is a portfolio/learning project. It runs fully offline with local Room storage — there is currently no backend sync layer.
