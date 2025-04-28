package com.attendance.attendancetracker.presentation.pages

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
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
//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import com.attendance.attendancetracker.R
import com.attendance.attendancetracker.ui.theme.Typography
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable
fun QRScannerScreen(
    courseName: String = "Cyber Security",
    teacherName: String = "Senayit Demisse",
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember { mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    ) }

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
                    style = Typography.titleLarge.copy(
                        color = Color(0xFF001E2F),
                        fontWeight = FontWeight.Bold
                    )
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

            // QR Scanner frame with camera preview
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                // Camera preview
                if (hasCameraPermission) {
                    CameraPreview(
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Camera permission required",
                            style = Typography.bodySmall,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Corner markers
                Box(
                    modifier = Modifier.fillMaxSize()
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Simply scan the QR code when you enter class to mark your attendance automatically!",
                style = Typography.bodyMedium.copy(color = Color(0xFF4A6572)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor = remember { Executors.newSingleThreadExecutor() }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = androidx.camera.core.Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview
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

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun QRScannerScreenPreview() {
    QRScannerScreen()
}