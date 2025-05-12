package com.attendance.attendancetracker.data.models

data class AuthResponse(
    val token: String,
    val userId: String,
    val name: String,
    val role: String
)