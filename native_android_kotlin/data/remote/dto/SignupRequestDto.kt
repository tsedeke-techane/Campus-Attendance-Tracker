package com.attendance.attendancetracker.data.remote.dto

data class SignupRequestDto(
    val name: String,
    val email: String,
    val password: String,
    val ID: String,
    val role: String
) 