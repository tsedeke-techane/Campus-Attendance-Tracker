package com.attendance.attendancetracker.data.models

import com.google.gson.annotations.SerializedName

data class GenerateQrRequest(
    @SerializedName("classId")
    val classId: String
)