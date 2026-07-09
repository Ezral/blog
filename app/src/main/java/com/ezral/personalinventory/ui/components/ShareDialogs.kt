package com.ezral.personalinventory.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.ezral.personalinventory.data.local.entity.ContainerType
import com.ezral.personalinventory.ui.components.SinglePhotoPickerField

fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
}

@Composable
fun EditHouseDialog(
    initialName: String,
    initialAddress: String = "",
    initialPhotoUri: String? = null,
    onDismiss: () -> Unit,
    onSave: (name: String, address: String, photoUri: String?) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var address by rememberSaveable { mutableStateOf(initialAddress) }
    var photoUri by rememberSaveable { mutableStateOf(initialPhotoUri) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit house") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address (optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                SinglePhotoPickerField(
                    photoUri = photoUri,
                    onPhotoUriChange = { photoUri = it },
                    label = "House photo",
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(name, address, photoUri) }, enabled = name.isNotBlank()) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Composable
fun EditRoomDialog(
    initialName: String,
    initialFloor: String = "",
    initialPhotoUri: String? = null,
    onDismiss: () -> Unit,
    onSave: (name: String, floorLabel: String, photoUri: String?) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var floor by rememberSaveable { mutableStateOf(initialFloor) }
    var photoUri by rememberSaveable { mutableStateOf(initialPhotoUri) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit room") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Room name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = floor,
                    onValueChange = { floor = it },
                    label = { Text("Floor (optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                SinglePhotoPickerField(
                    photoUri = photoUri,
                    onPhotoUriChange = { photoUri = it },
                    label = "Room photo",
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(name, floor, photoUri) }, enabled = name.isNotBlank()) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Composable
fun EditContainerDialog(
    initialName: String,
    initialType: ContainerType = ContainerType.OTHER,
    initialDescription: String = "",
    initialPhotoUri: String? = null,
    onDismiss: () -> Unit,
    onSave: (name: String, type: ContainerType, description: String, photoUri: String?) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var type by rememberSaveable { mutableStateOf(initialType) }
    var description by rememberSaveable { mutableStateOf(initialDescription) }
    var photoUri by rememberSaveable { mutableStateOf(initialPhotoUri) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit container") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = type.name.lowercase().replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Type") },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                )
                SinglePhotoPickerField(
                    photoUri = photoUri,
                    onPhotoUriChange = { photoUri = it },
                    label = "Container photo",
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(name, type, description, photoUri) }, enabled = name.isNotBlank()) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Composable
fun ShareInviteDialog(
    title: String,
    inviteCode: String,
    uuidLabel: String,
    uuid: String,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Share this code with another Homeventory install. It links by stable UUID.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(uuidLabel, style = MaterialTheme.typography.labelMedium)
                Text(uuid, fontFamily = FontFamily.Monospace)
                Text("Invite code", style = MaterialTheme.typography.labelMedium)
                Text(
                    inviteCode,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall,
                )
                Button(
                    onClick = { copyToClipboard(context, "Homeventory invite", inviteCode) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Copy invite code")
                }
                Button(
                    onClick = { copyToClipboard(context, "Homeventory UUID", uuid) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Copy UUID only")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Done") }
        },
    )
}

@Composable
fun ImportInviteDialog(
    onDismiss: () -> Unit,
    onImport: (String) -> Unit,
) {
    var code by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Connect house or room") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Paste an invite code from another phone. Rooms include their house UUID so they link correctly.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Invite code") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onImport(code.trim()) },
                enabled = code.isNotBlank(),
            ) { Text("Connect") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Composable
fun LinkRoomToHouseDialog(
    onDismiss: () -> Unit,
    onLink: (houseUuid: String) -> Unit,
) {
    var houseUuid by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Link room to house") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Paste the house UUID to attach this room on this device.")
                OutlinedTextField(
                    value = houseUuid,
                    onValueChange = { houseUuid = it },
                    label = { Text("House UUID") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onLink(houseUuid.trim()) },
                enabled = houseUuid.isNotBlank(),
            ) { Text("Link") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
