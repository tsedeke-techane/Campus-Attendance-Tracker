package com.attendance.attendancetracker.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attendance.attendancetracker.data.models.AddStudentResponse
import com.attendance.attendancetracker.data.models.ClassRequest
import com.attendance.attendancetracker.data.models.CreateClassResponse
import com.attendance.attendancetracker.data.models.GenerateQrResponse
import com.attendance.attendancetracker.data.repository.TeacherRepository
import com.attendance.attendancetracker.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val repository: TeacherRepository
) : ViewModel() {

    var isClassCreated by mutableStateOf(false)
        private set // Only ViewModel can set this

    var errorMessage by mutableStateOf("")
        private set // Only ViewModel can set this
        
    // Add student states with LiveData (to make it easier to observe in Compose)
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _isStudentAdded = MutableLiveData<Boolean>(false)
    val isStudentAdded: LiveData<Boolean> = _isStudentAdded
    
    private val _addStudentError = MutableLiveData<String?>(null)
    val addStudentError: LiveData<String?> = _addStudentError

    private val _qrCodeResponse = MutableLiveData<GenerateQrResponse?>()
    val qrCodeResponse: LiveData<GenerateQrResponse?> = _qrCodeResponse

    private val _qrCodeError = MutableLiveData<String?>()
    val qrCodeError: LiveData<String?> = _qrCodeError

    private val _isGeneratingQr = MutableLiveData<Boolean>()
    val isGeneratingQr: LiveData<Boolean> = _isGeneratingQr

    private val _createClassError = MutableLiveData<String?>()
    val createClassError: LiveData<String?> = _createClassError

    private val _isClassCreated = MutableLiveData<Boolean>()
    val isClassCreatedLiveData: LiveData<Boolean> = _isClassCreated

    fun createClass(className: String, section: String, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _createClassError.value = null // Reset error before new operation
            _isClassCreated.value = false // Reset success state

            when (val result = repository.createClass(className, section, token)) {
                is Result.Success -> {
                    if (result.data?.success == true) {
                        _isClassCreated.value = true
                    } else {
                        _createClassError.value = result.data?.message ?: "Failed to create class (unknown reason)"
                    }
                }
                is Result.Error -> {
                    _createClassError.value = result.message
                }
                else -> {}
            }
            _isLoading.value = false
        }
    }

    // Call this from the UI after the state has been observed and handled (e.g., Toast shown)
    fun onStateHandled() {
        // Reset states if there was a definitive outcome from the last operation
        if (isClassCreated || errorMessage.isNotEmpty()) {
            isClassCreated = false
            errorMessage = ""
        }
    }
    
    /**
     * Adds a student to the specified class
     */
    fun addStudentToClass(classId: String, studentName: String, studentId: String, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _addStudentError.value = null // Reset error
            _isStudentAdded.value = false // Reset success state

            when (val result = repository.addStudentToClass(classId, studentName, studentId, token)) {
                is Result.Success -> {
                    if (result.data?.success == true) {
                        _isStudentAdded.value = true
                    } else {
                        _addStudentError.value = result.data?.message ?: "Failed to add student (unknown reason)"
                    }
                }
                is Result.Error -> {
                    _addStudentError.value = result.message
                }
                else -> {}
            }
            _isLoading.value = false
        }
    }
    
    /**
     * Reset add student state after it's been handled in the UI
     */
    fun resetAddStudentState() {
        _isStudentAdded.value = false
        _addStudentError.value = null
    }

    fun generateQrCode(classId: String, token: String) {
        viewModelScope.launch {
            _isGeneratingQr.value = true
            _qrCodeError.value = null
            _qrCodeResponse.value = null

            when (val result = repository.generateQrCode(classId, token)) {
                is Result.Success -> {
                    _qrCodeResponse.value = result.data
                }
                is Result.Error -> {
                    _qrCodeError.value = result.message
                }
                else -> {}
            }
            _isGeneratingQr.value = false
        }
    }
}