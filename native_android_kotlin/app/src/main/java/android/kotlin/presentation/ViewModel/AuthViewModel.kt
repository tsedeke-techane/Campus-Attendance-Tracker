package com.attendance.attendancetracker.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attendance.attendancetracker.common.utils.ApiException
import com.attendance.attendancetracker.common.utils.UnknownApiException
import com.attendance.attendancetracker.data.models.AuthResponse
import com.attendance.attendancetracker.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var authState by mutableStateOf<Result<AuthResponse>?>(null)
        private set

    fun login(name: String, ID: String, password: String) {
        viewModelScope.launch {
            try {
                val result = repository.login(name, ID, password)
                authState = Result.success(result)
            } catch (e: ApiException) { 
                authState = Result.failure(e)
            } catch (e: Exception) { 
                authState = Result.failure(UnknownApiException("An unexpected system error occurred during login.", cause = e))
            }
        }
    }

    fun signup(name: String, studentOrStaffId: String, userEmail: String, password: String, role: String) {
        viewModelScope.launch {
            try {
                val result = repository.signup(name = name, email = userEmail, ID = studentOrStaffId, password = password, role = role)
                authState = Result.success(result)
            } catch (e: ApiException) { 
                authState = Result.failure(e)
            } catch (e: Exception) { 
                authState = Result.failure(UnknownApiException("An unexpected system error occurred during signup.", cause = e))
            }
        }
    }

    fun logout(name: String, ID: String, password: String) {
        viewModelScope.launch {
            try {
                repository.logout(name, ID, password)
                authState = null 
            } catch (e: ApiException) { 
                authState = Result.failure(e)
            } catch (e: Exception) { 
                authState = Result.failure(UnknownApiException("An unexpected system error occurred during logout.", cause = e))
            }
        }
    }

    fun clearAuthState() {
        authState = null
    }
} 