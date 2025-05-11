package com.attendance.attendancetracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TeacherDto(
    @SerializedName("_id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("ID") val staffId: String?, // Assuming 'ID' from JSON is staffId
    @SerializedName("email") val email: String?
    // Add other fields if needed, like role, classes, createdAt, __v
)
