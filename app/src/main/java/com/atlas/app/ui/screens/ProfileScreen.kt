package com.atlas.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.atlas.app.ui.components.StatCard
import com.atlas.app.ui.theme.*
import com.atlas.app.viewmodel.AtlasViewModel

@Composable
fun ProfileScreen(
    vm: AtlasViewModel,
    onEventsClick: () -> Unit,
    onLogsClick: () -> Unit,
    onLogOut: () -> Unit
) {
    val user = vm.currentUser

    Column(
        modifier = Modifier.fillMaxSize().background(PageBg)
    ) {
        // ── Blue header ───────────────────────────────────────
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
                .padding(top = 8.dp, start = 20.dp, end = 20.dp, bottom = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(72.dp).clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        user.name.split(" ").mapNotNull { it.firstOrNull()?.uppercaseChar() }
                            .take(2).joinToString(""),
                        fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(Modifier.height(10.dp))
                Text(user.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Organizer · ATLAS", fontSize = 12.sp, color = Color.White.copy(alpha = 0.75f))
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // ── Stat cards ────────────────────────────────────
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard("📅", "Events", user.eventsCount.toString(),
                    GreenStatBg, GreenStatBorder, GreenAccent, Modifier.weight(1f))
                StatCard("✅", "Tasks Done", user.tasksDone.toString(),
                    BlueStatBg, BlueStatBorder, BlueAccent, Modifier.weight(1f))
                StatCard("⭐", "Rate", user.rate,
                    OrangeStatBg, OrangeStatBorder, OrangeAccent, Modifier.weight(1f))
            }
            Spacer(Modifier.height(24.dp))

            // ── ACCOUNT section ───────────────────────────────
            SectionHeader("ACCOUNT")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(14.dp), spotColor = CardShadow)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
            ) {
                ProfileInfoRow("📧", "Email", user.email)
                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(horizontal = 16.dp))
                ProfileInfoRow("📱", "Phone", user.phone)
                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(horizontal = 16.dp))
                ProfileInfoRow("🏢", "Department", user.department)
            }

            Spacer(Modifier.height(20.dp))

            // ── QUICK ACCESS section ──────────────────────────
            SectionHeader("QUICK ACCESS")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(14.dp), spotColor = CardShadow)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
            ) {
                ProfileNavRow("▣  My Events", onClick = onEventsClick)
                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(horizontal = 16.dp))
                ProfileNavRow("📋  Activity Log", onClick = onLogsClick)
            }

            Spacer(Modifier.height(24.dp))

            // ── Log Out ───────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(14.dp), spotColor = Color(0x33E53935))
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(listOf(NavyBlue, MediumBlue))
                    )
                    .clickable(onClick = onLogOut)
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) { Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
            Spacer(Modifier.height(8.dp))
        }

        // ── Bottom nav ────────────────────────────────────────
        AtlasBottomNav(
            selected = BottomNavItem.PROFILE,
            onSelect = { item ->
                when (item) {
                    BottomNavItem.EVENTS -> onEventsClick()
                    BottomNavItem.LOGS   -> onLogsClick()
                    else -> {}
                }
            }
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
        color = TextMuted, letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
private fun ProfileInfoRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 16.sp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(label, fontSize = 11.sp, color = TextMuted)
            Text(value, fontSize = 14.sp, color = TextNavy, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ProfileNavRow(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp, color = TextNavy, fontWeight = FontWeight.Medium)
        Text("›", fontSize = 18.sp, color = TextMuted)
    }
}
