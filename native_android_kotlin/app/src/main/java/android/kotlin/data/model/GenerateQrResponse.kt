package com.attendance.attendancetracker.data.models

import com.google.gson.annotations.SerializedName

data class GenerateQrResponse(
    @SerializedName("qrCodeImage")
    val qrCodeImage: String, // Base64 encoded image string

    @SerializedName("expiresAt")
    val expiresAt: String, // ISO 8601 date string

    @SerializedName("attendanceId")
    val attendanceId: String
)