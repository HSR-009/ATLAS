package com.atlas.app.data

import androidx.compose.ui.graphics.Color

object SampleData {

    val cardThemes = listOf(
        CardThemeColors(
            accent = Color(0xFF43A047), gradEnd = Color(0xFF81C784),
            chips = listOf(Color(0xFF43A047), Color(0xFF66BB6A), Color(0xFF2E7D32)),
            statBg = Color(0xFFE8F5E9), statBorder = Color(0xFFA5D6A7)
        ),
        CardThemeColors(
            accent = Color(0xFF1E88E5), gradEnd = Color(0xFF64B5F6),
            chips = listOf(Color(0xFF1E88E5), Color(0xFF42A5F5), Color(0xFF0D47A1)),
            statBg = Color(0xFFE3F2FD), statBorder = Color(0xFF90CAF9)
        ),
        CardThemeColors(
            accent = Color(0xFFFB8C00), gradEnd = Color(0xFFFFD54F),
            chips = listOf(Color(0xFFFB8C00), Color(0xFFFFA726), Color(0xFFE65100)),
            statBg = Color(0xFFFFF3E0), statBorder = Color(0xFFFFCC80)
        ),
        CardThemeColors(
            accent = Color(0xFF8E24AA), gradEnd = Color(0xFFCE93D8),
            chips = listOf(Color(0xFF8E24AA), Color(0xFFAB47BC), Color(0xFF6A1B9A)),
            statBg = Color(0xFFF3E5F5), statBorder = Color(0xFFCE93D8)
        )
    )

    val events = listOf(
        Event("e1", "College Fest 2026", EventStatus.ACTIVE,
            "Mar 20–22", "Main Auditorium", 24, 18, 11,
            listOf("LG", "MK", "FD", "ST", "PR"), 0,
            description = "Annual college cultural festival"),
        Event("e2", "Tech Workshop", EventStatus.ACTIVE,
            "Mar 28", "Lab Block", 10, 9, 6,
            listOf("TK", "PR"), 1,
            description = "Hands-on coding workshop"),
        Event("e3", "Blood Donation Camp", EventStatus.UPCOMING,
            "Apr 5", "Ground Floor", 15, 12, 0,
            listOf("MD", "LG", "PR"), 2,
            description = "Community health initiative"),
        Event("e4", "Alumni Meet 2026", EventStatus.COMPLETED,
            "Feb 14", "Conference Hall", 20, 22, 22,
            listOf("EV", "FD", "MK"), 3,
            description = "Annual alumni gathering")
    )

    val departments = listOf(
        Department("d1", "e1", "Logistics", "🚛",
            Color(0xFFE3F2FD), Color(0xFF1E88E5), 5, 3, 3),
        Department("d2", "e1", "Marketing", "📢",
            Color(0xFFE8F5E9), Color(0xFF43A047), 4, 4, 3),
        Department("d3", "e1", "Food & Catering", "🍽️",
            Color(0xFFFFF3E0), Color(0xFFFB8C00), 5, 8, 2),
        Department("d4", "e1", "Stage & Decor", "🎤",
            Color(0xFFF3E5F5), Color(0xFF8E24AA), 4, 6, 2)
    )

    val volunteers = listOf(
        Volunteer("v1", "e1", "RS", "Rahul Sharma",
            "Logistics", Color(0xFF1E88E5), 5, 3, role = "Lead"),
        Volunteer("v2", "e1", "PV", "Priya Verma",
            "Marketing", Color(0xFF43A047), 4, 4, role = "Lead"),
        Volunteer("v3", "e1", "AK", "Arjun Kumar",
            "Food & Catering", Color(0xFFFB8C00), 5, 2, role = "Member"),
        Volunteer("v4", "e1", "SM", "Sneha Mehta",
            "Stage & Decor", Color(0xFF8E24AA), 4, 2, role = "Coordinator")
    )

    val tasks = listOf(
        Task("t1", "e1", "Book transport vehicles",
            "Rahul S.", "Mar 19", Priority.HIGH, true, departmentId = "d1"),
        Task("t2", "e1", "Arrange stage equipment",
            "Arjun K.", "Mar 18", Priority.HIGH, true, departmentId = "d3"),
        Task("t3", "e1", "Confirm catering order",
            "Sneha M.", "Mar 20", Priority.MEDIUM, false, departmentId = "d4"),
        Task("t4", "e1", "Prepare welcome banners",
            "Priya V.", "Mar 19", Priority.LOW, false, departmentId = "d2"),
        Task("t5", "e1", "Set up registration desk",
            "Rahul S.", "Mar 20", Priority.HIGH, false, departmentId = "d1")
    )

    val logEntries = listOf(
        LogEntry("l1", "10:32 AM", "Task marked as complete",
            "Rahul Sharma", Color(0xFF43A047)),
        LogEntry("l2", "09:15 AM", "New volunteer added to Logistics",
            "Admin", Color(0xFF1E88E5)),
        LogEntry("l3", "08:50 AM", "Task assigned to Sneha Mehta",
            "Admin", Color(0xFFFB8C00)),
        LogEntry("l4", "Yesterday", "Department 'Stage & Decor' created",
            "Admin", Color(0xFF8E24AA), isYesterday = true),
        LogEntry("l5", "Yesterday", "Event 'College Fest 2026' created",
            "Admin", Color(0xFF1A237E), isYesterday = true)
    )

    val currentUser = User(
        name = "Admin User",
        email = "admin@atlas.app",
        phone = "+91 98765 43210",
        department = "Management",
        eventsCount = 4,
        tasksDone = 28,
        rate = "94%"
    )
}
