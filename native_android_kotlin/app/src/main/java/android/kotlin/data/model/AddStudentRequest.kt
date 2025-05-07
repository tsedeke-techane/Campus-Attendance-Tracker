package com.attendance.attendancetracker.data.models

data class AddStudentRequest(
    val ID: String, // Note: API expects "ID" not "studentId"
    val name: String
)