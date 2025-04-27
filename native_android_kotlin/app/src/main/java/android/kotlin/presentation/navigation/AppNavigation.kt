package com.attendance.attendancetracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

object Routes {
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val STUDENT_HOME = "student_home"
    const val TEACHER_HOME = "teacher_home"
    const val COURSE_DASHBOARD = "course_dashboard/{courseName}"
    const val QR_SCANNER = "qr_scanner/{courseName}"
    const val SECTION_DETAIL = "section_detail/{sectionName}"
    const val ATTENDANCE_SUMMARY = "attendance_summary/{sectionName}"
    const val QR_GENERATOR = "qr_generator/{courseName}"

    // Helper functions for parameterized routes
    fun courseDashboard(courseName: String) = "course_dashboard/$courseName"
    fun qrScanner(courseName: String) = "qr_scanner/$courseName"
    fun sectionDetail(sectionName: String) = "section_detail/$sectionName"
    fun attendanceSummary(sectionName: String) = "attendance_summary/$sectionName"
    fun qrGenerator(courseName: String) = "qr_generator/$courseName"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.ONBOARDING
) {
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
                }
            )
        }

        composable(Routes.SIGNUP) {
            SignUpScreen(
                onLoginClick = { navController.navigate(Routes.LOGIN) },
                onSignUpSuccess = { isTeacher ->
                    // Navigate based on selected role
                    val destination = if (isTeacher) Routes.TEACHER_HOME else Routes.STUDENT_HOME
                    navController.navigate(destination) {
                        popUpTo(Routes.SIGNUP) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.STUDENT_HOME) {
            StudentHomeScreen(
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
                onSectionClick = { sectionName ->
                    navController.navigate(Routes.sectionDetail(sectionName))
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
            arguments = listOf(navArgument("sectionName") { type = NavType.StringType })
        ) { backStackEntry ->
            val sectionName = backStackEntry.arguments?.getString("sectionName") ?: "Section 1"

            SectionDetailScreen(
                sectionName = sectionName,
                onBackClick = { navController.popBackStack() },
                onAddNewStudentClick = {
                    // In a real app, you would navigate to a form to add a new student
                },
                onGenerateQRClick = { courseName ->
                    navController.navigate(Routes.qrGenerator(courseName))
                }
            )
        }

        composable(
            route = Routes.ATTENDANCE_SUMMARY,
            arguments = listOf(navArgument("sectionName") { type = NavType.StringType })
        ) { backStackEntry ->
            val sectionName = backStackEntry.arguments?.getString("sectionName") ?: "Section 1"

            AttendanceSummaryScreen(
                sectionName = sectionName,
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

            QRGeneratorScreen(
                courseName = courseName,
                teacherName = teacherMap[courseName] ?: "",
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
