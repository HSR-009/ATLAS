package com.atlas.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.app.ui.theme.*

// ─────────────────────────────────────────────────────────
//  Bottom Navigation Bar  (Events · Logs · Profile)
// ─────────────────────────────────────────────────────────
enum class BottomNavItem(val label: String, val icon: String) {
    EVENTS("Events", "▣"),
    LOGS("Logs", "📋"),
    PROFILE("Profile", "👤")
}

@Composable
fun AtlasBottomNav(
    selected: BottomNavItem,
    onSelect: (BottomNavItem) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp), spotColor = CardShadow)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Color.White)
            // navigationBarsPadding() ensures the nav bar clears the system
            // navigation bar (gesture bar / nav buttons) on phones.
            .navigationBarsPadding()
            .padding(vertical = 10.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem.entries.forEach { item ->
                val isSelected = item == selected
                val itemColor = when (item) {
                    BottomNavItem.EVENTS  -> NavyBlue
                    BottomNavItem.LOGS    -> Color(0xFF8E24AA)
                    BottomNavItem.PROFILE -> Color(0xFFE65100)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .then(
                            if (isSelected) Modifier.background(itemColor.copy(alpha = 0.08f))
                            else Modifier
                        )
                        .clickable { onSelect(item) }
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = item.icon,
                        fontSize = 20.sp,
                        color = if (isSelected) itemColor else TextGrey
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) itemColor else TextGrey
                    )
                    if (isSelected) {
                        Spacer(Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(itemColor)
                        )
                    }
                }
            }
        }
    }
}
