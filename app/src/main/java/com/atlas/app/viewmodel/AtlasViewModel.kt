package com.atlas.app.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.atlas.app.AtlasApplication
import com.atlas.app.data.*
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

class AtlasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as AtlasApplication).repository

    // ── Observable state (UI reads these directly – no change to screens) ──
    var events = mutableStateListOf<Event>()
        private set
    var departments = mutableStateListOf<Department>()
        private set
    var volunteers = mutableStateListOf<Volunteer>()
        private set
    var tasks = mutableStateListOf<Task>()
        private set
    var logEntries = mutableStateListOf<LogEntry>()
        private set

    var currentUser by mutableStateOf(
        User("", "", "", "", 0, 0, "0%")
    )
        private set

    var userRole by mutableStateOf(UserRole.ORGANIZER)
    var searchQuery by mutableStateOf("")

    var isLoading by mutableStateOf(true)
        private set

    // ── Init: seed demo data on first launch, then load ────
    init {
        viewModelScope.launch {
            if (repository.getEventCount() == 0) {
                seedDemoData()
            }
            refreshAll()
            isLoading = false
        }
    }

    private suspend fun seedDemoData() {
        // Seed demo user
        repository.saveUser(SampleData.currentUser)

        // Seed events
        SampleData.events.forEach { repository.insertEvent(it) }

        // Seed departments
        SampleData.departments.forEach { repository.insertDepartment(it) }

        // Seed volunteers
        SampleData.volunteers.forEach { repository.insertVolunteer(it) }

        // Seed tasks
        SampleData.tasks.forEach { repository.insertTask(it) }

        // Seed log entries
        SampleData.logEntries.forEach { repository.insertLogEntry(it) }
    }

    private suspend fun refreshAll() {
        events.clear()
        events.addAll(repository.getAllEvents())

        departments.clear()
        departments.addAll(repository.getAllDepartments())

        volunteers.clear()
        volunteers.addAll(repository.getAllVolunteers())

        tasks.clear()
        tasks.addAll(repository.getAllTasks())

        logEntries.clear()
        logEntries.addAll(repository.getAllLogEntries())

        repository.getUser()?.let { currentUser = it }
    }

    // ── Auth: signup → save user profile ──────────────────
    fun saveUserProfile(name: String, email: String, phone: String) {
        val user = User(
            name = name.trim(),
            email = email.trim(),
            phone = phone.trim(),
            department = "Management",
            eventsCount = 0,
            tasksDone = 0,
            rate = "0%"
        )
        currentUser = user
        viewModelScope.launch { repository.saveUser(user) }
    }

    // ── Auth: login → load user from DB by email ─────────
    fun loginUser(email: String) {
        viewModelScope.launch {
            val user = repository.getUser()
            if (user != null) {
                currentUser = user
            } else {
                // First time login with no existing account — create a minimal profile
                val newUser = User(
                    name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    email = email.trim(),
                    phone = "",
                    department = "Management",
                    eventsCount = 0,
                    tasksDone = 0,
                    rate = "0%"
                )
                currentUser = newUser
                repository.saveUser(newUser)
            }
            refreshAll()
        }
    }

    // ── Logout: clear all data ───────────────────────────
    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAll()
            events.clear()
            departments.clear()
            volunteers.clear()
            tasks.clear()
            logEntries.clear()
            currentUser = User("", "", "", "", 0, 0, "0%")
        }
    }

    // ── Computed ──────────────────────────────────────────
    val filteredEvents: List<Event>
        get() = if (searchQuery.isBlank()) events.toList()
        else events.filter { it.name.contains(searchQuery, ignoreCase = true) }

    fun getTotalEvents()  = events.size
    fun getActiveEvents() = events.count { it.status == EventStatus.ACTIVE }
    fun getOpenTasks()    = tasks.count { !it.isDone }

    fun getEventById(id: String)            = events.find { it.id == id }
    fun getDepartmentsForEvent(eId: String) = departments.filter { it.eventId == eId }
    fun getVolunteersForEvent(eId: String)  = volunteers.filter { it.eventId == eId }
    fun getTasksForEvent(eId: String)       = tasks.filter { it.eventId == eId }
    fun getCardTheme(idx: Int)              = SampleData.cardThemes[idx % 4]

    fun getTasksForDepartment(deptId: String): List<Task> =
        tasks.filter { it.departmentId == deptId }

    fun getFilteredEvents(filter: String): List<Event> {
        val base = filteredEvents
        return when (filter) {
            "Active"    -> base.filter { it.status == EventStatus.ACTIVE }
            "Upcoming"  -> base.filter { it.status == EventStatus.UPCOMING }
            "Completed" -> base.filter { it.status == EventStatus.COMPLETED }
            else        -> base
        }
    }

    // ── Mutations (update local state + persist to Room) ─

    fun toggleTask(taskId: String) {
        val i = tasks.indexOfFirst { it.id == taskId }
        if (i != -1) {
            val task = tasks[i]
            val nowDone = !task.isDone
            val updated = task.copy(isDone = nowDone)
            tasks[i] = updated

            // +1 when completing, -1 when un-completing
            val delta = if (nowDone) 1 else -1

            // ── Update department tasksDone ────────────────
            if (task.departmentId.isNotBlank()) {
                val dIdx = departments.indexOfFirst { it.id == task.departmentId }
                if (dIdx != -1) {
                    val updatedDept = departments[dIdx].copy(
                        tasksDone = (departments[dIdx].tasksDone + delta).coerceAtLeast(0)
                    )
                    departments[dIdx] = updatedDept
                    viewModelScope.launch { repository.updateDepartment(updatedDept) }
                }
            }

            // ── Update event tasksDone ─────────────────────
            val eIdx = events.indexOfFirst { it.id == task.eventId }
            if (eIdx != -1) {
                val updatedEvent = events[eIdx].copy(
                    tasksDone = (events[eIdx].tasksDone + delta).coerceAtLeast(0)
                )
                events[eIdx] = updatedEvent
                viewModelScope.launch { repository.updateEvent(updatedEvent) }
            }

            // ── Update user tasksDone ──────────────────────
            currentUser = currentUser.copy(
                tasksDone = (currentUser.tasksDone + delta).coerceAtLeast(0)
            )
            viewModelScope.launch { repository.saveUser(currentUser) }

            // ── Persist the task itself ────────────────────
            viewModelScope.launch { repository.updateTask(updated) }

            // ── Log ───────────────────────────────────────
            val action = if (nowDone) "Task '${task.name}' completed" else "Task '${task.name}' reopened"
            val color = if (nowDone) Color(0xFF43A047) else Color(0xFFF9A825)
            addLogEntry(action, color)
        }
    }

    fun addEvent(name: String, startDate: String, endDate: String, venue: String, expectedVols: String) {
        val themeIdx = events.size % 4
        val event = Event(
            id = "e_${UUID.randomUUID()}",
            name = name,
            status = EventStatus.UPCOMING,
            dateRange = if (endDate.isBlank()) startDate else "$startDate–$endDate",
            venue = venue,
            volunteerCount = expectedVols.toIntOrNull() ?: 0,
            taskCount = 0, tasksDone = 0,
            departmentCodes = emptyList(),
            themeIndex = themeIdx
        )
        events.add(event)
        // Update user event count
        currentUser = currentUser.copy(eventsCount = events.size)
        viewModelScope.launch {
            repository.insertEvent(event)
            repository.saveUser(currentUser)
        }
        addLogEntry("Event '$name' created", Color(0xFF1A237E))
    }

    fun addDepartment(
        eventId: String,
        name: String,
        emoji: String = "📋",
        accentColor: Color = Color(0xFF1A237E),
        bgColor: Color = Color(0xFFE8EAF6)
    ) {
        val dept = Department(
            id = "d_${UUID.randomUUID()}", eventId = eventId,
            name = name, emoji = emoji,
            bgColor = bgColor, accentColor = accentColor,
            taskCount = 0, volunteerCount = 0, tasksDone = 0
        )
        departments.add(dept)
        addLogEntry("Department '$name' created", accentColor)
        viewModelScope.launch { repository.insertDepartment(dept) }
    }

    fun addVolunteer(eventId: String, name: String, department: String, role: String = "Member") {
        val initials = name.trim().split(" ")
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }.take(2)
            .joinToString("")
        val dept = departments.find { it.name == department && it.eventId == eventId }
        val color = dept?.accentColor ?: Color(0xFF1A237E)
        val vol = Volunteer(
            id = "v_${UUID.randomUUID()}", eventId = eventId,
            initials = initials, name = name, department = department,
            avatarColor = color, tasksTotal = 0, tasksDone = 0,
            role = role
        )
        volunteers.add(vol)

        // Keep department volunteer count in sync
        if (dept != null) {
            val idx = departments.indexOf(dept)
            if (idx != -1) {
                val updated = dept.copy(volunteerCount = dept.volunteerCount + 1)
                departments[idx] = updated
                viewModelScope.launch { repository.updateDepartment(updated) }
            }
        }
        addLogEntry("Volunteer '$name' added to $department", color)
        viewModelScope.launch { repository.insertVolunteer(vol) }
    }

    fun addTask(eventId: String, name: String, assignee: String, dueDate: String, priority: Priority, departmentId: String = "") {
        val task = Task(
            id = "t_${UUID.randomUUID()}", eventId = eventId,
            name = name, assignee = assignee, dueDate = dueDate,
            priority = priority, isDone = false, departmentId = departmentId
        )
        tasks.add(task)

        // Keep department task count in sync
        if (departmentId.isNotBlank()) {
            val dept = departments.find { it.id == departmentId }
            if (dept != null) {
                val idx = departments.indexOf(dept)
                if (idx != -1) {
                    val updated = dept.copy(taskCount = dept.taskCount + 1)
                    departments[idx] = updated
                    viewModelScope.launch { repository.updateDepartment(updated) }
                }
            }
        }

        // Keep event task count in sync
        val eIdx = events.indexOfFirst { it.id == eventId }
        if (eIdx != -1) {
            val updatedEvent = events[eIdx].copy(taskCount = events[eIdx].taskCount + 1)
            events[eIdx] = updatedEvent
            viewModelScope.launch { repository.updateEvent(updatedEvent) }
        }

        val color = when (priority) {
            Priority.HIGH   -> Color(0xFFE53935)
            Priority.MEDIUM -> Color(0xFFF9A825)
            Priority.LOW    -> Color(0xFF43A047)
        }
        addLogEntry("Task '$name' assigned to $assignee", color)
        viewModelScope.launch { repository.insertTask(task) }
    }

    private fun addLogEntry(action: String, color: Color) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val amPm = if (hour < 12) "AM" else "PM"
        val displayHour = if (hour % 12 == 0) 12 else hour % 12
        val time = "$displayHour:${minute.toString().padStart(2, '0')} $amPm"
        val log = LogEntry(
            id = "l_${UUID.randomUUID()}",
            time = time,
            action = action,
            actionBy = currentUser.name.ifBlank { "User" },
            dotColor = color
        )
        logEntries.add(0, log)
        viewModelScope.launch { repository.insertLogEntry(log) }
    }
}
