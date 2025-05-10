package com.attendance.attendancetracker.presentation.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.ui.theme.Typography

@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC)) // light grey background
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF001E2F), RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
        ) {
            IconButton(
                onClick = { /* Back pressed */ },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_ios_back_svgrepo_com), // Replace with your back icon
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            TextButton(
                onClick = onSkipClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text("Skip", color = Color.White)
            }

            // Logo in the center
            Image(
                painter = painterResource(id = R.drawable.scanin_logo_removebg_preview__1__2_layerstyle__1_), // Replace with your logo resource
                contentDescription = "Logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "QR Your Way In.",
            style = Typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 32.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Effortlessly track your attendance and stay on top of your classes.",
            style = Typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 32.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onGetStartedClick,
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001E2F)),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(24.dp, bottom = 100.dp)
                .fillMaxWidth(0.6f)
                .height(50.dp)
        ) {
            Text("Get Started", style = Typography.labelLarge, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen()
}