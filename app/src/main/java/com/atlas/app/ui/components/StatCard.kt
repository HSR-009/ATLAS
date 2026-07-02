package com.atlas.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.atlas.app.ui.theme.CardShadow
import com.atlas.app.ui.theme.TextNavy

@Composable
fun StatCard(
    icon: String,
    label: String,
    value: String,
    bgColor: Color,
    borderColor: Color,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .shadow(3.dp, RoundedCornerShape(14.dp), spotColor = CardShadow)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(icon, fontSize = 20.sp)
        Spacer(Modifier.height(6.dp))
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextNavy.copy(alpha = 0.7f)
        )
    }
}
