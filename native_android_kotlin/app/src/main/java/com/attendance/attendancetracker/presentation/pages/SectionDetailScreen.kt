package com.attendance.attendancetracker.presentation.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun SectionDetailScreen(
    sectionName: String = "Section 1",
    onBackClick: () -> Unit = {},
    onAddNewStudentClick: () -> Unit = {},
    onGenerateQRClick: (String) -> Unit = {}
) {
    // Sample student data
    val students = listOf(
        Student("Unanias Tekeste", 85),
        Student("Eyuel Hadera", 91),
        Student("Saron Merone", 100),
        Student("Kidanian Semere", 92),
        Student("Eyob Seyum", 85)
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFF001E2F),
                    RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)
                )
                .padding(16.dp)
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp)
            )
        }
        
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Section title with back button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Back",
                        tint = Color(0xFF001E2F)
                    )
                }
                
                Text(
                    text = sectionName,
                    style = Typography.titleLarge.copy(
                        color = Color(0xFF001E2F),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            
            // Student list
            students.forEach { student ->
                StudentAttendanceItem(student = student)
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Add New Student button
            Button(
                onClick = onAddNewStudentClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001E2F)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Add New Student",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add New Student")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Generate QR Code button
            OutlinedButton(
                onClick = { onGenerateQRClick("Cyber Security") },
                border = BorderStroke(1.dp, Color(0xFF001E2F)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Generate QR Code",
                    tint = Color(0xFF001E2F),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Generate QR Code",
                    color = Color(0xFF001E2F)
                )
            }
        }
    }
}

data class Student(
    val name: String,
    val attendancePercentage: Int
)

@Composable
fun StudentAttendanceItem(
    student: Student
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = student.name,
                    style = Typography.bodyLarge.copy(
                        color = Color(0xFF001E2F),
                        fontWeight = FontWeight.Medium
                    )
                )
                
                Text(
                    text = "STUDENT ID",
                    style = Typography.bodySmall.copy(
                        color = Color(0xFF4A6572)
                    )
                )
            }
            
            Text(
                text = "${student.attendancePercentage}% Present",
                style = Typography.bodyMedium.copy(
                    color = Color(0xFF001E2F),
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionDetailScreenPreview() {
    SectionDetailScreen()
}
