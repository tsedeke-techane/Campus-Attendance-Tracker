package com.attendance.attendancetracker.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * A lightweight DTO representing a student object, primarily used for parsing
 * lists of students where only basic information (like ID for counting) is needed immediately.
 */
data class StudentLiteDto(
    @SerializedName("_id") val id: String?
    // Add other fields like 'name' if they might be useful from this context
)