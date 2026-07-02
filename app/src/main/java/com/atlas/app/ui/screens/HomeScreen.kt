package com.atlas.app.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.app.ui.components.*
import com.atlas.app.ui.theme.*
import com.atlas.app.viewmodel.AtlasViewModel
import java.util.Calendar

@Composable
fun HomeScreen(
    vm: AtlasViewModel,
    onEventClick: (String) -> Unit,
    onLogsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var showCreateEvent by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Active", "Upcoming", "Completed")
    val filteredEvents = vm.getFilteredEvents(selectedFilter)

    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        hour < 12 -> "Good morning ☀️"
        hour < 17 -> "Good afternoon 🌤"
        else      -> "Good evening 🌙"
    }

    Box(modifier = Modifier.fillMaxSize().background(PageBg)) {
        Column(Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                // ── Gradient header ───────────────────────────────
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(NavyBlue, MediumBlue),
                                    start = Offset(0f, 0f),
                                    end   = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                                )
                            )
                            .statusBarsPadding()
                            .padding(top = 8.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
                    ) {
                        Column {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(greeting, fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
                                    Text(
                                        vm.currentUser.name,
                                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF43A047))
                                        .clickable(onClick = onProfileClick),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        vm.currentUser.name.split(" ")
                                            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                                            .take(2).joinToString(""),
                                        fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White
                                    )
                                }
                            }
                            Spacer(Modifier.height(14.dp))
                            // Search bar
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🔍 ", fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                                BasicTextField(
                                    value = vm.searchQuery,
                                    onValueChange = { vm.searchQuery = it },
                                    singleLine = true,
                                    textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                                    decorationBox = { inner ->
                                        if (vm.searchQuery.isEmpty())
                                            Text("Search events…", fontSize = 14.sp,
                                                color = Color.White.copy(alpha = 0.5f))
                                        inner()
                                    }
                                )
                            }
                        }
                    }
                }

                // ── Stat cards ────────────────────────────────────
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard("📅", "Total Events", vm.getTotalEvents().toString(),
                            GreenStatBg, GreenStatBorder, GreenAccent, Modifier.weight(1f))
                        StatCard("⚡", "Active Now", vm.getActiveEvents().toString(),
                            BlueStatBg, BlueStatBorder, BlueAccent, Modifier.weight(1f))
                        StatCard("📋", "Open Tasks", vm.getOpenTasks().toString(),
                            OrangeStatBg, OrangeStatBorder, OrangeAccent, Modifier.weight(1f))
                    }
                }

                // ── Filter chips ─────────────────────────────────
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filters.forEach { filter ->
                            val isActive = filter == selectedFilter
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isActive) NavyBlue else PillChipBg)
                                    .clickable { selectedFilter = filter }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    filter,
                                    fontSize = 12.sp,
                                    fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isActive) Color.White else TextNavy
                                )
                            }
                        }
                    }
                }

                // ── Events section header ─────────────────────────
                item {
                    Text(
                        "${filteredEvents.size} Events",
                        fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextMuted,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                // ── Empty state or event cards ────────────────────
                if (filteredEvents.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🔍", fontSize = 48.sp)
                                Spacer(Modifier.height(12.dp))
                                Text("No events found", color = TextNavy, fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold)
                                Spacer(Modifier.height(4.dp))
                                Text("Try a different filter or create a new event",
                                    color = TextMuted, fontSize = 13.sp)
                            }
                        }
                    }
                } else {
                    items(filteredEvents) { event ->
                        EventCard(
                            event = event,
                            theme = vm.getCardTheme(event.themeIndex),
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .clickable { onEventClick(event.id) }
                        )
                    }
                }

                // Bottom spacer for FAB clearance
                item { Spacer(Modifier.height(80.dp)) }
            }

            // ── Bottom nav (always visible) ───────────────────
            AtlasBottomNav(
                selected = BottomNavItem.EVENTS,
                onSelect = { item ->
                    when (item) {
                        BottomNavItem.LOGS    -> onLogsClick()
                        BottomNavItem.PROFILE -> onProfileClick()
                        else -> {}
                    }
                }
            )
        }

        // ── Floating Action Button ────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 20.dp, bottom = 80.dp)
                .shadow(8.dp, CircleShape, spotColor = NavyBlue.copy(alpha = 0.4f))
                .size(56.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(NavyBlue, MediumBlue)))
                .clickable { showCreateEvent = true },
            contentAlignment = Alignment.Center
        ) {
            Text("＋", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Light)
        }

        // ── Modal overlay ─────────────────────────────────────
        CreateEventModal(
            visible   = showCreateEvent,
            onDismiss = { showCreateEvent = false },
            onSubmit  = { name, start, end, venue, vols ->
                vm.addEvent(name, start, end, venue, vols)
            }
        )
    }
}
