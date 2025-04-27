package com.attendance.attendancetracker.presentation.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.ui.theme.Typography

@Composable
fun StudentHomeScreen(
    studentName: String = "Anat",
    onCourseClick: (String) -> Unit = {},
    onScanClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Lighter background to match image
    ) {
        // Header with logo and menu button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF001E2F))
                .padding(16.dp)
        ) {
            // Logo
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "SCANIN Logo",
                    modifier = Modifier.size(32.dp)
                )
            }

            // Menu button
            IconButton(
                onClick = { /* Open menu */ },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Greeting
            Text(
                text = "Hi $studentName,",
                style = Typography.titleLarge.copy(
                    color = Color(0xFF4A6572),
                    fontWeight = FontWeight.Normal
                )
            )

            Text(
                text = "Ready To Attend Today?",
                style = Typography.titleLarge.copy(
                    color = Color(0xFF001E2F),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Course grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CourseCard(
                    title = "Cyber Security",
                    teacher = "Senayit Demisse",
                    modifier = Modifier.weight(1f),
                    onCardClick = { onCourseClick("Cyber Security") },
                    onScanClick = { onScanClick("Cyber Security") }
                )

                CourseCard(
                    title = "Operating System",
                    teacher = "Eshetu Demisse",
                    modifier = Modifier.weight(1f),
                    onCardClick = { onCourseClick("Operating System") },
                    onScanClick = { onScanClick("Operating System") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CourseCard(
                    title = "Mobile",
                    teacher = "Sara Mohammed",
                    modifier = Modifier.weight(1f),
                    onCardClick = { onCourseClick("Mobile") },
                    onScanClick = { onScanClick("Mobile") }
                )

                CourseCard(
                    title = "Artificial Intelligence",
                    teacher = "Manyazewal Eshetu",
                    modifier = Modifier.weight(1f),
                    onCardClick = { onCourseClick("Artificial Intelligence") },
                    onScanClick = { onScanClick("Artificial Intelligence") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CourseCard(
                    title = "Graphics",
                    teacher = "Abebe Tessema",
                    modifier = Modifier.weight(1f),
                    onCardClick = { onCourseClick("Graphics") },
                    onScanClick = { onScanClick("Graphics") }
                )

                CourseCard(
                    title = "Operating System",
                    teacher = "Teshome Chane",
                    modifier = Modifier.weight(1f),
                    onCardClick = { onCourseClick("Operating System 2") },
                    onScanClick = { onScanClick("Operating System 2") }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main scan button
            Button(
                onClick = { /* Global scan */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001E2F)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.End)
                    .width(120.dp)
                    .height(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Scan",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scan")
            }
        }
    }
}

@Composable
fun CourseCard(
    title: String,
    teacher: String,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    onScanClick: () -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF001E2F))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = Typography.titleMedium.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Teacher:",
                style = Typography.bodySmall.copy(color = Color.White.copy(alpha = 0.7f))
            )

            Text(
                text = teacher,
                style = Typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.9f)),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onCardClick() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Dashboard",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Dashboard", style = Typography.bodySmall)
                }

                OutlinedButton(
                    onClick = { onScanClick() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Scan",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Scan", style = Typography.bodySmall)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentHomeScreenPreview() {
    StudentHomeScreen()
}