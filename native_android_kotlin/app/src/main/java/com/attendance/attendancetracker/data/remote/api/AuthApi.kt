package com.attendance.attendancetracker.data.remote.api

import com.attendance.attendancetracker.data.remote.dto.AttendanceHistoryResponse
import com.attendance.attendancetracker.data.models.AddStudentRequest
import com.attendance.attendancetracker.data.models.AddStudentResponse
import com.attendance.attendancetracker.data.models.AttendanceScanRequest
import com.attendance.attendancetracker.data.models.AttendanceScanResponse
import com.attendance.attendancetracker.data.models.ClassItem
import com.attendance.attendancetracker.data.models.ClassRequest
import com.attendance.attendancetracker.data.models.CreateClassResponse
import com.attendance.attendancetracker.data.models.DeleteClassResponse
import com.attendance.attendancetracker.data.models.GenerateQrRequest
import com.attendance.attendancetracker.data.models.GenerateQrResponse
import com.attendance.attendancetracker.data.remote.dto.AuthResponseDto
import com.attendance.attendancetracker.data.remote.dto.DashboardResponse
import com.attendance.attendancetracker.data.remote.dto.LoginRequestDto
import com.attendance.attendancetracker.data.remote.dto.LogoutRequestDto
import com.attendance.attendancetracker.data.remote.dto.LogoutResponseDto
import com.attendance.attendancetracker.data.remote.dto.SignupRequestDto
import com.attendance.attendancetracker.data.remote.dto.StudentAttendanceHistoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: SignupRequestDto): AuthResponseDto

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("api/auth/logout")
    suspend fun logout(@Body request: LogoutRequestDto): LogoutResponseDto

    @GET("class/Dashboard")
    suspend fun getDashboardClasses(
        @Header("Authorization") token: String
    ): Response<DashboardResponse>

    @POST("teacher/create-class")
    suspend fun createClass(
        @Body classRequest: ClassRequest,
        @Header("Authorization") token: String
    ): Response<CreateClassResponse>

    @DELETE("teacher/delete-class/{classId}")
    suspend fun deleteClass(
        @Path("classId") classId: String,
        @Header("Authorization") token: String
    ): Response<DeleteClassResponse>

    @GET("attendance/class/{classId}/history")
    suspend fun getAttendanceHistory(
        @Path("classId") classId: String,
        @Header("Authorization") token: String
    ): Response<AttendanceHistoryResponse>

    @GET("attendance/history/class/{classId}")
    suspend fun getStudentAttendanceHistory(
        @Path("classId") classId: String,
        @Header("Authorization") token: String
    ): Response<StudentAttendanceHistoryResponse>
    
    @POST("attendance/scan")
    suspend fun scanAttendance(
        @Body request: AttendanceScanRequest,
        @Header("Authorization") token: String
    ): Response<AttendanceScanResponse>
    
    @POST("teacher/class/{classId}/students")
    suspend fun addStudentToClass(
        @Path("classId") classId: String,
        @Body request: AddStudentRequest,
        @Header("Authorization") token: String
    ): Response<AddStudentResponse>

    @POST("attendance/generate")
    suspend fun generateQrCode(
        @Body request: GenerateQrRequest,
        @Header("Authorization") token: String
    ): Response<GenerateQrResponse>
}