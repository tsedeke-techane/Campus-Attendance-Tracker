package com.attendance.attendancetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.attendance.attendancetracker.navigation.AppNavigation
import com.attendance.attendancetracker.navigation.Routes
import dagger.hilt.android.AndroidEntryPoint
//import com.attendance.attendancetracker.ui.theme.AttendanceTrackerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Start with the onboarding screen
                    AppNavigation(startDestination = Routes.ONBOARDING)
                }
            }
        }
    }