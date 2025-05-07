package com.attendance.attendancetracker.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attendance.attendancetracker.data.models.ClassItem
import com.attendance.attendancetracker.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository
    // Potentially inject DataStoreManager here if you need to fetch the token
) : ViewModel() {

    private val _classes = MutableLiveData<List<ClassItem>>()
    val classes: LiveData<List<ClassItem>> = _classes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadDashboard(token: String) {
        Log.d("DashboardViewModel", "loadDashboard called with token: $token")
        viewModelScope.launch {
            _isLoading.postValue(true)
            _error.postValue(null)
            // Ensure the token is prefixed with "Bearer " if not already done
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            Log.d("DashboardViewModel", "Attempting to fetch dashboard with bearerToken: $bearerToken")
            val result = repository.getDashboard(bearerToken)
            
            // Using result.fold for a more idiomatic Kotlin way to handle Success/Failure
            result.fold(
                onSuccess = { classList ->
                    Log.d("DashboardViewModel", "Successfully fetched ${classList.size} classes.")
                    _classes.postValue(classList)
                    _isLoading.postValue(false)
                },
                onFailure = { exception ->
                    Log.e("DashboardViewModel", "Error loading dashboard", exception)
                    _error.postValue(exception.message ?: "Unknown error")
                    _isLoading.postValue(false)
                    // You might want to log the full exception here
                }
            )
        }
    }

    fun deleteClassLocally(classId: String) {
        val currentClasses = _classes.value?.toMutableList() ?: mutableListOf()
        currentClasses.removeAll { it.id == classId }
        _classes.postValue(currentClasses)
    }
}
