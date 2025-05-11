package com.attendance.attendancetracker.presentation.pages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.presentation.viewmodels.DashboardViewModel
import com.attendance.attendancetracker.ui.theme.Typography

@Composable
fun StudentHomeScreen(
    studentName: String = "",
    authToken: String,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    onCourseClick: (classId: String, displayCourseName: String, teacherName: String) -> Unit = { _, _, _ -> },
    onScanClick: (String) -> Unit = {}
) {
    LaunchedEffect(key1 = authToken) {
        if (authToken.isNotBlank()) {
            Log.d("StudentHomeScreen", "AuthToken received, loading dashboard.")
            dashboardViewModel.loadDashboard(authToken)
        } else {
            Log.w("StudentHomeScreen", "AuthToken is blank. Cannot load dashboard.")
        }
    }

    // Observe LiveData states
    val classes by dashboardViewModel.classes.observeAsState(initial = emptyList())
    val isLoading by dashboardViewModel.isLoading.observeAsState(initial = false)
    val error by dashboardViewModel.error.observeAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF001E2F))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.scanin_logo_removebg_preview__1__2_layerstyle__1_),
                    contentDescription = "SCANIN Logo",
                    modifier = Modifier.size(52.dp)
                )
            }

            IconButton(
                onClick = { /* Open menu */ },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logeout),
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Hi $studentName,",
                style = Typography.titleLarge.copy(
                    color = Color(0xFF4A6572),
                    fontWeight = FontWeight.Normal
                )
            )

            Text(
                text = "Ready To Attend",
                style = Typography.titleLarge.copy(
                    color = Color(0xFF001E2F),
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = "Today?",
                style = Typography.titleLarge.copy(
                    color = Color(0xFF001E2F),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (error != null) {
                Text(
                    text = "Error loading dashboard: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
                )
            } else if (classes.isEmpty() && !isLoading) {
                Text(
                    text = "No courses available for you today.",
                    modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
                )
            } else {
                classes.forEach { classItem ->
                    CourseCard(
                        title = classItem.className,
                        teacher = classItem.teacherName,
                        onCardClick = {
                            onCourseClick(classItem.id, classItem.className, classItem.teacherName)
                        },
                        onScanClick = { onScanClick(classItem.id) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

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
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF001E2F))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = title,
                style = Typography.labelLarge.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Teacher:",
                style = Typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
            )

            Text(
                text = teacher,
                style = Typography.bodySmall.copy(color = Color.White.copy(alpha = 0.9f)),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCardClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color.White),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.dash),
                        contentDescription = "Dashboard",
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Dashboard",
                        style = Typography.bodySmall.copy(fontSize = 8.sp)
                    )
                }

                OutlinedButton(
                    onClick = onScanClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color.White),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.qr),
                        contentDescription = "Scan",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Scan",
                        style = Typography.bodySmall.copy(fontSize = 12.sp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentHomeScreenPreview() {
    StudentHomeScreen(authToken = "dummy_token_for_preview")
}