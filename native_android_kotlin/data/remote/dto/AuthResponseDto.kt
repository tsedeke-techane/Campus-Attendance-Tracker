package com.attendance.attendancetracker.data.remote.dto

import com.attendance.attendancetracker.data.models.AuthResponse

data class AuthResponseDto(
    val token: String,
    val user: UserDto // UserDto now has id, name, email, role, ID
)

fun AuthResponseDto.toDomain(): AuthResponse {
    return AuthResponse(
        token = this.token,
        userId = this.user.id, // user.id is the backend's unique user identifier
        name = this.user.name,
        role = this.user.role ?: "" // Map role, default to empty string if null
    )
} 