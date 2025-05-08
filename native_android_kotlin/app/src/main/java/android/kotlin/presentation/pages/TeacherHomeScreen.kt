package com.attendance.attendancetracker.presentation.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.data.models.ClassItem
import com.attendance.attendancetracker.presentation.viewmodels.AuthViewModel
import com.attendance.attendancetracker.presentation.viewmodels.DashboardViewModel
import com.attendance.attendancetracker.presentation.viewmodels.TeacherViewModel
import com.attendance.attendancetracker.ui.theme.Typography
import android.util.Log
import android.widget.Toast

data class ClassSection(val name: String, val studentCount: Int, val originalId: String)

@Composable
fun TeacherHomeScreen(
    authToken: String = "", // Receive auth token directly as parameter
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    teacherViewModel: TeacherViewModel = hiltViewModel(),
    onSectionClick: (String) -> Unit = {}
) {
    val classes by dashboardViewModel.classes.observeAsState(initial = emptyList())
    val isLoading by dashboardViewModel.isLoading.observeAsState(initial = false)
    val error by dashboardViewModel.error.observeAsState(initial = null)

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Log.d("TeacherHomeScreen", "Composition: authToken='${authToken}'")

    LaunchedEffect(authToken) {
        Log.d("TeacherHomeScreen", "LaunchedEffect triggered. Current token: '$authToken'")
        if (authToken.isNotBlank()) {
            Log.d("TeacherHomeScreen", "Token is valid. Loading dashboard with token: $authToken")
            dashboardViewModel.loadDashboard(authToken)
        } else {
            Log.d("TeacherHomeScreen", "Token is blank inside LaunchedEffect. Dashboard not loaded.")
        }
    }

    LaunchedEffect(teacherViewModel.isClassCreated) {
        if (teacherViewModel.isClassCreated) {
            Toast.makeText(context, "Class created successfully!", Toast.LENGTH_SHORT).show()
            if (authToken.isNotBlank()) {
                dashboardViewModel.loadDashboard(authToken) // Refresh dashboard
            }
            showDialog = false // Close dialog on success
            teacherViewModel.onStateHandled() // Reset state
        }
    }

    LaunchedEffect(teacherViewModel.errorMessage) {
        if (teacherViewModel.errorMessage.isNotEmpty()) {
            Toast.makeText(context, teacherViewModel.errorMessage, Toast.LENGTH_LONG).show()
            teacherViewModel.onStateHandled() // Reset state
        }
    }

    val classSections = remember(classes) {
        classes.map {
            ClassSection(
                name = it.className,
                studentCount = it.students?.size ?: 0,
                originalId = it.id
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
    ) {
        Column {
            Header()

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Teacher’s Dashboard",
                            style = Typography.titleLarge.copy(
                                color = Color(0xFF001E2F),
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Track student participation, generate reports, and stay organized",
                            style = Typography.bodyMedium.copy(color = Color(0xFF4A6572)),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.gg_qr),
                        contentDescription = "Logo",
                        modifier = Modifier.size(40.dp)
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (error != null) {
                    Text(
                        text = "Error: $error",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(classSections) { section ->
                            SectionCard(
                                section = section,
                                onClick = { onSectionClick(section.originalId) },
                                onDashboardClick = { onSectionClick(section.originalId) },
                                onDeleteClick = {
                                    dashboardViewModel.deleteClassLocally(section.originalId)
                                    Toast.makeText(context, "Class removed locally", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }

                FloatingActionButton(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Filled.Add, "Add new class", tint = Color.White)
                }
            }
        }
    }

    if (showDialog) {
        AddClassDialog(
            onDismissRequest = { showDialog = false },
            onAddClick = { className ->
                if (authToken.isNotBlank()) {
                    // For now, section is hardcoded as "S1"
                    teacherViewModel.createClass(className = className, section = "S1", token = authToken)
                } else {
                    Toast.makeText(context, "Auth token not available", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF001E2F))
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.scanin_logo_removebg_preview__1__2_layerstyle__1_),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(40.dp)
        )
    }
}

@Composable
fun SectionCard(
    section: ClassSection,
    onClick: () -> Unit,
    onDashboardClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF001E2F))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = section.name,
                    style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${section.studentCount} Students",
                    style = Typography.bodySmall,
                    color = Color.Gray
                )
            }

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onDashboardClick,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color.White),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.dash),
                        contentDescription = "Dashboard",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Dashboard",
                        style = Typography.labelSmall
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Delete Class",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun AddClassDialog(
    onDismissRequest: () -> Unit,
    onAddClick: (className: String) -> Unit
) {
    var className by remember { mutableStateOf("") }
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF001E2F)) // Dark background for card
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = className,
                    onValueChange = { className = it },
                    placeholder = { Text("Name of the class", color = Color.Gray) },
                    label = { Text("Name of the class", color = Color.White) },
                    textStyle = TextStyle(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.LightGray,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest, // "Dashboard" button acts as cancel
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.White),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.dash), // Assuming R.drawable.dash exists
                            contentDescription = "Dashboard/Cancel",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Dashboard", style = Typography.labelSmall)
                    }

                    OutlinedButton(
                        onClick = {
                            if (className.isNotBlank()) {
                                onAddClick(className)
                            } else {
                                Toast.makeText(context, "Class name cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.White),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("+ Add", style = Typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddClassDialogPreview() {
//    AttendanceTrackerTheme {
        AddClassDialog(onDismissRequest = {}, onAddClick = {})
//    }
}

@Preview(showBackground = true)
@Composable
fun TeacherHomeScreenPreview() {
    TeacherHomeScreen(authToken = "sample-preview-token")
}
