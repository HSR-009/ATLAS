package com.atlas.app.data

import com.atlas.app.data.db.*

/**
 * Repository that mediates between the ViewModel (domain models)
 * and the Room database (entity models).
 *
 * Every public function works with domain models.
 * Entity ↔ Domain conversion happens internally.
 */
class AtlasRepository(private val db: AtlasDatabase) {

    // ── Events ───────────────────────────────────────────
    suspend fun getAllEvents(): List<Event> =
        db.eventDao().getAll().map { it.toDomain() }

    suspend fun getEventById(id: String): Event? =
        db.eventDao().getById(id)?.toDomain()

    suspend fun insertEvent(event: Event) =
        db.eventDao().insert(event.toEntity())

    suspend fun updateEvent(event: Event) =
        db.eventDao().update(event.toEntity())

    suspend fun getEventCount(): Int =
        db.eventDao().getCount()

    // ── Departments ──────────────────────────────────────
    suspend fun getAllDepartments(): List<Department> =
        db.departmentDao().getAll().map { it.toDomain() }

    suspend fun getDepartmentsForEvent(eventId: String): List<Department> =
        db.departmentDao().getByEventId(eventId).map { it.toDomain() }

    suspend fun insertDepartment(dept: Department) =
        db.departmentDao().insert(dept.toEntity())

    suspend fun updateDepartment(dept: Department) =
        db.departmentDao().update(dept.toEntity())

    // ── Volunteers ───────────────────────────────────────
    suspend fun getAllVolunteers(): List<Volunteer> =
        db.volunteerDao().getAll().map { it.toDomain() }

    suspend fun getVolunteersForEvent(eventId: String): List<Volunteer> =
        db.volunteerDao().getByEventId(eventId).map { it.toDomain() }

    suspend fun insertVolunteer(vol: Volunteer) =
        db.volunteerDao().insert(vol.toEntity())

    suspend fun updateVolunteer(vol: Volunteer) =
        db.volunteerDao().update(vol.toEntity())

    // ── Tasks ────────────────────────────────────────────
    suspend fun getAllTasks(): List<Task> =
        db.taskDao().getAll().map { it.toDomain() }

    suspend fun getTasksForEvent(eventId: String): List<Task> =
        db.taskDao().getByEventId(eventId).map { it.toDomain() }

    suspend fun getTaskById(id: String): Task? =
        db.taskDao().getById(id)?.toDomain()

    suspend fun insertTask(task: Task) =
        db.taskDao().insert(task.toEntity())

    suspend fun updateTask(task: Task) =
        db.taskDao().update(task.toEntity())

    // ── Log Entries ──────────────────────────────────────
    suspend fun getAllLogEntries(): List<LogEntry> =
        db.logEntryDao().getAll().map { it.toDomain() }

    suspend fun insertLogEntry(log: LogEntry) =
        db.logEntryDao().insert(log.toEntity())

    // ── User Profile ─────────────────────────────────────
    suspend fun getUser(): User? =
        db.userDao().getUser()?.toDomain()

    suspend fun saveUser(user: User) =
        db.userDao().insertOrUpdate(user.toEntity())

    // ── Clear all data (logout) ────────────────────────────
    suspend fun clearAll() {
        db.eventDao().deleteAll()
        db.departmentDao().deleteAll()
        db.volunteerDao().deleteAll()
        db.taskDao().deleteAll()
        db.logEntryDao().deleteAll()
        db.userDao().deleteAll()
    }
}

