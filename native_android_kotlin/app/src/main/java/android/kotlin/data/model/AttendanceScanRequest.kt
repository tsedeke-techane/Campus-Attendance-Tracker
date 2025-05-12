package com.attendance.attendancetracker.data.models

data class AttendanceScanRequest(
    val token: String,
    val classId: String
)