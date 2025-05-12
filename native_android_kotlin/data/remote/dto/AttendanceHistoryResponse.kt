package com.attendance.attendancetracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AttendanceHistoryResponse(
    val success: Boolean,
    @SerializedName("class")
    val classInfo: ClassInfo,
    val overallStats: OverallStats,
    val history: List<HistoryItem> // Reserved for future use
)

data class ClassInfo(
    val name: String,
    val section: String,
    val totalStudents: Int
)

data class OverallStats(
    val averageAttendance: Int,
    val totalClasses: Int,
    val students: List<StudentInfo>
)

data class StudentInfo(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val email: String,
    @SerializedName("ID")
    val studentOrStaffId: String,
    val attendancePercentage: Int
)

data class HistoryItem(
    @SerializedName("_id")
    val id: String,
    val date: String,
    val presentStudents: List<String>
)

// --- New Data Classes for Student-Specific Attendance History ---

data class StudentAttendanceHistoryResponse(
    val success: Boolean,
    @SerializedName("class")
    val classDetail: StudentClassDetail,
    val statistics: StudentAttendanceStatistics,
    val history: List<DailyAttendanceRecord>
)

data class StudentClassDetail(
    val name: String,
    val section: String
)

data class StudentAttendanceStatistics(
    val totalClasses: Int,
    val presentCount: Int,
    val absentCount: Int,
    val attendancePercentage: Int
)

data class DailyAttendanceRecord(
    val date: String, // Format: "YYYY-MM-DD"
    val status: String // e.g., "present", "absent"
)