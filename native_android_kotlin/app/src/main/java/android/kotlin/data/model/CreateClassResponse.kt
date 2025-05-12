package com.attendance.attendancetracker.data.models

import com.google.gson.annotations.SerializedName

data class CreateClassResponse(
    val success: Boolean,
    val data: ClassData?, // Making it nullable in case data is not always present on failure
    val message: String? // Optional message field
)

data class ClassData(
    val className: String,
    val section: String,
    val teacher: String, // Assuming teacher is an ID string
    val students: List<String>, // Assuming students is a list of IDs or can be an empty list
    @SerializedName("_id") // Mapping the JSON field _id to id
    val id: String,
    val createdAt: String,
    @SerializedName("__v")
    val version: Int? // __v is often an integer version key
)