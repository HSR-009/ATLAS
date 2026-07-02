package com.atlas.app.data.db

import androidx.room.*

// ─── Event DAO ───────────────────────────────────────────
@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    suspend fun getAll(): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getById(id: String): EventEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<EventEntity>)

    @Update
    suspend fun update(event: EventEntity)

    @Delete
    suspend fun delete(event: EventEntity)

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getCount(): Int

    @Query("DELETE FROM events")
    suspend fun deleteAll()
}

// ─── Department DAO ──────────────────────────────────────
@Dao
interface DepartmentDao {
    @Query("SELECT * FROM departments")
    suspend fun getAll(): List<DepartmentEntity>

    @Query("SELECT * FROM departments WHERE eventId = :eventId")
    suspend fun getByEventId(eventId: String): List<DepartmentEntity>

    @Query("SELECT * FROM departments WHERE id = :id")
    suspend fun getById(id: String): DepartmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dept: DepartmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(depts: List<DepartmentEntity>)

    @Update
    suspend fun update(dept: DepartmentEntity)

    @Query("SELECT COUNT(*) FROM departments")
    suspend fun getCount(): Int

    @Query("DELETE FROM departments")
    suspend fun deleteAll()
}

// ─── Volunteer DAO ───────────────────────────────────────
@Dao
interface VolunteerDao {
    @Query("SELECT * FROM volunteers")
    suspend fun getAll(): List<VolunteerEntity>

    @Query("SELECT * FROM volunteers WHERE eventId = :eventId")
    suspend fun getByEventId(eventId: String): List<VolunteerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vol: VolunteerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vols: List<VolunteerEntity>)

    @Update
    suspend fun update(vol: VolunteerEntity)

    @Query("SELECT COUNT(*) FROM volunteers")
    suspend fun getCount(): Int

    @Query("DELETE FROM volunteers")
    suspend fun deleteAll()
}

// ─── Task DAO ────────────────────────────────────────────
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    suspend fun getAll(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE eventId = :eventId")
    suspend fun getByEventId(eventId: String): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE departmentId = :deptId")
    suspend fun getByDepartmentId(deptId: String): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<TaskEntity>)

    @Update
    suspend fun update(task: TaskEntity)

    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getCount(): Int

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()
}

// ─── LogEntry DAO ────────────────────────────────────────
@Dao
interface LogEntryDao {
    @Query("SELECT * FROM log_entries ORDER BY timestamp DESC")
    suspend fun getAll(): List<LogEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: LogEntryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<LogEntryEntity>)

    @Query("SELECT COUNT(*) FROM log_entries")
    suspend fun getCount(): Int

    @Query("DELETE FROM log_entries")
    suspend fun deleteAll()
}

// ─── User DAO ────────────────────────────────────────────
@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = 1")
    suspend fun getUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}
