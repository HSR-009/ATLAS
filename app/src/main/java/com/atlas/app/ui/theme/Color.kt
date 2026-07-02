package com.atlas.app.ui.theme

import androidx.compose.ui.graphics.Color

// Primary / Header gradient
val NavyBlue    = Color(0xFF1A237E)
val MediumBlue  = Color(0xFF1565C0)
val DarkGreen   = Color(0xFF2E7D32)

// Page & card
val PageBg      = Color(0xFFF0F4FF)
val CardBg      = Color(0xFFFFFFFF)
val CardBorder  = Color(0xFFE8EAF6)
val PillChipBg  = Color(0xFFE8EAF6)

// Text
val TextNavy    = Color(0xFF1A237E)
val TextMuted   = Color(0xFF607D8B)
val TextGrey    = Color(0xFF9E9E9E)
val DisabledBlue = Color(0xFF9FA8DA)

// ── Card Theme 0 : Green ──────────────────────────────────
val GreenAccent     = Color(0xFF43A047)
val GreenGradEnd    = Color(0xFF81C784)
val GreenStatBg     = Color(0xFFE8F5E9)
val GreenStatBorder = Color(0xFFA5D6A7)

// ── Card Theme 1 : Blue ───────────────────────────────────
val BlueAccent      = Color(0xFF1E88E5)
val BlueGradEnd     = Color(0xFF64B5F6)
val BlueStatBg      = Color(0xFFE3F2FD)
val BlueStatBorder  = Color(0xFF90CAF9)

// ── Card Theme 2 : Orange ─────────────────────────────────
val OrangeAccent     = Color(0xFFFB8C00)
val OrangeGradEnd    = Color(0xFFFFD54F)
val OrangeStatBg     = Color(0xFFFFF3E0)
val OrangeStatBorder = Color(0xFFFFCC80)

// ── Card Theme 3 : Purple ─────────────────────────────────
val PurpleAccent     = Color(0xFF8E24AA)
val PurpleGradEnd    = Color(0xFFCE93D8)
val PurpleStatBg     = Color(0xFFF3E5F5)
val PurpleStatBorder = Color(0xFFCE93D8)

// ── Special screen headers ────────────────────────────────
val LogHeaderStart     = Color(0xFF6A1B9A)
val LogHeaderEnd       = Color(0xFF8E24AA)
val ProfileHeaderStart = Color(0xFFE65100)
val ProfileHeaderEnd   = Color(0xFFFB8C00)

// Status chips
val StatusGreen  = Color(0xFF43A047)
val StatusYellow = Color(0xFFF9A825)
val StatusBlue   = Color(0xFF1E88E5)

// Priority
val PriorityHigh   = Color(0xFFE53935)
val PriorityMedium = Color(0xFFF9A825)
val PriorityLow    = Color(0xFF43A047)

// ── Department preset colors for picker ───────────────────
data class DeptColorPreset(val accent: Color, val bg: Color, val name: String)

val DeptPresetColors = listOf(
    DeptColorPreset(Color(0xFF1E88E5), Color(0xFFE3F2FD), "Blue"),
    DeptColorPreset(Color(0xFF43A047), Color(0xFFE8F5E9), "Green"),
    DeptColorPreset(Color(0xFFFB8C00), Color(0xFFFFF3E0), "Orange"),
    DeptColorPreset(Color(0xFF8E24AA), Color(0xFFF3E5F5), "Purple"),
    DeptColorPreset(Color(0xFFE53935), Color(0xFFFFEBEE), "Red"),
    DeptColorPreset(Color(0xFF00897B), Color(0xFFE0F2F1), "Teal"),
    DeptColorPreset(Color(0xFFD81B60), Color(0xFFFCE4EC), "Pink"),
    DeptColorPreset(Color(0xFF3949AB), Color(0xFFE8EAF6), "Indigo"),
)

// ── Department preset emojis ──────────────────────────────
val DeptPresetEmojis = listOf(
    "🚛", "📢", "🍽️", "🎤", "💻", "🎨",
    "🛡️", "💰", "📸", "🎪", "🎵", "📋",
    "🏆", "🎓", "⚡", "🔧"
)

// ── Surface elevation ─────────────────────────────────────
val SurfaceElevated = Color(0xFFFAFBFF)
val CardShadow      = Color(0x1A1A237E)
