package com.attendance.attendancetracker.data.remote.dto

import com.attendance.attendancetracker.data.models.User

data class UserDto(
    val id: String,
    val name: String,
    val email: String?,
    val role: String?,
    val ID: String?
)

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        name = this.name
    )
} 