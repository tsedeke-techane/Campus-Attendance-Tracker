package com.attendance.attendancetracker.data.remote.dto

import com.attendance.attendancetracker.data.models.ClassItem
import com.google.gson.annotations.SerializedName
import com.google.gson.JsonElement
import com.google.gson.Gson
import com.attendance.attendancetracker.data.remote.dto.StudentLiteDto

// Main response wrapper
data class DashboardResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("count") val count: Int,
    @SerializedName("data") val data: List<ClassItemDto>? // List can be null or empty
)

// DTO for items within the 'data' array
data class ClassItemDto(
    @SerializedName("_id") val id: String,
    @SerializedName("className") val className: String?,
    @SerializedName("section") val section: String?,
    @SerializedName("schedule") val schedule: ScheduleDto?,
    @SerializedName("students") val students: JsonElement?, // Using JsonElement to handle both arrays of strings and objects
    @SerializedName("teacher") val teacher: JsonElement?,
    @SerializedName("createdAt") val createdAt: String? // Consider converting to Date/LocalDateTime in mapper
)

// DTO for the 'schedule' object
data class ScheduleDto(
    @SerializedName("days") val days: List<String>?
    // Add other fields if your 'schedule' object has more details
)

// Mapper function for ClassItemDto to ClassItem domain model
fun ClassItemDto.toDomain(): ClassItem {
    val finalTeacherId: String
    val finalTeacherName: String

    if (this.teacher != null && !this.teacher.isJsonNull) {
        if (this.teacher.isJsonObject) {
            // Case: Teacher is an object (Student's view)
            val teacherDto = Gson().fromJson(this.teacher, TeacherDto::class.java)
            finalTeacherId = teacherDto.id ?: "N/A"
            finalTeacherName = teacherDto.name ?: "N/A"
        } else if (this.teacher.isJsonPrimitive && this.teacher.asJsonPrimitive.isString) {
            // Case: Teacher is a String ID (Teacher's view)
            finalTeacherId = this.teacher.asString
            finalTeacherName = "N/A" 
        } else {
            finalTeacherId = "N/A"
            finalTeacherName = "N/A"
        }
    } else {
        finalTeacherId = "N/A"
        finalTeacherName = "N/A"
    }

    // Extract student IDs based on the type of students field
    val studentIds = when {
        this.students == null || this.students.isJsonNull -> emptyList()
        this.students.isJsonArray -> {
            val jsonArray = this.students.asJsonArray
            // Handle both cases: array of strings or array of objects
            jsonArray.map { element ->
                when {
                    element.isJsonPrimitive -> element.asString // Direct string ID
                    element.isJsonObject -> {
                        // Object with ID field
                        val studentObj = element.asJsonObject
                        if (studentObj.has("_id")) studentObj.get("_id").asString else ""
                    }
                    else -> "" // Handle other cases
                }
            }.filter { it.isNotEmpty() }
        }
        else -> emptyList() // Fallback for unexpected formats
    }

    return ClassItem(
        id = this.id,
        className = this.className ?: "N/A",
        section = this.section ?: "N/A",
        scheduleDays = this.schedule?.toDomain() ?: emptyList(),
        teacherId = finalTeacherId,
        teacherName = finalTeacherName,
        students = studentIds, 
        createdAt = this.createdAt ?: "N/A"
    )
}

// Mapper function for ScheduleDto to List<String> (domain representation of schedule days)
fun ScheduleDto.toDomain(): List<String> {
    return this.days ?: emptyList()
}