package com.atlas.app.ui.components

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.app.data.*
import com.atlas.app.ui.theme.*
import java.util.Calendar


// ─────────────────────────────────────────────────────────
//  Shared bottom-sheet overlay wrapper
//  • imePadding() pushes the sheet up when the keyboard appears
//  • navigationBarsPadding() ensures it clears the system nav bar
// ─────────────────────────────────────────────────────────
@Composable
fun ModalOverlay(
    visible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(200)),
        exit  = fadeOut(animationSpec = tween(200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)
                ),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .shadow(8.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(Color.White)
                        .clickable(enabled = false, onClick = {})
                        // imePadding makes the sheet rise above the keyboard
                        .imePadding()
                        // navigationBarsPadding clears the system gesture/button bar
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                        .heightIn(max = 600.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp).height(4.dp)
                            .background(Color(0xFFD0D5DD), RoundedCornerShape(2.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(20.dp))
                    content()
                }
            }
        }
    }
}

@Composable
private fun ModalHeader(
    icon: String, title: String, subtitle: String,
    gradientColors: List<Color> = listOf(NavyBlue, MediumBlue)
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.linearGradient(gradientColors)),
            contentAlignment = Alignment.Center
        ) { Text(icon, fontSize = 18.sp) }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextNavy)
            Text(subtitle, fontSize = 12.sp, color = TextMuted)
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextNavy,
        modifier = Modifier.padding(bottom = 8.dp, top = 4.dp))
}

// ─────────────────────────────────────────────────────────
//  Date Picker Button — opens a native DatePickerDialog
// ─────────────────────────────────────────────────────────
@Composable
fun DatePickerButton(
    label: String,
    date: String,
    onDateSelected: (String) -> Unit,
    isError: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val months = listOf("Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec")

    val displayText = date.ifBlank { label }
    val isPlaceholder = date.isBlank()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(
                width = if (isError) 2.dp else 1.dp,
                color = if (isError) Color(0xFFE53935)
                        else if (!isPlaceholder) NavyBlue.copy(alpha = 0.4f)
                        else Color(0xFFBDBDBD),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        onDateSelected("${months[month]} $day, $year")
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = if (isError) Color(0xFFE53935) else TextMuted
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = if (isPlaceholder) "Tap to select" else date,
                    fontSize = 14.sp,
                    color = if (isPlaceholder) Color(0xFFBDBDBD)
                            else if (isError) Color(0xFFE53935)
                            else TextNavy,
                    fontWeight = if (!isPlaceholder) FontWeight.Medium else FontWeight.Normal
                )
            }
            Text("📅", fontSize = 18.sp)
        }
    }
}

// ─────────────────────────────────────────────────────────
//  Create Event Modal — with calendar date pickers + validation
// ─────────────────────────────────────────────────────────
@Composable
fun CreateEventModal(
    visible: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (name: String, startDate: String, endDate: String, venue: String, vols: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var venue by remember { mutableStateOf("") }
    var vols by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf("") }

    LaunchedEffect(visible) {
        if (visible) { name = ""; startDate = ""; endDate = ""; venue = ""; vols = ""; dateError = "" }
    }

    // Validate dates: parse "Mon DD, YYYY" and compare
    fun parseDateMillis(dateStr: String): Long? {
        return try {
            val months = mapOf("Jan" to 0, "Feb" to 1, "Mar" to 2, "Apr" to 3,
                "May" to 4, "Jun" to 5, "Jul" to 6, "Aug" to 7,
                "Sep" to 8, "Oct" to 9, "Nov" to 10, "Dec" to 11)
            val parts = dateStr.replace(",", "").split(" ")
            val month = months[parts[0]] ?: return null
            val day = parts[1].toInt()
            val year = parts[2].toInt()
            val cal = Calendar.getInstance()
            cal.set(year, month, day, 0, 0, 0)
            cal.timeInMillis
        } catch (e: Exception) { null }
    }

    ModalOverlay(visible = visible, onDismiss = onDismiss) {
        ModalHeader("🎉", "Create Event", "Set up a new event to manage")
        Spacer(Modifier.height(24.dp))

        SectionLabel("📝  Event Information")
        ModalTextField(value = name, onValueChange = { name = it }, label = "Event Name *")
        Spacer(Modifier.height(16.dp))

        SectionLabel("📅  Schedule & Location")

        // Start date picker
        DatePickerButton(
            label = "Start Date",
            date = startDate,
            onDateSelected = { picked ->
                startDate = picked
                // Validate: start must not be after end
                if (endDate.isNotBlank()) {
                    val s = parseDateMillis(picked)
                    val e = parseDateMillis(endDate)
                    dateError = if (s != null && e != null && s > e)
                        "Start date cannot be after end date" else ""
                }
            }
        )
        Spacer(Modifier.height(10.dp))

        // End date picker
        DatePickerButton(
            label = "End Date",
            date = endDate,
            isError = dateError.isNotBlank(),
            onDateSelected = { picked ->
                val s = parseDateMillis(startDate)
                val e = parseDateMillis(picked)
                if (s != null && e != null && e < s) {
                    // Block: don't set the invalid date, show error.
                    dateError = "End date cannot be before start date"
                    endDate = picked  // still store so user sees it
                } else {
                    endDate = picked
                    dateError = ""
                }
            }
        )

        if (dateError.isNotBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(dateError, fontSize = 11.sp, color = Color(0xFFE53935))
        }

        Spacer(Modifier.height(10.dp))
        ModalTextField(value = venue, onValueChange = { venue = it }, label = "📍 Venue")
        Spacer(Modifier.height(16.dp))

        SectionLabel("👥  Team Size")
        ModalTextField(value = vols, onValueChange = { vols = it }, label = "Expected Volunteers")
        Spacer(Modifier.height(24.dp))

        ModalSubmitButton(label = "🚀  Create Event", color = NavyBlue) {
            if (name.isNotBlank() && dateError.isBlank()) {
                onSubmit(name, startDate, endDate, venue, vols)
                onDismiss()
            }
        }
        Spacer(Modifier.height(8.dp))
        ModalCancelButton(onDismiss)
    }
}

// ─────────────────────────────────────────────────────────
//  Add Department Modal (emoji + color picker + preview)
// ─────────────────────────────────────────────────────────
@Composable
fun AddDepartmentModal(
    visible: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (name: String, emoji: String, accentColor: Color, bgColor: Color) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("📋") }
    var selectedColorIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(visible) {
        if (visible) { name = ""; selectedEmoji = "📋"; selectedColorIndex = 0 }
    }

    val preset = DeptPresetColors[selectedColorIndex]

    ModalOverlay(visible = visible, onDismiss = onDismiss) {
        ModalHeader("🏢", "Add Department", "Create a new team division",
            listOf(Color(0xFF1E88E5), Color(0xFF42A5F5)))
        Spacer(Modifier.height(24.dp))

        ModalTextField(value = name, onValueChange = { name = it }, label = "Department Name *")
        Spacer(Modifier.height(20.dp))

        // ── Emoji Picker ──
        SectionLabel("🎯  Choose Icon")
        DeptPresetEmojis.chunked(8).forEach { row ->
            Row(Modifier.fillMaxWidth().padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { emoji ->
                    val isSel = emoji == selectedEmoji
                    Box(
                        modifier = Modifier.size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSel) preset.bg else Color(0xFFF5F5F5))
                            .border(if (isSel) 2.dp else 0.dp,
                                if (isSel) preset.accent else Color.Transparent,
                                RoundedCornerShape(10.dp))
                            .clickable { selectedEmoji = emoji },
                        contentAlignment = Alignment.Center
                    ) { Text(emoji, fontSize = 17.sp) }
                }
            }
        }
        Spacer(Modifier.height(20.dp))

        // ── Color Picker ──
        SectionLabel("🎨  Choose Color")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            DeptPresetColors.forEachIndexed { i, p ->
                val isSel = i == selectedColorIndex
                Box(
                    modifier = Modifier.size(34.dp)
                        .clip(CircleShape)
                        .background(p.accent)
                        .then(if (isSel) Modifier.border(3.dp, Color.White, CircleShape)
                            .border(5.dp, p.accent.copy(alpha = 0.4f), CircleShape)
                        else Modifier)
                        .clickable { selectedColorIndex = i },
                    contentAlignment = Alignment.Center
                ) { if (isSel) Text("✓", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
        Spacer(Modifier.height(20.dp))

        // ── Live Preview ──
        SectionLabel("👁  Preview")
        Row(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFFAFBFF))
                .border(1.dp, preset.accent.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(12.dp)).background(preset.bg),
                contentAlignment = Alignment.Center
            ) { Text(selectedEmoji, fontSize = 22.sp) }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(name.ifBlank { "Department Name" },
                    fontWeight = FontWeight.Bold, fontSize = 15.sp,
                    color = if (name.isBlank()) TextMuted else TextNavy)
                Text("0 tasks · 0 volunteers", fontSize = 11.sp, color = TextMuted)
            }
        }
        Spacer(Modifier.height(24.dp))

        ModalSubmitButton("✨  Create Department", preset.accent) {
            if (name.isNotBlank()) { onSubmit(name, selectedEmoji, preset.accent, preset.bg); onDismiss() }
        }
        Spacer(Modifier.height(8.dp))
        ModalCancelButton(onDismiss)
    }
}

// ─────────────────────────────────────────────────────────
//  Add Volunteer Modal
//  Uses ExposedDropdownMenuBox so the dropdown reliably opens on phones
// ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVolunteerModal(
    visible: Boolean,
    onDismiss: () -> Unit,
    departments: List<Department>,
    onSubmit: (name: String, department: String, role: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedDept by remember { mutableStateOf<Department?>(null) }
    var selectedRole by remember { mutableStateOf("Member") }
    var deptExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) { name = ""; selectedDept = null; selectedRole = "Member"; deptExpanded = false }
    }

    val roles = listOf("Lead", "Coordinator", "Member")
    val roleColors = listOf(Color(0xFFE53935), Color(0xFFFB8C00), Color(0xFF43A047))

    ModalOverlay(visible = visible, onDismiss = onDismiss) {
        ModalHeader("🙋", "Add Volunteer", "Assign a new team member",
            listOf(Color(0xFF43A047), Color(0xFF66BB6A)))
        Spacer(Modifier.height(24.dp))

        ModalTextField(name, { name = it }, "Full Name *")
        Spacer(Modifier.height(12.dp))

        // Department dropdown — ExposedDropdownMenuBox reliably opens on phones
        SectionLabel("🏢  Department")
        ExposedDropdownMenuBox(
            expanded = deptExpanded,
            onExpandedChange = { deptExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedDept?.let { "${it.emoji} ${it.name}" } ?: "",
                onValueChange = {},
                label = { Text(if (departments.isEmpty()) "No departments yet" else "Select Department") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = deptExpanded) },
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = deptExpanded && departments.isNotEmpty(),
                onDismissRequest = { deptExpanded = false }
            ) {
                departments.forEach { dept ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    Modifier.size(28.dp).clip(RoundedCornerShape(7.dp))
                                        .background(dept.bgColor),
                                    contentAlignment = Alignment.Center
                                ) { Text(dept.emoji, fontSize = 13.sp) }
                                Spacer(Modifier.width(10.dp))
                                Text(dept.name, color = TextNavy)
                            }
                        },
                        onClick = { selectedDept = dept; deptExpanded = false }
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        // Role selector
        SectionLabel("🎖  Role")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            roles.forEachIndexed { i, role ->
                val isSel = role == selectedRole
                val c = roleColors[i]
                Box(
                    modifier = Modifier.weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSel) c.copy(alpha = 0.12f) else Color(0xFFF5F5F5))
                        .border(if (isSel) 1.5.dp else 0.dp,
                            if (isSel) c else Color.Transparent, RoundedCornerShape(10.dp))
                        .clickable { selectedRole = role }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(role, fontSize = 13.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSel) c else TextMuted)
                }
            }
        }
        Spacer(Modifier.height(24.dp))

        ModalSubmitButton("🙋  Add Volunteer", Color(0xFF43A047)) {
            val deptName = selectedDept?.name ?: ""
            if (name.isNotBlank() && deptName.isNotBlank()) {
                onSubmit(name, deptName, selectedRole)
                onDismiss()
            }
        }
        Spacer(Modifier.height(8.dp))
        ModalCancelButton(onDismiss)
    }
}

// ─────────────────────────────────────────────────────────
//  Add Task Modal
//  Uses ExposedDropdownMenuBox + calendar date picker
// ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskModal(
    visible: Boolean,
    onDismiss: () -> Unit,
    departments: List<Department>,
    volunteers: List<Volunteer>,
    onSubmit: (name: String, assignee: String, dueDate: String, priority: Priority, departmentId: String) -> Unit
) {
    var taskName by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var selectedDept by remember { mutableStateOf<Department?>(null) }
    var selectedVol by remember { mutableStateOf<Volunteer?>(null) }
    var deptExpanded by remember { mutableStateOf(false) }
    var volExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) {
            taskName = ""; dueDate = ""; selectedPriority = Priority.MEDIUM
            selectedDept = null; selectedVol = null
            deptExpanded = false; volExpanded = false
        }
    }

    // Filter volunteers by selected department
    val filteredVols = if (selectedDept != null)
        volunteers.filter { it.department.equals(selectedDept!!.name, ignoreCase = true) }
    else volunteers

    ModalOverlay(visible = visible, onDismiss = onDismiss) {
        ModalHeader("📋", "Add Task", "Create and assign a new task",
            listOf(Color(0xFFFB8C00), Color(0xFFFFB74D)))
        Spacer(Modifier.height(24.dp))

        ModalTextField(taskName, { taskName = it }, "Task Name *")
        Spacer(Modifier.height(12.dp))

        // Department dropdown
        SectionLabel("🏢  Department")
        ExposedDropdownMenuBox(
            expanded = deptExpanded,
            onExpandedChange = { deptExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedDept?.let { "${it.emoji} ${it.name}" } ?: "",
                onValueChange = {},
                label = { Text(if (departments.isEmpty()) "No departments yet" else "Select Department") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = deptExpanded) },
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = deptExpanded && departments.isNotEmpty(),
                onDismissRequest = { deptExpanded = false }
            ) {
                departments.forEach { dept ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    Modifier.size(28.dp).clip(RoundedCornerShape(7.dp))
                                        .background(dept.bgColor),
                                    contentAlignment = Alignment.Center
                                ) { Text(dept.emoji, fontSize = 13.sp) }
                                Spacer(Modifier.width(10.dp))
                                Text(dept.name, color = TextNavy)
                            }
                        },
                        onClick = {
                            selectedDept = dept
                            selectedVol = null  // reset volunteer when dept changes
                            deptExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        // Assignee dropdown
        SectionLabel("👤  Assign To")
        ExposedDropdownMenuBox(
            expanded = volExpanded,
            onExpandedChange = { volExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedVol?.name ?: "",
                onValueChange = {},
                label = { Text(if (filteredVols.isEmpty()) "No volunteers available" else "Select Volunteer") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = volExpanded) },
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = volExpanded && filteredVols.isNotEmpty(),
                onDismissRequest = { volExpanded = false }
            ) {
                filteredVols.forEach { vol ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    Modifier.size(28.dp).clip(CircleShape).background(vol.avatarColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(vol.initials, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(vol.name, color = TextNavy, fontSize = 14.sp)
                                    Text(vol.department, color = TextMuted, fontSize = 11.sp)
                                }
                            }
                        },
                        onClick = { selectedVol = vol; volExpanded = false }
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        // Due date picker
        SectionLabel("📅  Due Date")
        DatePickerButton(
            label = "Due Date",
            date = dueDate,
            onDateSelected = { dueDate = it }
        )
        Spacer(Modifier.height(16.dp))

        // Priority pills
        SectionLabel("⚡  Priority")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val priorities = listOf(
                Triple(Priority.HIGH, "🔴 High", Color(0xFFE53935)),
                Triple(Priority.MEDIUM, "🟡 Medium", Color(0xFFF9A825)),
                Triple(Priority.LOW, "🟢 Low", Color(0xFF43A047))
            )
            priorities.forEach { (p, label, c) ->
                val isSel = p == selectedPriority
                Box(
                    modifier = Modifier.weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSel) c.copy(alpha = 0.12f) else Color(0xFFF5F5F5))
                        .border(if (isSel) 1.5.dp else 0.dp,
                            if (isSel) c else Color.Transparent, RoundedCornerShape(10.dp))
                        .clickable { selectedPriority = p }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) { Text(label, fontSize = 12.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSel) c else TextMuted) }
            }
        }
        Spacer(Modifier.height(24.dp))

        ModalSubmitButton("📋  Add Task", Color(0xFFFB8C00)) {
            val vol = selectedVol
            val dept = selectedDept
            if (taskName.isNotBlank() && vol != null) {
                val parts = vol.name.split(" ")
                val assigneeName = if (parts.size >= 2) "${parts[0]} ${parts[1].first()}." else vol.name
                val deptId = dept?.id ?: ""
                onSubmit(taskName, assigneeName, dueDate, selectedPriority, deptId)
                onDismiss()
            }
        }
        Spacer(Modifier.height(8.dp))
        ModalCancelButton(onDismiss)
    }
}

// ─────────────────────────────────────────────────────────
//  Helpers
// ─────────────────────────────────────────────────────────
@Composable
fun ModalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.padding(vertical = 4.dp),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun ModalSubmitButton(label: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(50.dp)
            .shadow(4.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) { Text(label, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp) }
}

@Composable
fun ModalCancelButton(onDismiss: () -> Unit) {
    Button(
        onClick = onDismiss,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F4FF))
    ) { Text("Cancel", fontWeight = FontWeight.Medium, color = TextNavy) }
}
