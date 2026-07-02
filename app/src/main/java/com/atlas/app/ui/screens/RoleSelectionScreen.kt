package com.atlas.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.app.data.UserRole
import com.atlas.app.ui.theme.*

@Composable
fun RoleSelectionScreen(
    mode: String,
    onBack: () -> Unit,
    onContinue: (UserRole) -> Unit
) {
    var selected by remember { mutableStateOf<UserRole?>(null) }
    val isLogin = mode == "login"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
            .padding(24.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        // Back pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(PillChipBg)
                .clickable(onClick = onBack)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text("← Back", fontSize = 13.sp, color = TextNavy, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = if (isLogin) "Welcome back!" else "Create Account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextNavy
        )
        Text(
            text = "Select your role to continue",
            fontSize = 13.sp,
            color = TextMuted
        )
        Spacer(Modifier.height(28.dp))

        // Organizer card
        RoleCard(
            icon = "📋",
            title = "Organizer",
            description = "Create events, manage team & assign tasks",
            bgColor = Color(0xFFE3F2FD),
            isSelected = selected == UserRole.ORGANIZER,
            onClick = { selected = UserRole.ORGANIZER }
        )
        Spacer(Modifier.height(16.dp))

        // Volunteer card
        RoleCard(
            icon = "🙋",
            title = "Volunteer",
            description = "Join events & complete your assigned tasks",
            bgColor = Color(0xFFE8F5E9),
            isSelected = selected == UserRole.VOLUNTEER,
            onClick = { selected = UserRole.VOLUNTEER }
        )

        Spacer(Modifier.weight(1f))

        val btnColor = when (selected) {
            UserRole.ORGANIZER -> NavyBlue
            UserRole.VOLUNTEER -> Color(0xFF43A047)
            null               -> DisabledBlue
        }
        Button(
            onClick = { selected?.let { onContinue(it) } },
            enabled = selected != null,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = btnColor,
                disabledContainerColor = DisabledBlue
            )
        ) {
            Text("Continue →", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun RoleCard(
    icon: String,
    title: String,
    description: String,
    bgColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0xFFE8EAF6) else bgColor)
            .border(
                width = if (isSelected) 2.dp else 0.5.dp,
                color = if (isSelected) NavyBlue else CardBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 22.sp)
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextNavy)
            Text(description, fontSize = 12.sp, color = TextMuted, lineHeight = 18.sp)
        }
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(if (isSelected) NavyBlue else Color.White)
                .border(2.dp, if (isSelected) NavyBlue else CardBorder,
                    androidx.compose.foundation.shape.CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) Text("✓", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
