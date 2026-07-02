package com.atlas.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import com.atlas.app.ui.components.AtlasBottomNav
import com.atlas.app.ui.components.BottomNavItem
import com.atlas.app.ui.theme.*
import com.atlas.app.viewmodel.AtlasViewModel

private val DATE_CHIPS = listOf("Today", "Yesterday", "Mar 24", "Mar 23", "Mar 22")

@Composable
fun ActivityLogScreen(
    vm: AtlasViewModel,
    onEventsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var selectedDate by remember { mutableIntStateOf(0) }
    val logEntries = vm.logEntries

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
    ) {
        // ── Purple header ─────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(LogHeaderStart, LogHeaderEnd),
                        start = Offset(0f, 0f),
                        end   = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .statusBarsPadding()
                .padding(top = 8.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Column {
                Text("Activity Log", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Track what happened, and when", fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.75f))
            }
        }

        // ── Date chips ────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DATE_CHIPS.forEachIndexed { i, date ->
                val isActive = i == selectedDate
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isActive) LogHeaderEnd else PillChipBg)
                        .clickable { selectedDate = i }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(date, fontSize = 12.sp,
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isActive) Color.White else TextNavy)
                }
            }
        }

        // ── Timeline ──────────────────────────────────────────
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            val filtered = logEntries.filter { !it.isYesterday || selectedDate >= 1 }
            if (filtered.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📋", fontSize = 48.sp)
                            Spacer(Modifier.height(12.dp))
                            Text("No activity yet", color = TextNavy, fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(4.dp))
                            Text("Actions you perform will appear here",
                                color = TextMuted, fontSize = 13.sp)
                        }
                    }
                }
            } else {
                items(filtered) { entry ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier.width(72.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(entry.time, fontSize = 10.sp, color = TextMuted,
                                modifier = Modifier.padding(top = 4.dp))
                        }
                        Spacer(Modifier.width(8.dp))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier.size(10.dp)
                                    .clip(CircleShape).background(entry.dotColor)
                            )
                            Box(
                                modifier = Modifier.width(2.dp).height(56.dp)
                                    .background(CardBorder)
                            )
                        }
                        Spacer(Modifier.width(10.dp))

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = CardShadow)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .padding(12.dp)
                        ) {
                            Text(entry.action, fontSize = 13.sp, color = TextNavy,
                                fontWeight = FontWeight.Medium)
                            Text("by ${entry.actionBy}", fontSize = 11.sp, color = TextMuted)
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
        }

        // ── Bottom nav ────────────────────────────────────────
        AtlasBottomNav(
            selected = BottomNavItem.LOGS,
            onSelect = { item ->
                when (item) {
                    BottomNavItem.EVENTS  -> onEventsClick()
                    BottomNavItem.PROFILE -> onProfileClick()
                    else -> {}
                }
            }
        )
    }
}
