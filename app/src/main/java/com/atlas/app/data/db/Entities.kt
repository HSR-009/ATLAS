package com.atlas.app.data.db

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.atlas.app.data.*

// ─── Room Entity classes ─────────────────────────────────
// These mirror the domain models but use only Room-friendly types.
// Color → Int (ARGB), List<String> → comma-separated String, Enums → String name.

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val name: String,
    val status: String,
    val dateRange: String,
    val venue: String,
    val volunteerCount: Int,
    val taskCount: Int,
    val tasksDone: Int,
    val departmentCodes: String,
    val themeIndex: Int,
    val description: String
)

@Entity(tableName = "departments")
data class DepartmentEntity(
    @PrimaryKey val id: String,
    val eventId: String,
    val name: String,
    val emoji: String,
    val bgColor: Int,
    val accentColor: Int,
    val taskCount: Int,
    val volunteerCount: Int,
    val tasksDone: Int
)

@Entity(tableName = "volunteers")
data class VolunteerEntity(
    @PrimaryKey val id: String,
    val eventId: String,
    val initials: String,
    val name: String,
    val department: String,
    val avatarColor: Int,
    val tasksTotal: Int,
    val tasksDone: Int,
    val role: String
)

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val eventId: String,
    val name: String,
    val assignee: String,
    val dueDate: String,
    val priority: String,
    val isDone: Boolean,
    val departmentId: String
)

@Entity(tableName = "log_entries")
data class LogEntryEntity(
    @PrimaryKey val id: String,
    val time: String,
    val action: String,
    val actionBy: String,
    val dotColor: Int,
    val isYesterday: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val email: String,
    val phone: String,
    val department: String,
    val eventsCount: Int,
    val tasksDone: Int,
    val rate: String
)

// ─── Mapping: Domain → Entity ────────────────────────────

fun Event.toEntity() = EventEntity(
    id = id, name = name, status = status.name,
    dateRange = dateRange, venue = venue,
    volunteerCount = volunteerCount,
    taskCount = taskCount, tasksDone = tasksDone,
    departmentCodes = departmentCodes.joinToString(","),
    themeIndex = themeIndex, description = description
)

fun Department.toEntity() = DepartmentEntity(
    id = id, eventId = eventId, name = name, emoji = emoji,
    bgColor = bgColor.toArgb(), accentColor = accentColor.toArgb(),
    taskCount = taskCount, volunteerCount = volunteerCount, tasksDone = tasksDone
)

fun Volunteer.toEntity() = VolunteerEntity(
    id = id, eventId = eventId, initials = initials,
    name = name, department = department,
    avatarColor = avatarColor.toArgb(),
    tasksTotal = tasksTotal, tasksDone = tasksDone, role = role
)

fun Task.toEntity() = TaskEntity(
    id = id, eventId = eventId, name = name,
    assignee = assignee, dueDate = dueDate,
    priority = priority.name, isDone = isDone,
    departmentId = departmentId
)

fun LogEntry.toEntity(timestamp: Long = System.currentTimeMillis()) = LogEntryEntity(
    id = id, time = time, action = action,
    actionBy = actionBy, dotColor = dotColor.toArgb(),
    isYesterday = isYesterday, timestamp = timestamp
)

fun User.toEntity() = UserEntity(
    name = name, email = email, phone = phone,
    department = department, eventsCount = eventsCount,
    tasksDone = tasksDone, rate = rate
)

// ─── Mapping: Entity → Domain ────────────────────────────

fun EventEntity.toDomain() = Event(
    id = id, name = name,
    status = EventStatus.valueOf(status),
    dateRange = dateRange, venue = venue,
    volunteerCount = volunteerCount,
    taskCount = taskCount, tasksDone = tasksDone,
    departmentCodes = if (departmentCodes.isBlank()) emptyList()
                      else departmentCodes.split(","),
    themeIndex = themeIndex, description = description
)

fun DepartmentEntity.toDomain() = Department(
    id = id, eventId = eventId, name = name, emoji = emoji,
    bgColor = Color(bgColor), accentColor = Color(accentColor),
    taskCount = taskCount, volunteerCount = volunteerCount, tasksDone = tasksDone
)

fun VolunteerEntity.toDomain() = Volunteer(
    id = id, eventId = eventId, initials = initials,
    name = name, department = department,
    avatarColor = Color(avatarColor),
    tasksTotal = tasksTotal, tasksDone = tasksDone, role = role
)

fun TaskEntity.toDomain() = Task(
    id = id, eventId = eventId, name = name,
    assignee = assignee, dueDate = dueDate,
    priority = Priority.valueOf(priority),
    isDone = isDone, departmentId = departmentId
)

fun LogEntryEntity.toDomain() = LogEntry(
    id = id, time = time, action = action,
    actionBy = actionBy, dotColor = Color(dotColor),
    isYesterday = isYesterday
)

fun UserEntity.toDomain() = User(
    name = name, email = email, phone = phone,
    department = department, eventsCount = eventsCount,
    tasksDone = tasksDone, rate = rate
)
