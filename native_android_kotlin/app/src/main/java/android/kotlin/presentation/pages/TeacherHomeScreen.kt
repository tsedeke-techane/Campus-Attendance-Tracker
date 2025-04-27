package com.attendance.attendancetracker.presentation.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.ui.theme.Typography

@Composable
fun TeacherHomeScreen(
    teacherName: String = "Senayit Demisse",
    onSectionClick: (String) -> Unit = {},
    onAddNewClassClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
    ) {
        // Header with logo
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
            // Dashboard title
            Text(
                text = "Teacher's Dashboard",
                style = Typography.titleLarge.copy(
                    color = Color(0xFF001E2F),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            Text(
                text = "Track student attendance, manage your sections, and stay organized.",
                style = Typography.bodyMedium.copy(color = Color(0xFF4A6572)),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Sections grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SectionCard(
                    sectionName = "Section 1",
                    modifier = Modifier.weight(1f),
                    onClick = { onSectionClick("Section 1") }
                )
                
                SectionCard(
                    sectionName = "Section 2",
                    modifier = Modifier.weight(1f),
                    onClick = { onSectionClick("Section 2") }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SectionCard(
                    sectionName = "Section 3",
                    modifier = Modifier.weight(1f),
                    onClick = { onSectionClick("Section 3") }
                )
                
                SectionCard(
                    sectionName = "Section 4",
                    modifier = Modifier.weight(1f),
                    onClick = { onSectionClick("Section 4") }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SectionCard(
                    sectionName = "Section 5",
                    modifier = Modifier.weight(1f),
                    onClick = { onSectionClick("Section 5") }
                )
                
                // Add New Class card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .clickable { onAddNewClassClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFF001E2F).copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Add New Class",
                            tint = Color(0xFF001E2F),
                            modifier = Modifier.size(24.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Add New Class",
                            style = Typography.bodyMedium.copy(
                                color = Color(0xFF001E2F),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionCard(
    sectionName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF001E2F))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = sectionName,
                style = Typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { onClick() },
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "View Section",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeacherHomeScreenPreview() {
    TeacherHomeScreen()
}
