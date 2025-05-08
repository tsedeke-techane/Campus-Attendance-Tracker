package com.attendance.attendancetracker.presentation.pages

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.presentation.viewmodels.AuthViewModel
import com.attendance.attendancetracker.ui.theme.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignUpClick: () -> Unit = {},
    onLoginSuccess: (Boolean) -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") } // This is student/staff ID
    var password by remember { mutableStateOf("") }
    // var isTeacher by remember { mutableStateOf(false) } // Role will be determined from AuthResponse
    val context = LocalContext.current

    LaunchedEffect(viewModel.authState) {
        viewModel.authState?.let {
            if (it.isSuccess) {
                val authResponse = it.getOrNull()
                val userName = authResponse?.name ?: "User"
                val isTeacher = authResponse?.role == "teacher" // Or whatever string represents a teacher
                Toast.makeText(context, "Login successful for $userName", Toast.LENGTH_SHORT).show()
                onLoginSuccess(isTeacher)
                // viewModel.clearAuthState() // Removed: Clear state after handling
            } else {
                val errorMessage = it.exceptionOrNull()?.message ?: "Login failed"
                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                viewModel.clearAuthState() // Clear state after handling error
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF001E2F)), // Dark blue background
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Row for logo at the start (top-left)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 32.dp), // Padding from top and left
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.scanin_logo_removebg_preview__1__2_layerstyle__1_),
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp) // Adjust size as needed
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Welcome text and subtitle - centered
        Text(
            text = "Welcome Back",
            style = Typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Log in to track your attendance, manage records, and stay updated.",
            style = Typography.bodyMedium.copy(
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 24.dp)
        )



    Spacer(modifier = Modifier.height(32.dp))

        // Card with form fields
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)) // Light grey card background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    shape = RoundedCornerShape(50), // Fully rounded corners
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF001E2F), // Dark blue focus border
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        focusedContainerColor = Color.White, // White background when focused
                        unfocusedContainerColor = Color.White // White background when unfocused
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("ID") }, // Changed from "Email or ID" to just "ID" as per state variable
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

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { 
                        if (name.isNotBlank() && id.isNotBlank() && password.isNotBlank()) {
                            viewModel.login(name, id, password) // Assuming ViewModel's login takes name, id, password
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001E2F)), // Dark blue button
                    shape = RoundedCornerShape(50), // Fully rounded button
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Log In", color = Color.White)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("I don't have an account. ")
                    Text(
                        "Sign up",
                        color = Color(0xFF001E2F), // Dark blue, matching theme
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { 
                            viewModel.clearAuthState() // Clear auth state before navigating to sign up
                            onSignUpClick() 
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Pushes content to the top of the card
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}