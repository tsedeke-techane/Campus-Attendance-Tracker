package com.attendance.attendancetracker.data.models

data class ClassItem(
    val id: String,
    val className: String,
    val section: String,
    val scheduleDays: List<String>, // Represents days like ["Mon", "Wed"]
    val teacherId: String, // Store teacher's ID
    val teacherName: String, // Store teacher's Name
    val students: List<String>?, // Added field for student IDs
    val createdAt: String // Store creation date as string for now
)