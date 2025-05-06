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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.ui.theme.Typography

@Composable
fun QRGeneratorScreen(
    courseName: String = "Cyber Security",
    teacherName: String = "Senayit Demisse",
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
    ) {
        // Top bar with logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF001E2F))
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(48.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title and Back Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_ios_back_svgrepo_com),
                        contentDescription = "Back",
                        tint = Color(0xFF001E2F)
                    )
                }

                Text(
                    text = courseName,
                    style = Typography.titleLarge.copy(
                        color = Color(0xFF001E2F),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Teacher name
            Text(
                text = "Teacher: $teacherName",
                style = Typography.bodyMedium.copy(color = Color(0xFF4A6572)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, bottom = 24.dp)
            )

            // QR Code Heading
            Text(
                text = "Scan the QR code for Attendance",
                style = Typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF001E2F)
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // QR Code Card
            Card(
                modifier = Modifier
                    .size(240.dp)
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with actual QR
                        contentDescription = "QR Code",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer instruction
            Text(
                text = "Simply scan the QR code when you enter class to mark your attendance automatically!",
                style = Typography.bodyMedium.copy(color = Color(0xFF4A6572)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QRGeneratorScreenPreview() {
    QRGeneratorScreen()
}
