package com.ezral.personalinventory.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewModelScope
import com.ezral.personalinventory.data.local.entity.ItemEntity
import com.ezral.personalinventory.data.repository.ItemRepository
import com.ezral.personalinventory.ui.components.InventoryScaffold
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
) : ViewModel() {
    fun lookupBarcode(barcode: String, onResult: (ItemEntity?) -> Unit) {
        viewModelScope.launch {
            onResult(itemRepository.findByBarcode(barcode))
        }
    }
}

@Composable
fun ScanScreen(
    onOpenItem: (Long) -> Unit,
    onAddItemWithBarcode: (String) -> Unit,
    viewModel: ScanViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    var statusMessage by rememberSaveable { mutableStateOf("Point camera at a barcode") }
    var manualBarcode by rememberSaveable { mutableStateOf("") }
    var isLookingUp by remember { mutableStateOf(false) }
    var lastScanned by remember { mutableStateOf("") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasCameraPermission = granted
        if (!granted) {
            statusMessage = "Camera permission denied. Enter barcode manually below."
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun lookupBarcode(barcode: String) {
        val trimmed = barcode.trim()
        if (trimmed.isBlank() || isLookingUp) return
        isLookingUp = true
        statusMessage = "Looking up $trimmed…"
        viewModel.lookupBarcode(trimmed) { item ->
            isLookingUp = false
            if (item != null) {
                statusMessage = "Found: ${item.name}"
                onOpenItem(item.id)
            } else {
                statusMessage = "No item for $trimmed. Tap Add item to create one."
                manualBarcode = trimmed
            }
        }
    }

    InventoryScaffold(title = "Scan") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(statusMessage, style = MaterialTheme.typography.bodyMedium)

            if (hasCameraPermission) {
                BarcodeCameraPreview(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onBarcode = { barcode ->
                        if (barcode != lastScanned) {
                            lastScanned = barcode
                            lookupBarcode(barcode)
                        }
                    },
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                        Text("Grant camera permission")
                    }
                }
            }

            OutlinedTextField(
                value = manualBarcode,
                onValueChange = { manualBarcode = it },
                label = { Text("Barcode") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Button(
                onClick = { lookupBarcode(manualBarcode) },
                enabled = manualBarcode.isNotBlank() && !isLookingUp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (isLookingUp) "Looking up…" else "Look up barcode")
            }

            Button(
                onClick = { onAddItemWithBarcode(manualBarcode.trim()) },
                enabled = manualBarcode.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Add item with this barcode")
            }
        }
    }
}

@Composable
private fun BarcodeCameraPreview(
    modifier: Modifier = Modifier,
    onBarcode: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember { Executors.newSingleThreadExecutor() }
    val scanner = remember {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_ALL_FORMATS,
            )
            .build()
        BarcodeScanning.getClient(options)
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PreviewView(ctx)
        },
        update = { previewView ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }
                val analysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(1280, 720))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                analysis.setAnalyzer(executor) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees,
                        )
                        scanner.process(image)
                            .addOnSuccessListener { barcodes ->
                                barcodes.firstOrNull { !it.rawValue.isNullOrBlank() }
                                    ?.rawValue
                                    ?.let(onBarcode)
                            }
                            .addOnCompleteListener { imageProxy.close() }
                    } else {
                        imageProxy.close()
                    }
                }
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        analysis,
                    )
                } catch (_: Exception) {
                }
            }, ContextCompat.getMainExecutor(context))
        },
    )
}
