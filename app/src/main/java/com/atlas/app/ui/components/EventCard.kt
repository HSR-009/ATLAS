package com.atlas.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
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
import com.atlas.app.data.CardThemeColors
import com.atlas.app.data.Event
import com.atlas.app.data.EventStatus
import com.atlas.app.ui.theme.*

@Composable
fun EventCard(
    event: Event,
    theme: CardThemeColors,
    modifier: Modifier = Modifier
) {
    val progress = if (event.taskCount == 0) 0f
    else event.tasksDone.toFloat() / event.taskCount

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = CardShadow)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        Row(
            Modifier.fillMaxWidth().height(IntrinsicSize.Min)
        ) {
            // Left accent bar — fixed with IntrinsicSize
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(theme.accent)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Title row
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextNavy,
                        modifier = Modifier.weight(1f)
                    )
                    StatusChip(event.status)
                }
                Spacer(Modifier.height(8.dp))
                // Meta row
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetaText("📅 ${event.dateRange}")
                    MetaText("📍 ${event.venue}")
                    MetaText("👥 ${event.volunteerCount}")
                }
                Spacer(Modifier.height(10.dp))
                // Progress
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Task Progress", fontSize = 11.sp, color = TextMuted)
                    Text(
                        "${(progress * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = theme.accent
                    )
                }
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = theme.accent,
                    trackColor = theme.statBg
                )
                Spacer(Modifier.height(10.dp))
                // Dept chips row
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DeptChips(codes = event.departmentCodes, colors = theme.chips)
                    Text(
                        "Tap to open ›",
                        fontSize = 10.sp,
                        color = theme.accent,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusChip(status: EventStatus) {
    val (label, bg, textColor) = when (status) {
        EventStatus.ACTIVE    -> Triple("Active",    Color(0xFFE8F5E9), Color(0xFF43A047))
        EventStatus.UPCOMING  -> Triple("Upcoming",  Color(0xFFFFF8E1), Color(0xFFF9A825))
        EventStatus.COMPLETED -> Triple("Done",      Color(0xFFE3F2FD), Color(0xFF1E88E5))
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = textColor)
    }
}

@Composable
private fun MetaText(text: String) {
    Text(text, fontSize = 11.sp, color = TextMuted)
}

@Composable
private fun DeptChips(codes: List<String>, colors: List<Color>) {
    val shown = codes.take(3)
    val extra = codes.size - shown.size
    Row {
        shown.forEachIndexed { i, code ->
            val color = colors.getOrElse(i % colors.size) { colors.first() }
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = (-6 * i).dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    code.take(2),
                    fontSize = 7.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        if (extra > 0) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = (-6 * shown.size).dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8EAF6)),
                contentAlignment = Alignment.Center
            ) {
                Text("+$extra", fontSize = 7.sp, color = TextNavy, fontWeight = FontWeight.Bold)
            }
        }
    }
}
