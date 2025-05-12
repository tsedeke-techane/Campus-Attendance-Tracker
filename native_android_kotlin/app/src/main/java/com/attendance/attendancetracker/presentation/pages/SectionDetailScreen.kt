package com.attendance.attendancetracker.presentation.pages

import android.util.Log
import android.widget.Toast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.attendance.attendancetracker.R
// Removed BackButton import as it's not available
import com.attendance.attendancetracker.data.models.GenerateQrResponse
import com.attendance.attendancetracker.presentation.viewmodels.AuthViewModel
import com.attendance.attendancetracker.presentation.viewmodels.AttendanceViewModel
import com.attendance.attendancetracker.presentation.viewmodels.TeacherViewModel
import com.attendance.attendancetracker.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun SectionDetailScreen(
    courseName: String,
    authToken: String,
    onBackClick: () -> Unit = {},
    onGenerateQRClick: (String) -> Unit = {},
    attendanceViewModel: AttendanceViewModel = hiltViewModel(),
    teacherViewModel: TeacherViewModel = hiltViewModel()
) {
    var students by remember {
        mutableStateOf(
            mutableListOf(
                Student("Tsedeke Techane", "UGR/1234/15", 40),
                Student("Anat Esayas", "UGR/5002/15", 90),
                Student("Nuel Mezemir", "UGR/3456/15", 100),
                Student("Kalkidan Semere", "UGR/3894/15", 60),
                Student("Biruk Siyoum", "UGR/2486/15", 80),
                Student("Student Six", "UGR/6000/15", 50),
                Student("Student Seven", "UGR/7000/15", 75),
                Student("Student Eight", "UGR/8000/15", 85),
                Student("Student Nine", "UGR/9000/15", 95),
                Student("Student Ten", "UGR/10000/15", 88)
            )
        )
    }

    // Controls visibility of the add student dialog
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newId by remember { mutableStateOf("") }
    var showAll by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope() // Add coroutine scope for suspend functions
    val visibleCount = 8
    
    // States from ViewModels
    val attendanceHistory by attendanceViewModel.attendanceHistory.observeAsState(null)
    val isLoading by attendanceViewModel.isLoading.observeAsState(initial = false)
    val error by attendanceViewModel.error.observeAsState()
    
    // Add student states
    val isStudentAdded by teacherViewModel.isStudentAdded.observeAsState(false)
    val addStudentError by teacherViewModel.addStudentError.observeAsState()
    val isAddingStudent by teacherViewModel.isLoading.observeAsState(false)
    val context = LocalContext.current

    // QR Code Generation States
    var showQrDialog by remember { mutableStateOf(false) }
    val qrCodeResponse by teacherViewModel.qrCodeResponse.observeAsState()
    val qrCodeError by teacherViewModel.qrCodeError.observeAsState()
    val isGeneratingQr by teacherViewModel.isGeneratingQr.observeAsState(false)

    LaunchedEffect(key1 = courseName, key2 = authToken) {
        Log.d("SectionDetailScreen", "LaunchedEffect triggered. courseName: '" + courseName + "', authToken present: " + authToken.isNotBlank().toString())
        if (courseName.isNotBlank() && authToken.isNotBlank()) {
            Log.d("SectionDetailScreen", "Calling loadAttendanceHistory for classId: " + courseName)
            attendanceViewModel.loadAttendanceHistory(classId = courseName, token = authToken)
        } else {
            Log.w("SectionDetailScreen", "Skipping loadAttendanceHistory: courseName ('" + courseName + "') or authToken (present: " + authToken.isNotBlank().toString() + ") is blank.")
        }
    }
    
    // Handle student addition results
    LaunchedEffect(isStudentAdded, addStudentError) {
        if (isStudentAdded) {
            // Student was successfully added
            Toast.makeText(context, "Student added successfully!", Toast.LENGTH_SHORT).show()
            showAddDialog = false
            newName = ""
            newId = ""
            
            // Refresh attendance data to show the newly added student
            if (courseName.isNotBlank() && authToken.isNotBlank()) {
                attendanceViewModel.loadAttendanceHistory(classId = courseName, token = authToken)
            }
            
            // Reset the state in viewModel
            teacherViewModel.resetAddStudentState()
        }
        
        addStudentError?.let { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                teacherViewModel.resetAddStudentState()
            }
        }
    }
    
    // Handle QR Code generation results
    LaunchedEffect(qrCodeResponse, qrCodeError) {
        qrCodeResponse?.let {
            showQrDialog = true
            // Potentially reset LiveData in ViewModel if it shouldn't persist post-dialog
        }
        qrCodeError?.let {
            if (it.isNotEmpty()) {
                Toast.makeText(context, "QR Error: $it", Toast.LENGTH_LONG).show()
                // Reset error in ViewModel
                // teacherViewModel.resetQrErrorState() // You'll need to implement this
            }
        }
    }
    
    // Add Student Dialog
    if (showAddDialog) {
        Dialog(onDismissRequest = { showAddDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    // Dialog Title
                    Text(
                        text = "Add New Student",
                        style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF001E2F),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Student Name Field
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Student Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true
                    )
                    
                    // Student ID Field
                    OutlinedTextField(
                        value = newId,
                        onValueChange = { newId = it },
                        label = { Text("Student ID") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    
                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        // Cancel Button
                        TextButton(
                            onClick = { 
                                showAddDialog = false
                                newName = ""
                                newId = ""
                            }
                        ) {
                            Text("Cancel")
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Add Button with loading indicator
                        Button(
                            onClick = {
                                if (newName.isBlank() || newId.isBlank()) {
                                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Call API to add student
                                    teacherViewModel.addStudentToClass(
                                        classId = courseName,
                                        studentName = newName,
                                        studentId = newId,
                                        token = authToken
                                    )
                                }
                            },
                            enabled = !isAddingStudent,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001E2F))
                        ) {
                            if (isAddingStudent) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Add Student")
                            }
                        }
                    }
                }
            }
        }
    }

    // QR Code Dialog
    if (showQrDialog && qrCodeResponse != null) {
        QrCodeDialog(
            qrResponse = qrCodeResponse!!,
            onDismiss = { 
                showQrDialog = false 
                // teacherViewModel.resetQrResponseState() // You'll need to implement this
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            // Floating action button to show the add student dialog
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF001E2F)
            ) {
                Text("+", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color(0xFFECECEC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF001E2F))
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.scanin_logo_removebg_preview__1__2_layerstyle__1_),
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF001E2F)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = courseName,
                            style = Typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF001E2F)
                            )
                        )
                        Text(
                            text = "Total: ${students.size} Students",
                            style = Typography.bodySmall.copy(color = Color(0xFF4A6572))
                        )
                    }
                    IconButton(onClick = { 
                        teacherViewModel.generateQrCode(
                            classId = courseName,
                            token = authToken
                        )
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.gg_qr),
                            contentDescription = "Generate QR",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (error != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: $error", style = Typography.bodySmall, color = Color(0xFF4A6572))
                    }
                } else if (attendanceHistory != null) {
                    val history = attendanceHistory!!
                    Column(horizontalAlignment = Alignment.Start) {
                        // Display basic class info from response
                        Text("Class: ${history.classInfo?.name ?: "Unknown"}", style = Typography.titleMedium)
                        Text("Section: ${history.classInfo?.section ?: "Unknown"}", style = Typography.bodySmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Overall Attendance: ${history.overallStats?.averageAttendance ?: 0}%", style = Typography.bodySmall)
                        Text("Total Classes Conducted: ${history.overallStats?.totalClasses ?: 0}", style = Typography.bodySmall)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Student Attendance:", style = Typography.titleMedium)
                        val studentList = history.overallStats?.students ?: emptyList()
                        if (studentList.isEmpty()) {
                            Text("No student data available.", style = Typography.bodySmall)
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 300.dp) // Add a max height constraint
                            ) {
                                items(studentList) { student ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = student.name ?: "Unknown",
                                                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "ID: ${student.studentOrStaffId ?: "Unknown"}",
                                                style = Typography.bodySmall
                                            )
                                            Text(
                                                text = "Email: ${student.email ?: "Unknown"}",
                                                style = Typography.bodySmall
                                            )
                                            Text(
                                                text = "Attendance: ${student.attendancePercentage}%",
                                                style = Typography.bodySmall.copy(
                                                    color = if (student.attendancePercentage >= 75) Color(0xFF001E2F) else Color(0xFF4A6572)
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            val displayedStudents = if (showAll) students else students.take(visibleCount)

                            displayedStudents.forEachIndexed { index, student ->
                                StudentAttendanceItem(index + 1, student)
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            if (!showAll && students.size > visibleCount) {
                                IconButton(
                                    onClick = { showAll = true },
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(top = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Show More",
                                        tint = Color(0xFF001E2F)
                                    )
                                }
                            }


                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Fetched Attendance History",
                        style = Typography.titleLarge.copy(color = Color(0xFF001E2F)),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else if (error != null) {
                        Card(modifier = Modifier.fillMaxWidth(),  colors = CardDefaults.cardColors(containerColor = Color(0xFFE57373))) {
                            Text(
                                text = "Error: $error",
                                style = Typography.bodySmall.copy(color = Color(0xFF4A6572)),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else if (attendanceHistory != null) {
                        val history = attendanceHistory!!
                        Card(modifier = Modifier.fillMaxWidth(),  colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Class: ${history.classInfo.name}", style = Typography.titleMedium)
                                Text("Section: ${history.classInfo.section}", style = Typography.bodySmall)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Overall Attendance: ${history.overallStats.averageAttendance}%", style = Typography.bodySmall)
                                Text("Total Classes Conducted: ${history.overallStats.totalClasses}", style = Typography.bodySmall)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Student Breakdown:", style = Typography.titleMedium)
                                if (history.overallStats.students.isEmpty()) {
                                    Text("No student attendance data available from server.", style = Typography.bodySmall)
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)
                                    ) {
                                        items(history.overallStats.students) { student ->
                                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                                Text(
                                                    text = student.name,
                                                    style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                                )
                                                Text(
                                                    text = "ID: ${student.studentOrStaffId} - Email: ${student.email}",
                                                    style = Typography.bodySmall
                                                )
                                                Text(
                                                    text = "Attendance: ${student.attendancePercentage ?: 0}%",
                                                    style = Typography.bodySmall.copy(
                                                        color = if ((student.attendancePercentage ?: 0) >= 75) Color(0xFF00695C) else Color(0xFFD32F2F)
                                                    )
                                                )
                                                Divider(modifier = Modifier.padding(top = 4.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Text("No attendance history loaded yet or data is unavailable.", style = Typography.bodySmall, modifier = Modifier.padding(16.dp))
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun StudentAttendanceItem(index: Int, student: Student) {
    var showBottomSheet by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showBottomSheet = true },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                "$index. ${student.name}",
                fontWeight = FontWeight.Medium,
                color = Color(0xFF001E2F)
            )
            Text(
                student.id,
                color = Color(0xFF4A6572),
                style = Typography.bodySmall
            )
        }
        Text(
            "${student.attendancePercentage}%",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001E2F)
        )
    }
    
    if (showBottomSheet) {
        AttendanceSummaryBottomSheet(
            student = student,
            onDismiss = { showBottomSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceSummaryBottomSheet(student: Student, onDismiss: () -> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Header with title and close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Attendance Summary",
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF001E2F)
                )
                
                IconButton(onClick = onDismiss) {
                    Text("✕", color = Color(0xFF001E2F), fontWeight = FontWeight.Bold)
                }
            }
            
            // Student info with avatar
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Student avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF001E2F), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Student",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Student details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = student.name,
                        style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF001E2F)
                    )
                    
                    Text(
                        text = "Email: ${student.name.replace(" ", "").lowercase()}@gmail.com",
                        style = Typography.bodySmall,
                        color = Color(0xFF4A6572)
                    )
                    
                    Text(
                        text = student.id,
                        style = Typography.bodySmall,
                        color = Color(0xFF4A6572)
                    )
                }
                
                // Section info
                Text(
                    text = "Section 1",
                    style = Typography.bodySmall,
                    color = Color(0xFF4A6572)
                )
            }
            
            // Present/Absent percentage cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Present card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Present icon
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFF001E2F), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Present",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = "Present",
                                style = Typography.bodyMedium,
                                color = Color(0xFF001E2F)
                            )
                            
                            Text(
                                text = "${student.attendancePercentage}%",
                                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF001E2F)
                            )
                        }
                    }
                }
                
                // Absent card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Absent icon
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFF001E2F), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Absent",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = "Absent",
                                style = Typography.bodyMedium,
                                color = Color(0xFF001E2F)
                            )
                            
                            Text(
                                text = "${100 - student.attendancePercentage}%",
                                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF001E2F)
                            )
                        }
                    }
                }
            }
            
            // Attendance history grid
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Grid of attendance days
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        // Generate sample attendance days
                        items(21) { day ->
                            val status = when {
                                day == 9 || day == 16 -> "absent" // Red squares
                                day == 2 -> "excused" // Yellow square
                                else -> "present" // Dark blue squares
                            }
                            
                            val backgroundColor = when(status) {
                                "present" -> Color(0xFF001E2F)
                                "absent" -> Color(0xFFE57373)
                                "excused" -> Color(0xFFFFD54F)
                                else -> Color(0xFF001E2F)
                            }
                            
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(32.dp)
                                    .background(
                                        color = backgroundColor,
                                        shape = RoundedCornerShape(4.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${day + 1}",
                                    color = Color.White,
                                    style = Typography.bodySmall
                                )
                            }
                        }
                    }
                    
                    // Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Present legend
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color(0xFF001E2F), RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Present", style = Typography.bodySmall)
                        }
                        
                        // Excuse legend
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color(0xFFFFD54F), RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Excuse", style = Typography.bodySmall)
                        }
                        
                        // Absent legend
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color(0xFFE57373), RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Absent", style = Typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

data class Student(
    val name: String,
    val id: String,
    val attendancePercentage: Int
)

@Composable
fun QrCodeDialog(qrResponse: GenerateQrResponse, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Scan for Attendance", 
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Decode and display QR Code Image
                val imageBitmap = remember(qrResponse.qrCodeImage) {
                    try {
                        val pureBase64Encoded = qrResponse.qrCodeImage.substringAfter("base64,")
                        val imageBytes = android.util.Base64.decode(pureBase64Encoded, android.util.Base64.DEFAULT)
                        android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()
                    } catch (e: Exception) {
                        Log.e("QrCodeDialog", "Error decoding Base64 image", e)
                        null
                    }
                }

                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "QR Code",
                        modifier = Modifier
                            .size(250.dp)
                            .padding(bottom = 16.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text(
                        "Error displaying QR code.", 
                        color = Color.Red, 
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Text(
                    "Attendance ID: ${qrResponse.attendanceId}", 
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "Expires At: ${qrResponse.expiresAt}", // Consider formatting this date/time
                    style = Typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001E2F))
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionDetailScreenPreview() {
    SectionDetailScreen(courseName = "Sample Course", authToken = "sample_token")
}