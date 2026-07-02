package com.atlas.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.app.data.*
import com.atlas.app.ui.components.*
import com.atlas.app.ui.theme.*
import com.atlas.app.viewmodel.AtlasViewModel

@Composable
fun EventDetailScreen(
    eventId: String,
    vm: AtlasViewModel,
    onBack: () -> Unit
) {
    val event = vm.getEventById(eventId) ?: return
    val theme = vm.getCardTheme(event.themeIndex)
    var selectedTab by remember { mutableIntStateOf(0) }
    var showDeptModal by remember { mutableStateOf(false) }
    var showVolModal by remember { mutableStateOf(false) }
    var showTaskModal by remember { mutableStateOf(false) }
    var showSpeedDial by remember { mutableStateOf(false) }
    var expandedDeptId by remember { mutableStateOf<String?>(null) }

    val headerGrad = Brush.linearGradient(
        listOf(NavyBlue, theme.accent),
        start = Offset(0f, 0f),
        end   = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
    val tabs = listOf("Departments", "Volunteers", "Tasks")
    val depts = vm.getDepartmentsForEvent(eventId)
    val vols  = vm.getVolunteersForEvent(eventId)
    val tasks = vm.getTasksForEvent(eventId)

    Box(modifier = Modifier.fillMaxSize().background(PageBg)) {
        Column(Modifier.fillMaxSize()) {

            // ── Header ────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerGrad)
                    .statusBarsPadding()
                    .padding(top = 8.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .clickable(onClick = onBack)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("← Back", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Medium)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(event.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(
                        "📅 ${event.dateRange}  📍 ${event.venue}",
                        fontSize = 12.sp, color = Color.White.copy(alpha = 0.75f)
                    )
                    Spacer(Modifier.height(16.dp))

                    // 4 stat boxes
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val progress = if (event.taskCount == 0) 0
                        else (event.tasksDone * 100 / event.taskCount)
                        listOf(
                            Triple("Tasks", event.taskCount.toString(), "📋"),
                            Triple("Done", event.tasksDone.toString(), "✅"),
                            Triple("Progress", "$progress%", "📊"),
                            Triple("Volunteers", event.volunteerCount.toString(), "👥")
                        ).forEach { (label, value, icon) ->
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(icon, fontSize = 14.sp)
                                Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(label, fontSize = 9.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }

            // ── Tabs ──────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp)
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                tabs.forEachIndexed { i, label ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = i; expandedDeptId = null }
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            label,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == i) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == i) theme.accent else TextMuted
                        )
                        Spacer(Modifier.height(4.dp))
                        if (selectedTab == i) {
                            Box(
                                Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(theme.accent)
                            )
                        }
                    }
                }
            }

            // ── Tab content ───────────────────────────────────
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (selectedTab) {
                    0 -> {
                        items(depts, key = { it.id }) { dept ->
                            val isExpanded = expandedDeptId == dept.id
                            Column {
                                DeptCard(dept = dept, isExpanded = isExpanded,
                                    onClick = { expandedDeptId = if (isExpanded) null else dept.id })

                                AnimatedVisibility(
                                    visible = isExpanded,
                                    enter = expandVertically(spring(stiffness = Spring.StiffnessMediumLow)) + fadeIn(),
                                    exit = shrinkVertically(spring(stiffness = Spring.StiffnessMediumLow)) + fadeOut()
                                ) {
                                    val deptTasks = vm.getTasksForDepartment(dept.id)
                                    Column(Modifier.padding(top = 8.dp)) {
                                        if (deptTasks.isEmpty()) {
                                            Box(
                                                modifier = Modifier.fillMaxWidth()
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(dept.bgColor.copy(alpha = 0.5f))
                                                    .padding(14.dp),
                                                contentAlignment = Alignment.Center
                                            ) { Text("No tasks assigned yet", fontSize = 12.sp, color = TextMuted) }
                                        } else {
                                            Text("${deptTasks.size} Tasks in ${dept.name}",
                                                fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                                color = dept.accentColor,
                                                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp))
                                            deptTasks.forEach { task ->
                                                TaskRow(task, dept.accentColor) { vm.toggleTask(task.id) }
                                                Spacer(Modifier.height(8.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        items(vols, key = { it.id }) { vol -> VolunteerRow(vol) }
                    }
                    2 -> {
                        val done  = tasks.count { it.isDone }
                        val total = tasks.size
                        item {
                            Row(Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("$total Tasks", fontWeight = FontWeight.SemiBold, color = TextNavy)
                                Text("$done done", fontSize = 12.sp, color = theme.accent)
                            }
                        }
                        items(tasks, key = { it.id }) { task ->
                            TaskRow(task, theme.accent) { vm.toggleTask(task.id) }
                        }
                    }
                }
                // Bottom spacer for FAB
                item { Spacer(Modifier.height(72.dp)) }
            }
        }

        // ── Speed Dial FAB Overlay ────────────────────────────
        if (showSpeedDial) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable { showSpeedDial = false }
            )
        }

        // ── Speed Dial Options ────────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 20.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnimatedVisibility(visible = showSpeedDial,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
            ) {
                Column(horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SpeedDialItem("🏢", "Department", Color(0xFF1E88E5)) {
                        showSpeedDial = false; showDeptModal = true }
                    SpeedDialItem("🙋", "Volunteer", Color(0xFF43A047)) {
                        showSpeedDial = false; showVolModal = true }
                    SpeedDialItem("📋", "Task", Color(0xFFFB8C00)) {
                        showSpeedDial = false; showTaskModal = true }
                }
            }

            // Main FAB
            Box(
                modifier = Modifier
                    .shadow(8.dp, CircleShape, spotColor = theme.accent.copy(alpha = 0.4f))
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(theme.accent, theme.gradEnd)))
                    .clickable { showSpeedDial = !showSpeedDial },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (showSpeedDial) "✕" else "＋",
                    color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Light
                )
            }
        }

        // ── Modals ────────────────────────────────────────────
        AddDepartmentModal(showDeptModal, { showDeptModal = false }) { name, emoji, accent, bg ->
            vm.addDepartment(eventId, name, emoji, accent, bg)
        }
        AddVolunteerModal(showVolModal, { showVolModal = false }, depts) { name, dept, role ->
            vm.addVolunteer(eventId, name, dept, role)
        }
        AddTaskModal(showTaskModal, { showTaskModal = false }, depts, vols) { name, assignee, due, priority, deptId ->
            vm.addTask(eventId, name, assignee, due, priority, deptId)
        }
    }
}

// ─────────────────────────────────────────────────────────
//  Speed Dial Item
// ─────────────────────────────────────────────────────────
@Composable
private fun SpeedDialItem(icon: String, label: String, color: Color, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .shadow(2.dp, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextNavy)
        }
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier.size(44.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) { Text(icon, fontSize = 18.sp) }
    }
}

// ─────────────────────────────────────────────────────────
//  Department card (expandable with animation)
// ─────────────────────────────────────────────────────────
@Composable
private fun DeptCard(dept: Department, isExpanded: Boolean, onClick: () -> Unit) {
    val progress = if (dept.taskCount == 0) 0f
    else dept.tasksDone.toFloat() / dept.taskCount

    // Outer Box owns shadow + clip. Inner Row owns IntrinsicSize.Min.
    // These cannot share the same modifier chain — shadow breaks intrinsic measurement.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isExpanded) 4.dp else 2.dp, RoundedCornerShape(14.dp), spotColor = CardShadow)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(
                if (isExpanded) 1.5.dp else 0.dp,
                if (isExpanded) dept.accentColor else Color.Transparent,
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(Modifier.width(5.dp).fillMaxHeight().background(dept.accentColor))
            Row(
                Modifier.fillMaxWidth().padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(50.dp).clip(RoundedCornerShape(12.dp)).background(dept.bgColor),
                    contentAlignment = Alignment.Center
                ) { Text(dept.emoji, fontSize = 22.sp) }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Row(Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(dept.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextNavy)
                        Text(if (isExpanded) "▲" else "▼", fontSize = 10.sp, color = dept.accentColor)
                    }
                    Text("${dept.taskCount} tasks · ${dept.volunteerCount} volunteers",
                        fontSize = 11.sp, color = TextMuted)
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)),
                        color = dept.accentColor, trackColor = dept.bgColor)
                    Spacer(Modifier.height(2.dp))
                    Text("${(progress * 100).toInt()}% complete", fontSize = 10.sp, color = dept.accentColor)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
//  Volunteer row
// ─────────────────────────────────────────────────────────
@Composable
private fun VolunteerRow(vol: Volunteer) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = CardShadow)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(vol.avatarColor),
            contentAlignment = Alignment.Center
        ) { Text(vol.initials, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White) }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(vol.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextNavy)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(vol.department, fontSize = 12.sp, color = TextMuted)
                if (vol.role != "Member") {
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                            .background(vol.avatarColor.copy(alpha = 0.08f))
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) { Text(vol.role, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = vol.avatarColor) }
                }
            }
        }
        Box(
            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                .background(vol.avatarColor.copy(alpha = 0.12f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) { Text("${vol.tasksDone}/${vol.tasksTotal}", fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold, color = vol.avatarColor) }
    }
}

// ─────────────────────────────────────────────────────────
//  Task row (toggleable)
// ─────────────────────────────────────────────────────────
@Composable
private fun TaskRow(task: Task, accentColor: Color, onToggle: () -> Unit) {
    val priorityLabel: String
    val priorityColor: Color
    when (task.priority) {
        Priority.HIGH   -> { priorityLabel = "High";  priorityColor = Color(0xFFE53935) }
        Priority.MEDIUM -> { priorityLabel = "Med";   priorityColor = Color(0xFFF9A825) }
        Priority.LOW    -> { priorityLabel = "Low";   priorityColor = Color(0xFF43A047) }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(12.dp), spotColor = CardShadow)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(22.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (task.isDone) Color(0xFF43A047) else Color.White)
                .border(2.dp, if (task.isDone) Color(0xFF43A047) else CardBorder, RoundedCornerShape(6.dp))
                .clickable(onClick = onToggle),
            contentAlignment = Alignment.Center
        ) {
            if (task.isDone) Text("✓", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(
                task.name, fontSize = 14.sp,
                color = if (task.isDone) TextMuted else TextNavy,
                fontWeight = if (task.isDone) FontWeight.Normal else FontWeight.Medium,
                style = if (task.isDone)
                    androidx.compose.ui.text.TextStyle(
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                else androidx.compose.ui.text.TextStyle()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("👤 ${task.assignee}", fontSize = 10.sp, color = TextMuted)
                Text("📅 ${task.dueDate}", fontSize = 10.sp, color = TextMuted)
            }
        }
        Box(
            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                .background(priorityColor.copy(alpha = 0.12f))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) { Text(priorityLabel, fontSize = 10.sp, color = priorityColor, fontWeight = FontWeight.SemiBold) }
    }
}
