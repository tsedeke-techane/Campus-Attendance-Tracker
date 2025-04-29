package com.attendance.attendancetracker.presentation.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.ui.theme.Typography

@Composable
fun SignUpScreen(
    onLoginClick: () -> Unit = {},
    onSignUpSuccess: (Boolean) -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf("Student") }
    var name by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF001E2F))
    ) {
        // Logo and header section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 24.dp)
            )

            // Sign Up text
            Text(
                text = "Sign Up to Get Started",
                style = Typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Subtitle
            Text(
                text = "Sign up to track your attendance and manage your records",
                style = Typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center
            )
        }

        // Card with form fields
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Role selection buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Student button
                    Button(
                        onClick = { selectedRole = "Student" },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedRole == "Student") Color(0xFF001E2F) else Color.White
                        ),
                        border = if (selectedRole != "Student") BorderStroke(1.dp, Color(0xFF001E2F)) else null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_ios_back_svgrepo_com),
                            contentDescription = null,
                            tint = if (selectedRole == "Student") Color.White else Color(0xFF001E2F),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Student",
                            color = if (selectedRole == "Student") Color.White else Color(0xFF001E2F)
                        )
                    }

                    // Teacher button
                    Button(
                        onClick = { selectedRole = "Teacher" },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedRole == "Teacher") Color(0xFF001E2F) else Color.White
                        ),
                        border = if (selectedRole != "Teacher") BorderStroke(1.dp, Color(0xFF001E2F)) else null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            tint = if (selectedRole == "Teacher") Color.White else Color(0xFF001E2F),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Teacher",
                            color = if (selectedRole == "Teacher") Color.White else Color(0xFF001E2F)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Name field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF001E2F),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                // ID field
                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("ID") },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF001E2F),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF001E2F),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    shape = RoundedCornerShape(50),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF001E2F),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                // Login link
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("I already have an account? ")
                    Text(
                        "Log In",
                        color = Color(0xFF001E2F),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onLoginClick() }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Sign Up button
                Button(
                    onClick = {
                        val isTeacher = selectedRole == "Teacher"
                        onSignUpSuccess(isTeacher)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001E2F)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Sign Up", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen()
}
