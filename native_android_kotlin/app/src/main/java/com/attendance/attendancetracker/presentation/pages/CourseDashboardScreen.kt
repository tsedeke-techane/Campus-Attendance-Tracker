package com.attendance.attendancetracker.presentation.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.ui.theme.Typography

@Composable
fun CourseDashboardScreen(
    courseName: String = "Cyber Security",
    teacherName: String = "Senayit Demisse",
    presentPercentage: Int = 80,
    absentPercentage: Int = 20,
    onBackClick: () -> Unit = {},
    onScanClick: () -> Unit = {},
    onPresentClick: () -> Unit = {},
    onExcuseClick: () -> Unit = {},
    onAbsentClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {
        // Header with logo
        AppHeader()

        // Main content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Course title with back button
            CourseHeader(courseName, teacherName, onBackClick)

            // Tracking Summary section
            TrackingSummarySection(presentPercentage, absentPercentage)

            // Divider
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                color = Color.LightGray,
                thickness = 1.dp
            )

            // Attendance Report section (now more compact)
            AttendanceReportSection(onPresentClick, onExcuseClick, onAbsentClick)

            // Scan button
            Box(modifier = Modifier.fillMaxWidth()) {
                ScanButton(onScanClick)
            }
        }
    }
}

@Composable
private fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF001E2F))
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun CourseHeader(
    courseName: String,
    teacherName: String,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(24.dp)
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
                )
            )
        }

        Text(
            text = "Teacher: $teacherName",
            style = Typography.bodyMedium.copy(color = Color(0xFF4A6572)),
            modifier = Modifier.padding(start = 24.dp)
        )
    }
}

@Composable
private fun TrackingSummarySection(
    presentPercentage: Int,
    absentPercentage: Int
) {
    Column {
        // Section title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = "Calendar",
                tint = Color(0xFF001E2F),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Tracking Summary",
                style = Typography.titleMedium.copy(
                    color = Color(0xFF001E2F),
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // Present card
        AttendanceStatCard(
            iconRes = R.drawable.logo,
            title = "Present",
            description = "The number of days that the student was available",
            percentage = presentPercentage
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Absent card
        AttendanceStatCard(
            iconRes = R.drawable.logo,
            title = "Absent",
            description = "The number of days that the student was NOT available",
            percentage = absentPercentage
        )
    }
}

@Composable
private fun AttendanceStatCard(
    iconRes: Int,
    title: String,
    description: String,
    percentage: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF001E2F)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    style = Typography.titleMedium.copy(
                        color = Color(0xFF001E2F),
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = description,
                    style = Typography.bodySmall.copy(color = Color(0xFF4A6572))
                )
            }

            Text(
                text = "$percentage%",
                style = Typography.titleLarge.copy(
                    color = Color(0xFF001E2F),
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun AttendanceReportSection(
    onPresentClick: () -> Unit,
    onExcuseClick: () -> Unit,
    onAbsentClick: () -> Unit
) {
    Column {
        // Section title
        Text(
            text = "Attendance Report",
            style = Typography.titleMedium.copy(
                color = Color(0xFF001E2F),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Compact button row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CompactAttendanceButton(
                text = "Present",
                color = Color(0xFF001E2F),
                onClick = onPresentClick
            )
            CompactAttendanceButton(
                text = "Excuse",
                color = Color(0xFFFFB74D),
                onClick = onExcuseClick
            )
            CompactAttendanceButton(
                text = "Absent",
                color = Color(0xFFE53935),
                onClick = onAbsentClick
            )
        }

        // Calendar grid with smaller cells
        AttendanceCalendarGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        // Compact legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CompactLegendItem(color = Color(0xFF001E2F), text = "Present")
            CompactLegendItem(color = Color(0xFFFFB74D), text = "Excuse")
            CompactLegendItem(color = Color(0xFFE53935), text = "Absent")
        }
    }
}

@Composable
private fun CompactAttendanceButton(
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.height(28.dp)
    ) {
        Text(
            text = text,
            style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun AttendanceCalendarGrid(modifier: Modifier) {
    val attendanceData = listOf(
        listOf(1, 1, 1, 1, 1), // 1 = Present
        listOf(1, 1, 3, 1, 1), // 3 = Absent
        listOf(1, 1, 1, 3, 1),
        listOf(1, 1, 1, 1, 1),
        listOf(1, 0, 0, 0, 0)  // 0 = Empty
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        attendanceData.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                row.forEach { status ->
                    val color = when (status) {
                        1 -> Color(0xFF001E2F) // Present
                        2 -> Color(0xFFFFB74D) // Excuse
                        3 -> Color(0xFFE53935) // Absent
                        else -> Color.Transparent // Empty
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(color, RoundedCornerShape(2.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactLegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, RoundedCornerShape(2.dp))
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            style = Typography.labelSmall.copy(color = Color(0xFF4A6572))
        )
    }
}

@Composable
private fun ScanButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001E2F)),
        shape = RoundedCornerShape(50),
        modifier = Modifier
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CourseDashboardScreenPreview() {
    CourseDashboardScreen()
}