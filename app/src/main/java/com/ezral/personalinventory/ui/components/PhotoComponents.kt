package com.ezral.personalinventory.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PhotoThumbnail(
    photoUri: String?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    contentDescription: String? = null,
    fallback: @Composable () -> Unit = {},
) {
    if (photoUri.isNullOrBlank()) {
        fallback()
    } else {
        AsyncImage(
            model = photoUri,
            contentDescription = contentDescription,
            modifier = modifier
                .size(size)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun SinglePhotoPickerField(
    photoUri: String?,
    onPhotoUriChange: (String?) -> Unit,
    label: String = "Photo",
    previewHeight: Dp = 120.dp,
) {
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->
        onPhotoUriChange(uri?.toString())
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        if (!photoUri.isNullOrBlank()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AsyncImage(
                    model = photoUri,
                    contentDescription = null,
                    modifier = Modifier
                        .height(previewHeight)
                        .fillMaxWidth(0.6f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                )
                IconButton(onClick = { onPhotoUriChange(null) }) {
                    Icon(Icons.Default.Close, contentDescription = "Remove photo")
                }
            }
        }
        AssistChip(
            onClick = {
                photoPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            },
            label = { Text(if (photoUri.isNullOrBlank()) "Add photo" else "Change photo") },
            leadingIcon = { Icon(Icons.Default.Photo, contentDescription = null) },
        )
    }
}
