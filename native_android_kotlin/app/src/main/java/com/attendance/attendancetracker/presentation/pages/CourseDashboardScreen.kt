package com.attendance.attendancetracker.presentation.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
//import androidx.compose.material.icons.filled.People
//import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.data.remote.dto.DailyAttendanceRecord
import com.attendance.attendancetracker.presentation.viewmodels.AttendanceViewModel
import com.attendance.attendancetracker.ui.theme.Typography
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDashboardScreen(
    courseName: String,
    teacherName: String = "Senayit Demisse",
    authToken: String,
    onBackClick: () -> Unit = {},
    onScanClick: () -> Unit = {}, // Added for QR scanning
    attendanceViewModel: AttendanceViewModel = hiltViewModel()
) {
    val studentAttendanceData by attendanceViewModel.studentAttendanceCalendarData.observeAsState()
    val isLoading by attendanceViewModel.isLoading.observeAsState(initial = false)
    val error by attendanceViewModel.error.observeAsState()

    LaunchedEffect(key1 = courseName, key2 = authToken) {
        if (courseName.isNotBlank() && authToken.isNotBlank()) {
            attendanceViewModel.loadStudentAttendanceCalendar(classId = courseName, token = authToken)
        }
    }

    Scaffold(
        topBar = {
            AppHeader(courseName, teacherName, onBackClick)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onScanClick,
                containerColor = Color(0xFF001E2F),
                contentColor = Color.White,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.qr),
                    contentDescription = "Scan QR Code",
                    tint = Color.White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Text("Error loading attendance: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
            } else if (studentAttendanceData != null) {
                val data = studentAttendanceData!!

                TrackingSummarySection(
                    presentCount = data.statistics.presentCount,
                    absentCount = data.statistics.absentCount,
                    totalClasses = data.statistics.totalClasses,
                    attendancePercentage = data.statistics.attendancePercentage
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    color = Color.LightGray,
                    thickness = 1.dp
                )

                Text("Attendance Calendar", style = Typography.titleMedium.copy(color = Color(0xFF001E2F), fontWeight = FontWeight.Bold), modifier = Modifier.padding(bottom = 8.dp))

                if (data.history.isEmpty()){
                    Text("No daily attendance records found.")
                } else {
                    // Get the year and month from the first record, or use current date if parsing fails
                    val calendar = Calendar.getInstance()
                    try {
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val firstRecordDate = dateFormat.parse(data.history.first().date)
                        if (firstRecordDate != null) {
                            calendar.time = firstRecordDate
                        }
                    } catch (e: Exception) {
                        // Fallback to current date
                    }
                    
                    StudentAttendanceCalendarView(
                        year = calendar.get(Calendar.YEAR),
                        month = calendar.get(Calendar.MONTH),
                        attendanceRecords = data.history
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Legend for attendance status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(16.dp).background(Color(0xFF001E2F)))
                    Text(" Present ", modifier = Modifier.padding(start=4.dp, end=8.dp), style = Typography.bodySmall)
                    Box(modifier = Modifier.size(16.dp).background(Color.Red))
                    Text(" Absent", modifier = Modifier.padding(start=4.dp), style = Typography.bodySmall)
                }

                // AttendanceReportSection - Teacher focused, commented out
            } else {
                Text("No attendance data loaded.", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
private fun AppHeader(courseNameForTitle: String, teacherName: String, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF001E2F))
            .padding(16.dp, 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() }
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = courseNameForTitle,
                    style = Typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                
                Text(
                    text = "Teacher: $teacherName",
                    style = Typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}

@Composable
private fun TrackingSummarySection(
    presentCount: Int,
    absentCount: Int,
    totalClasses: Int,
    attendancePercentage: Int
) {
    val absentPercentage = if (totalClasses > 0) ((absentCount.toFloat() / totalClasses.toFloat()) * 100).toInt() else 0

    Column(modifier = Modifier.padding(top = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Tracking Summary Icon",
                tint = Color(0xFF001E2F),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Tracking Summary",
                style = Typography.titleSmall.copy(
                    color = Color(0xFF001E2F),
                    fontWeight = FontWeight.Bold
                )
            )
        }

        AttendanceStatCard(
            iconRes = -1,
            iconVector = ImageVector.vectorResource(id = R.drawable.p),
            title = "Present",
            description = "$presentCount out of $totalClasses classes attended",
            percentage = attendancePercentage
        )

        Spacer(modifier = Modifier.height(12.dp))

        AttendanceStatCard(
            iconRes = -1,
            iconVector = ImageVector.vectorResource(id = R.drawable.group),
            title = "Absent",
            description = "$absentCount out of $totalClasses classes missed",
            percentage = absentPercentage
        )
    }
}

@Composable
fun AttendanceStatCard(
    iconRes: Int,
    iconVector: androidx.compose.ui.graphics.vector.ImageVector? = null,
    title: String,
    description: String,
    percentage: Int
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF001E2F)),
                contentAlignment = Alignment.Center
            ) {
                if (iconVector != null) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                } else if (iconRes != -1) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF001E2F))
                )
                Text(
                    text = description,
                    style = Typography.bodySmall.copy(color = Color(0xFF4A6572))
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "$percentage%",
                style = Typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF001E2F)
                )
            )
        }
    }
}

@Composable
private fun StudentAttendanceCalendarView(
    year: Int,
    month: Int,
    attendanceRecords: List<DailyAttendanceRecord>
) {
    // Create attendance map for quick lookup
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val attendanceMap = mutableMapOf<String, String>() // Map of "day" -> "status"
    
    attendanceRecords.forEach { record ->
        try {
            val date = dateFormat.parse(record.date)
            if (date != null) {
                val cal = Calendar.getInstance()
                cal.time = date
                // Store just the day (1-31) as the key
                val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH).toString()
                attendanceMap[dayOfMonth] = record.status
            }
        } catch (e: Exception) {
            // Skip invalid dates
        }
    }

    // Setup calendar to calculate days in month and first day of week
    val cal = Calendar.getInstance()
    cal.set(year, month, 1) // First day of the given month
    
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1 // 0 = Sunday, 6 = Saturday
    
    Column {
        // Days of the week header
        Row(modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            daysOfWeek.forEach { dayName ->
                Text(
                    text = dayName,
                    modifier = Modifier.weight(1f).padding(vertical = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    style = Typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            // Empty cells for days before the 1st
            items(firstDayOfWeek) {
                Box(modifier = Modifier.aspectRatio(1f).padding(2.dp))
            }

            // Days of the month
            items(daysInMonth) { index ->
                val dayNumber = index + 1
                val status = attendanceMap[dayNumber.toString()]

                val cellColor = when (status) {
                    "present" -> Color(0xFF001E2F) // Dark Blue
                    "absent" -> Color.Red
                    else -> Color.LightGray // Default for days with no record
                }
                
                val textColor = when (status) {
                    "present", "absent" -> Color.White
                    else -> Color.Black
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(3.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(cellColor)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayNumber.toString(),
                        color = textColor,
                        style = Typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CourseDashboardScreenPreview() {
    MaterialTheme {
        CourseDashboardScreen(
            courseName = "Cyber Security", 
            teacherName = "Senayit Demisse",
            authToken = "dummy_auth_token",
            onScanClick = {}
        )
    }
}