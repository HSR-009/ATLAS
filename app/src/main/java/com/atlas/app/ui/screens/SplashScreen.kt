package com.atlas.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val bg = Brush.linearGradient(
        colors = listOf(Color(0xFF1A237E), Color(0xFF1565C0), Color(0xFF2E7D32)),
        start = Offset(0f, 0f),
        end   = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Box(
        modifier = Modifier.fillMaxSize().background(bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Spacer(Modifier.weight(1f))

            // ── Logo ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF43A047), Color(0xFF1565C0))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "A",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            Spacer(Modifier.height(24.dp))

            // ── App name ──────────────────────────────────────
            Text(
                text = "A T L A S",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 6.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "AUTOMATED TASK & LOGISTICS ASSIGNMENT SYSTEM",
                fontSize = 9.sp,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))

            // ── Tagline ───────────────────────────────────────
            Text(
                text = "Organize events. Coordinate volunteers.\nTrack every task — all in one place.",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(Modifier.weight(1f))

            // ── Buttons ───────────────────────────────────────
            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor   = Color(0xFF1A237E)
                )
            ) {
                Text("Log In", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = onCreateAccountClick,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp, Color.White.copy(alpha = 0.6f)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White,
                    containerColor = Color.White.copy(alpha = 0.1f)
                )
            ) {
                Text("Create Account", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = "ATLAS v1.0 · Powered by Firebase",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.45f)
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}
