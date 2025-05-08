package com.attendance.attendancetracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.attendance.attendancetracker.presentation.pages.AttendanceSummaryScreen
import com.attendance.attendancetracker.presentation.pages.CourseDashboardScreen
import com.attendance.attendancetracker.presentation.pages.LoginScreen
import com.attendance.attendancetracker.presentation.pages.OnboardingScreen
import com.attendance.attendancetracker.presentation.pages.QRGeneratorScreen
import com.attendance.attendancetracker.presentation.pages.QRScannerScreen
import com.attendance.attendancetracker.presentation.pages.SectionDetailScreen
import com.attendance.attendancetracker.presentation.pages.SignUpScreen
import com.attendance.attendancetracker.presentation.pages.StudentHomeScreen
import com.attendance.attendancetracker.presentation.pages.TeacherHomeScreen
import com.attendance.attendancetracker.presentation.viewmodels.AuthViewModel
import android.util.Log

object Routes {
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val STUDENT_HOME = "student_home"
    const val TEACHER_HOME = "teacher_home"
    const val COURSE_DASHBOARD = "course_dashboard/{courseName}"
    const val QR_SCANNER = "qr_scanner/{courseName}"
    const val SECTION_DETAIL = "section_detail/{courseName}"
    const val ATTENDANCE_SUMMARY = "attendance_summary/{sectionName}"
    const val QR_GENERATOR = "qr_generator/{courseName}"

    // Helper functions for parameterized routes
    fun courseDashboard(courseName: String) = "course_dashboard/$courseName"
    fun qrScanner(courseName: String) = "qr_scanner/$courseName"
    fun sectionDetail(courseName: String) = "section_detail/$courseName"
    fun attendanceSummary(sectionName: String) = "attendance_summary/$sectionName"
    fun qrGenerator(courseName: String) = "qr_generator/$courseName"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.ONBOARDING
) {
    // Create a single shared AuthViewModel for all routes
    val authViewModel: AuthViewModel = hiltViewModel()
    Log.d("AppNavigation", "Creating shared AuthViewModel at NavHost level")
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onGetStartedClick = { navController.navigate(Routes.SIGNUP) },
                onSkipClick = { navController.navigate(Routes.LOGIN) }
            )
        }

        composable(Routes.LOGIN) {
            // Using the shared AuthViewModel from NavHost level
            Log.d("AppNavigation", "LOGIN route - Using shared AuthViewModel, authState: ${authViewModel.authState?.isSuccess}")
            LoginScreen(
                onSignUpClick = {
                    navController.navigate(Routes.SIGNUP)
                },
                onLoginSuccess = { isTeacher ->
                    // Navigate based on user role
                    val destination = if (isTeacher) Routes.TEACHER_HOME else Routes.STUDENT_HOME
                    navController.navigate(destination) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                viewModel = authViewModel
            )
        }

        composable(Routes.SIGNUP) {
            // Using the shared AuthViewModel from NavHost level
            Log.d("AppNavigation", "SIGNUP route - Using shared AuthViewModel, authState: ${authViewModel.authState?.isSuccess}")
            SignUpScreen(
                onLoginClick = { navController.navigate(Routes.LOGIN) },
                onSignUpSuccess = { isTeacher ->
                    // Navigate based on selected role
                    val destination = if (isTeacher) Routes.TEACHER_HOME else Routes.STUDENT_HOME
                    navController.navigate(destination) {
                        popUpTo(Routes.SIGNUP) { inclusive = true }
                    }
                },
                viewModel = authViewModel
            )
        }

        composable(Routes.STUDENT_HOME) {
            // Using the shared AuthViewModel from NavHost level
            val currentAuthState = authViewModel.authState
            val token = currentAuthState?.getOrNull()?.token ?: ""
            Log.d("AppNavigation", "STUDENT_HOME - Using shared AuthViewModel, currentAuthState: ${currentAuthState?.isSuccess}, token: '$token'")

            StudentHomeScreen(
                authToken = token, // Pass the extracted token
                onCourseClick = { courseName ->
                    navController.navigate(Routes.courseDashboard(courseName))
                },
                onScanClick = { courseName ->
                    navController.navigate(Routes.qrScanner(courseName))
                }
            )
        }

        composable(Routes.TEACHER_HOME) {
            TeacherHomeScreen(
                onSectionClick = { courseName ->
                    navController.navigate(Routes.sectionDetail(courseName))
                },
                onAddNewClassClick = {
                    // In a real app, you would navigate to a form to add a new class
                }
            )
        }

        composable(
            route = Routes.COURSE_DASHBOARD,
            arguments = listOf(navArgument("courseName") { type = NavType.StringType })
        ) { backStackEntry ->
            // Using the shared AuthViewModel from NavHost level
            val authResult = authViewModel.authState?.getOrNull() // Get auth result
            val token = authResult?.token ?: "" // Extract token, default to empty

            val courseName = backStackEntry.arguments?.getString("courseName") ?: "Cyber Security"

            // Map course names to teacher names (in a real app, this would come from a database)
            val teacherMap = remember {
                mapOf(
                    "Cyber Security" to "Senayit Demisse",
                    "Operating System" to "Senayit Demisse",
                    "Mobile" to "Sara Mohammed",
                    "Artificial Intelligence" to "Manyazewal Eshetu",
                    "Graphics" to "Abebe Tessema",
                    "Operating System 2" to "Teshome Chane"
                )
            }

            CourseDashboardScreen(
                courseName = courseName,
                teacherName = teacherMap[courseName] ?: "",
                authToken = token, // Pass the token
                onBackClick = { navController.popBackStack() },
                onScanClick = { navController.navigate(Routes.qrScanner(courseName)) }
            )
        }

        composable(
            route = Routes.QR_SCANNER,
            arguments = listOf(navArgument("courseName") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseName = backStackEntry.arguments?.getString("courseName") ?: "Cyber Security"

            // Map course names to teacher names
            val teacherMap = remember {
                mapOf(
                    "Cyber Security" to "Senayit Demisse",
                    "Operating System" to "Senayit Demisse",
                    "Mobile" to "Sara Mohammed",
                    "Artificial Intelligence" to "Manyazewal Eshetu",
                    "Graphics" to "Abebe Tessema",
                    "Operating System 2" to "Teshome Chane"
                )
            }

            QRScannerScreen(
                courseName = courseName,
                teacherName = teacherMap[courseName] ?: "",
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.SECTION_DETAIL,
            arguments = listOf(navArgument("courseName") { type = NavType.StringType })
        ) { backStackEntry ->
            // Using the shared AuthViewModel from NavHost level
            val courseName = backStackEntry.arguments?.getString("courseName") ?: "DefaultCourse"
            val authResult = authViewModel.authState?.getOrNull()
            val token = authResult?.token ?: ""

            SectionDetailScreen(
                courseName = courseName,
                authToken = token,
                onBackClick = { navController.popBackStack() },
                onGenerateQRClick = { actualCourseName ->
                    navController.navigate(Routes.qrGenerator(actualCourseName))
                }
            )
        }

        composable(
            route = Routes.ATTENDANCE_SUMMARY,
            arguments = listOf(navArgument("sectionName") { type = NavType.StringType })
        ) { backStackEntry ->
            // Using the shared AuthViewModel from NavHost level
            val sectionName = backStackEntry.arguments?.getString("sectionName") ?: "Section 1"
            val authResult = authViewModel.authState?.getOrNull()
            val token = authResult?.token ?: ""

            AttendanceSummaryScreen(
                sectionName = sectionName,
                authToken = token,
                onBackClick = { navController.popBackStack() },
                onAddNewStudentClick = {
                    // In a real app, you would navigate to a form to add a new student
                }
            )
        }

        composable(
            route = Routes.QR_GENERATOR,
            arguments = listOf(navArgument("courseName") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseName = backStackEntry.arguments?.getString("courseName") ?: ""
            // Using shared AuthViewModel if needed in the future
            // val authResult = authViewModel.authState?.getOrNull()
            // val token = authResult?.token ?: ""

            // Temporary: Use the duplicated teacherMap. This should be refactored later.
            val teacherMap = remember {
                mapOf(
                    "Cyber Security" to "Senayit Demisse",
                    "Operating System" to "Senayit Demisse",
                    "Mobile" to "Sara Mohammed",
                    "Artificial Intelligence" to "Manyazewal Eshetu",
                    "Graphics" to "Abebe Tessema",
                    "Operating System 2" to "Teshome Chane",
                    "DefaultCourse" to "N/A" // Added for safety, assuming courseName could be DefaultCourse
                    // Add other courses as needed
                )
            }
            val teacherName = teacherMap[courseName] ?: "Unknown Teacher"

            QRGeneratorScreen(
                courseName = courseName,
                teacherName = teacherName, // Provide teacherName
                // authToken = token, // authToken removed as QRGeneratorScreen doesn't take it
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}