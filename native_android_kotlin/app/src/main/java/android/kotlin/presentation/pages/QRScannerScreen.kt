package com.attendance.attendancetracker.presentation.pages

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.livedata.observeAsState
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.data.models.AttendanceScanResponse
import com.attendance.attendancetracker.presentation.viewmodels.AttendanceViewModel
import com.attendance.attendancetracker.ui.theme.Typography
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import org.json.JSONObject
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable
fun QRScannerScreen(
    courseName: String = "Cyber Security",
    teacherName: String = "Senayit Demisse",
    onBackClick: () -> Unit = {},
    classId: String = "",
    viewModel: AttendanceViewModel = hiltViewModel(),
    authToken: String = ""
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember { mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    ) }
    
    var isQrCodeDetected by remember { mutableStateOf(false) }
    var scanMessage by remember { mutableStateOf("") }
    var scanSuccess by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }
    
    // Observe LiveData from ViewModel
    val scanResult by viewModel.scanResult.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState(null)
    
    // Process scan results and errors
    LaunchedEffect(scanResult) {
        scanResult?.let {
            scanSuccess = it.success
            scanMessage = it.message
            isQrCodeDetected = true
        }
    }
    
    LaunchedEffect(error) {
        error?.let { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                scanSuccess = false
                scanMessage = errorMessage
                isQrCodeDetected = true
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF001E2F))
                .padding(30.dp)
        ) {
            // Logo
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "SCANIN Logo",
                    modifier = Modifier.size(50.dp).padding(top = 20.dp)
                )
            }
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Course title with back button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_ios_back_svgrepo_com),
                        contentDescription = "Back",
                        tint = Color(0xFF001E2F)
                    )
                }
                
                Text(
                    text = courseName,
                    style = Typography.titleMedium.copy(color = Color(0xFF001E2F)),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            // Teacher name
            Text(
                text = "Teacher: $teacherName",
                style = Typography.bodyMedium.copy(color = Color(0xFF4A6572)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, bottom = 16.dp)
            )

            Text(
                text = "Scan QR Codes for Attendance",
                style = Typography.titleMedium.copy(
                    color = Color(0xFF001E2F),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (!isQrCodeDetected) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                ) {
                    // Camera preview that processes QR codes
                    if (hasCameraPermission) {
                        CameraPreview(
                            modifier = Modifier.fillMaxSize(),
                            onQrCodeScanned = { token, scannedClassId ->
                                Log.d("QRScannerScreen", "QR Code scanned - token: $token, classId: $scannedClassId, expected classId: $classId")
                                // Process the scanned QR code data
                                if (authToken.isNotEmpty()) {
                                    // Use the scanned classId if current one is empty
                                    val targetClassId = if (classId.isNotEmpty()) classId else scannedClassId
                                    Log.d("QRScannerScreen", "Calling scanAttendance with token: $token, classId: $targetClassId")
                                    viewModel.scanAttendance(token, targetClassId, authToken)
                                } else {
                                    Log.e("QRScannerScreen", "Authentication error - empty auth token")
                                    // Show an error if we don't have an auth token
                                    scanSuccess = false
                                    scanMessage = "Authentication error. Please login again."
                                    isQrCodeDetected = true
                                }
                            }
                        )
                    }
                    
                    // Frame corners
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        // Top-left corner
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.TopStart)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(20.dp)
                                    .background(Color(0xFF001E2F))
                                    .align(Alignment.TopStart)
                            )
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(4.dp)
                                    .background(Color(0xFF001E2F))
                                    .align(Alignment.TopStart)
                            )
                        }

                        // Top-right corner
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(20.dp)
                                    .background(Color(0xFF001E2F))
                                    .align(Alignment.TopEnd)
                            )
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(4.dp)
                                    .background(Color(0xFF001E2F))
                                    .align(Alignment.TopEnd)
                            )
                        }

                        // Bottom-left corner
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.BottomStart)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(20.dp)
                                    .background(Color(0xFF001E2F))
                                    .align(Alignment.BottomStart)
                            )
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(4.dp)
                                    .background(Color(0xFF001E2F))
                                    .align(Alignment.BottomStart)
                            )
                        }

                        // Bottom-right corner
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.BottomEnd)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(20.dp)
                                    .background(Color(0xFF001E2F))
                                    .align(Alignment.BottomEnd)
                            )
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(4.dp)
                                    .background(Color(0xFF001E2F))
                                    .align(Alignment.BottomEnd)
                            )
                        }
                    }
                    
                    // Loading indicator
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x88FFFFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF001E2F))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isQrCodeDetected) {
                // Show scan result
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            color = if (scanSuccess) Color(0xFF4CAF50) else Color(0xFFF44336),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = scanMessage,
                        style = Typography.bodyMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { isQrCodeDetected = false },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Scan Again")
                }
            } else {
                Text(
                    text = "Simply scan the QR code when you enter class to mark your attendance automatically!",
                    style = Typography.bodyMedium.copy(color = Color(0xFF4A6572)),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onQrCodeScanned: (String, String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor = remember { Executors.newSingleThreadExecutor() }
    
    // Create barcode scanner options
    val options = remember {
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    }
    
    // Create barcode scanner
    val scanner = remember { BarcodeScanning.getClient(options) }
    
    // Used to avoid multiple scans of the same QR code
    var lastScanned by remember { mutableStateOf("") }
    
    // Define image analyzer to process camera frames
    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply {
                setAnalyzer(executor, createQrCodeAnalyzer(scanner) { qrContent ->
                    if (qrContent != lastScanned) {
                        lastScanned = qrContent
                        try {
                            // Parse JSON from QR code
                            val jsonObject = JSONObject(qrContent)
                            val token = jsonObject.optString("token", "")
                            val classId = jsonObject.optString("classId", "")
                            
                            if (token.isNotEmpty() && classId.isNotEmpty()) {
                                Log.d("QRScanner", "QR Scanned: token=$token, classId=$classId")
                                onQrCodeScanned(token, classId)
                            } else {
                                Log.e("QRScanner", "Invalid QR code format: missing token or classId")
                            }
                        } catch (e: Exception) {
                            Log.e("QRScanner", "Error parsing QR code: ${e.message}")
                        }
                    }
                })
            }
    }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = modifier
    )
}

fun createQrCodeAnalyzer(
    scanner: BarcodeScanner,
    onQrCodeDetected: (String) -> Unit
): ImageAnalysis.Analyzer {
    return object : ImageAnalysis.Analyzer {
        @OptIn(ExperimentalGetImage::class)
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                
                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        if (barcodes.isNotEmpty()) {
                            barcodes.firstOrNull()?.rawValue?.let { rawValue ->
                                onQrCodeDetected(rawValue)
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("QRScanner", "Barcode scanning failed: ${exception.message}")
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun QRScannerScreenPreview() {
    QRScannerScreen(authToken = "sample_token")
}
