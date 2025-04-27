package com.attendance.attendancetracker.presentation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.data.models.Student
import com.attendance.attendancetracker.ui.theme.Typography

@Composable
fun AttendanceSummaryScreen(
    sectionName: String = "Section 1",
    totalStudents: Int = 50,
    studentList: List<Student> = listOf(
        Student("Tsedeke Techane", "UGR/1234/15", 40),
        Student("Anat Esayas", "UGR/5002/15", 90),
        Student("Nuel Mezemir", "UGR/3456/15", 100),
        Student("Kalkidan Semere", "UGR/3894/15", 60),
        Student("Biruk Siyoum", "UGR/2468/15", 80)
    ),
    onAddStudent: (Student) -> Unit = {},
    onBackClick: () -> Unit = {},
    onAddNewStudentClick: () -> Unit = {}
) {
    var newName by remember { mutableStateOf("") }
    var newId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF001E2F))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SCANIN",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Section Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_ios_back_svgrepo_com),
                        contentDescription = "Back",
                        tint = Color(0xFF001E2F)
                    )
                }
                Text(
                    text = "$sectionName\nTotal: $totalStudents Students",
                    style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF001E2F)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Student List
            studentList.forEachIndexed { index, student ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("${index + 1}. ${student.name}", fontWeight = FontWeight.SemiBold)
                        Text(student.id, color = Color.Gray, fontSize = 12.sp)
                        Text("${student.attendancePercentage}% Present", color = Color(0xFF001E2F))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Input fields for new student
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "${studentList.size + 1}.", fontWeight = FontWeight.Bold)
                    BasicTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        textStyle = TextStyle(fontSize = 14.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        decorationBox = { innerTextField ->
                            if (newName.isEmpty()) Text("Name of the student", color = Color.Gray)
                            innerTextField()
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BasicTextField(
                        value = newId,
                        onValueChange = { newId = it },
                        textStyle = TextStyle(fontSize = 14.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        decorationBox = { innerTextField ->
                            if (newId.isEmpty()) Text("ID", color = Color.Gray)
                            innerTextField()
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (newName.isNotBlank() && newId.isNotBlank()) {
                                onAddStudent(Student(newName, newId, 0))
                                newName = ""
                                newId = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001E2F))
                    ) {
                        Text("+ Add")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAddNewStudentClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("+ Add New Student", color = Color(0xFF001E2F))
            }
        }
    }
}
