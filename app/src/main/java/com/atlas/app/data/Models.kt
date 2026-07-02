package com.atlas.app.data

import androidx.compose.ui.graphics.Color

// ── Enums ─────────────────────────────────────────────────
enum class UserRole { ORGANIZER, VOLUNTEER }
enum class EventStatus { ACTIVE, UPCOMING, COMPLETED }
enum class Priority { HIGH, MEDIUM, LOW }

// ── Theme helper ──────────────────────────────────────────
data class CardThemeColors(
    val accent: Color,
    val gradEnd: Color,
    val chips: List<Color>,
    val statBg: Color,
    val statBorder: Color
)

// ── Domain models ─────────────────────────────────────────
data class Event(
    val id: String,
    val name: String,
    val status: EventStatus,
    val dateRange: String,
    val venue: String,
    val volunteerCount: Int,
    val taskCount: Int,
    val tasksDone: Int,
    val departmentCodes: List<String>,
    val themeIndex: Int,
    val description: String = ""
)

data class Department(
    val id: String,
    val eventId: String,
    val name: String,
    val emoji: String,
    val bgColor: Color,
    val accentColor: Color,
    val taskCount: Int,
    val volunteerCount: Int,
    val tasksDone: Int
)

data class Volunteer(
    val id: String,
    val eventId: String,
    val initials: String,
    val name: String,
    val department: String,
    val avatarColor: Color,
    val tasksTotal: Int,
    val tasksDone: Int,
    val role: String = "Member"
)

data class Task(
    val id: String,
    val eventId: String,
    val name: String,
    val assignee: String,
    val dueDate: String,
    val priority: Priority,
    val isDone: Boolean = false,
    val departmentId: String = ""
)

data class LogEntry(
    val id: String,
    val time: String,
    val action: String,
    val actionBy: String,
    val dotColor: Color,
    val isYesterday: Boolean = false
)

data class User(
    val name: String,
    val email: String,
    val phone: String,
    val department: String,
    val eventsCount: Int,
    val tasksDone: Int,
    val rate: String
)
