package com.coolsharp.qrcode

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner

class QRActivity : ComponentActivity() {
    // 리퀘스트 퍼미션
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) { // 승인되지 않았으면
            // 토스트 메시지를 띄우고
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
            // 종료
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 리퀘스트 퍼미션(카메라)
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        setContent {
            MaterialTheme {
                QRCodeScannerWithBottomSheet() // QRCodeScanner와 BottomSheet을 설정
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeScannerWithBottomSheet() {
    val context = LocalContext.current // 로컬 컨텍스트
    val lifecycleOwner = LocalLifecycleOwner.current // 라이프 사이클 오너
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager // 클립보드 매니저

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var scannedCode by remember { mutableStateOf<String?>(null) }
    var showSheet by remember { mutableStateOf(false) }
    // showSheet가 true이고 scannedCode가 null이 아니면 BottomSheet을 표시합니다.
    if (showSheet && scannedCode != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false // showSheet를 false로 변경처리함.
                scannedCode = null // scannedCode를 null로 변경처리함.
            }, sheetState = sheetState
        ) {
            BottomSheetContent(
                scannedCode = scannedCode.orEmpty(), // 스캔된 텍스트 대입
                onCopy = { // 카피 버튼 클릭 시
                    // 클립보드 매니저의 클립보드 저장
                    clipboardManager.setPrimaryClip(
                        ClipData.newPlainText("QR Code", scannedCode)
                    )
                    // Toast 메시지 표시
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                }, onShare = {
                    // 텍스트 엑스트라 인텐트 호출
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, scannedCode)
                    }
                    // 암시적 엑티비티 호출
                    context.startActivity(Intent.createChooser(intent, "Share QR Code"))
                }, onClose = {
                    // 시트 값 초기화
                    showSheet = false
                    scannedCode = null
                })
        }
    }
    AndroidView(factory = { context ->
        val previewView = PreviewView(context) // PreviewView 생성
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context) // ProcessCameraProvider 싱글톤
        cameraProviderFuture.addListener({ // 리스너 추가
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build().apply {
                    surfaceProvider = previewView.surfaceProvider
                }
            val analyzer = ImageAnalysis.Builder() // 이미지 분석 빌더 설정
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build().also {
                    it.setAnalyzer( // 이미지 분석기 설정
                        ContextCompat.getMainExecutor(context),
                        QRCodeAnalyzer { qrCode -> // QR 코드 콜백
                            if (!showSheet) { // 시트가 표시되지 않았다면
                                // 스캔된 코드 대입
                                scannedCode = qrCode
                                showSheet = true
                            }
                        })
                }
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analyzer
            )
        }, ContextCompat.getMainExecutor(context))
        previewView
    }, modifier = Modifier.fillMaxSize())
}

@Composable
fun BottomSheetContent(
    scannedCode: String, onCopy: () -> Unit, onShare: () -> Unit, onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth() // 넓이를 최대로
            .padding(16.dp), // 패딩은 16dp로 변경
        horizontalAlignment = Alignment.CenterHorizontally // 수평 정렬
    ) {
        Text("Scanned QR Code", style = MaterialTheme.typography.headlineSmall) // 텍스트 스타일 지정
        Spacer(modifier = Modifier.height(8.dp)) // 간격 설정
        Text(
            scannedCode, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge
        ) // 텍스트 스타일 지정
        Spacer(modifier = Modifier.height(24.dp)) // 간격 설정

        // Row 생성
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { // 좌우 간격 설정
            // Copy Button 생성
            Button(onClick = onCopy) {
                // Copy Icon 설정
                Icon(Icons.Default.CopyAll, contentDescription = "Copy")
                // 간격 설정
                Spacer(modifier = Modifier.width(4.dp))
                // Text 지정
                Text("Copy")
            }
            // Share Button 생성
            Button(onClick = onShare) {
                // Share Icon 설정
                Icon(Icons.Default.Share, contentDescription = "Share")
                // 간격 설정
                Spacer(modifier = Modifier.width(4.dp))
                // Text 지정
                Text("Share")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Close Button 생성
        OutlinedButton(onClick = onClose) {
            // Close Icon 설정
            Icon(Icons.Default.Close, contentDescription = "Close")
            // 간격 설정
            Spacer(modifier = Modifier.width(4.dp))
            // Text 지정
            Text("Close")
        }
    }
}