package com.atlas.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.app.ui.theme.*

@Composable
fun AuthScreen(
    role: String,
    mode: String,
    onBack: () -> Unit,
    onSignUp: (name: String, email: String, phone: String) -> Unit,
    onLogin: (email: String) -> Unit
) {
    var activeTab by remember { mutableStateOf(if (mode == "login") 0 else 1) }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Validation state
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmError by remember { mutableStateOf("") }

    val isLogin = activeTab == 0
    val isOrganizer = role == "organizer"
    val roleColor = if (isOrganizer) NavyBlue else Color(0xFF43A047)

    // Clear errors when switching tabs
    LaunchedEffect(activeTab) {
        nameError = ""; emailError = ""; passwordError = ""; confirmError = ""
    }

    fun validateAndSubmit() {
        // Reset errors
        nameError = ""; emailError = ""; passwordError = ""; confirmError = ""
        var isValid = true

        if (email.isBlank() || !email.contains("@")) {
            emailError = "Enter a valid email address"
            isValid = false
        }
        if (password.isBlank() || password.length < 4) {
            passwordError = "Password must be at least 4 characters"
            isValid = false
        }

        if (!isLogin) {
            if (fullName.isBlank()) {
                nameError = "Name is required"
                isValid = false
            }
            if (confirmPassword != password) {
                confirmError = "Passwords don't match"
                isValid = false
            }
        }

        if (isValid) {
            if (isLogin) {
                onLogin(email)
            } else {
                onSignUp(fullName, email, phone)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
            .verticalScroll(rememberScrollState())
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
        Spacer(Modifier.height(20.dp))

        // Title + role badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (isLogin) "Welcome back" else "Create account",
                fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextNavy
            )
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(roleColor.copy(alpha = 0.12f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    role.replaceFirstChar { it.uppercase() },
                    fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = roleColor
                )
            }
        }
        Spacer(Modifier.height(20.dp))

        // Tab switcher
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CardBorder)
                .padding(4.dp)
        ) {
            listOf("Log In", "Sign Up").forEachIndexed { i, label ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (activeTab == i) NavyBlue else Color.Transparent)
                        .clickable { activeTab = i }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (activeTab == i) Color.White else TextMuted
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))

        // Extra fields for signup
        if (!isLogin) {
            AuthField("Full Name", fullName, { fullName = it }, errorMsg = nameError)
            AuthField("Phone", phone, { phone = it })
        }
        AuthField("Email", email, { email = it }, errorMsg = emailError)
        AuthField("Password", password, { password = it }, isPassword = true, errorMsg = passwordError)
        if (!isLogin) {
            AuthField("Confirm Password", confirmPassword, { confirmPassword = it },
                isPassword = true, errorMsg = confirmError)
        }
        if (isLogin) {
            Text(
                "Forgot password?",
                fontSize = 12.sp, color = roleColor,
                modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
            )
        }
        Spacer(Modifier.height(20.dp))

        // Submit button
        Button(
            onClick = { validateAndSubmit() },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = roleColor)
        ) {
            Text(
                if (isLogin) "Log In" else "Create Account",
                fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White
            )
        }
        Spacer(Modifier.height(16.dp))

        // Divider
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(Modifier.weight(1f), color = CardBorder)
            Text("  or  ", fontSize = 12.sp, color = TextMuted)
            HorizontalDivider(Modifier.weight(1f), color = CardBorder)
        }
        Spacer(Modifier.height(16.dp))

        // Google button
        Button(
            onClick = {
                // For college project: treat Google sign-in same as login with a default account
                onLogin("user@atlas.app")
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(Color(0xFF1E88E5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("G", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(Modifier.width(10.dp))
                Text("Continue with Google", fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
        Spacer(Modifier.height(16.dp))

        // Switch link
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                if (isLogin) "Don't have an account? " else "Already have an account? ",
                fontSize = 13.sp, color = TextMuted
            )
            Text(
                if (isLogin) "Sign Up" else "Log In",
                fontSize = 13.sp, color = roleColor, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { activeTab = if (isLogin) 1 else 0 }
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun AuthField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    errorMsg: String = ""
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (isPassword) PasswordVisualTransformation()
            else VisualTransformation.None,
            isError = errorMsg.isNotBlank()
        )
        if (errorMsg.isNotBlank()) {
            Text(
                errorMsg,
                fontSize = 11.sp,
                color = Color(0xFFE53935),
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}
