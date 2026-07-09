package com.ezral.personalinventory.ui.item

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.ezral.personalinventory.data.local.dao.ItemWithPhotos
import com.ezral.personalinventory.data.local.entity.ItemEntity
import com.ezral.personalinventory.data.repository.ItemRepository
import com.ezral.personalinventory.domain.model.ItemDraft
import com.ezral.personalinventory.domain.model.LocationPath
import com.ezral.personalinventory.ui.components.InventoryScaffold
import com.ezral.personalinventory.ui.components.LocationBreadcrumb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
) : ViewModel() {
    private val _locationPath = MutableStateFlow<LocationPath?>(null)
    val locationPath: StateFlow<LocationPath?> = _locationPath.asStateFlow()

    fun observeItem(itemId: Long) = itemRepository.observeItemWithPhotos(itemId)

    fun loadLocation(itemId: Long) {
        viewModelScope.launch {
            _locationPath.value = itemRepository.getLocationPath(itemId)
            itemRepository.markViewed(itemId)
        }
    }

    fun toggleFavorite(item: ItemEntity) {
        viewModelScope.launch { itemRepository.toggleFavorite(item) }
    }

    fun deleteItem(itemId: Long, onDone: () -> Unit) {
        viewModelScope.launch {
            itemRepository.deleteItem(itemId)
            onDone()
        }
    }
}

@Composable
fun ItemDetailScreen(
    itemId: Long,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    viewModel: ItemDetailViewModel = hiltViewModel(),
) {
    val itemWithPhotos by viewModel.observeItem(itemId).collectAsStateWithLifecycle(initialValue = null)
    val locationPath by viewModel.locationPath.collectAsStateWithLifecycle()

    LaunchedEffect(itemId) { viewModel.loadLocation(itemId) }

    val item = itemWithPhotos?.item

    InventoryScaffold(
        title = item?.name ?: "Item",
        onBack = onBack,
    ) { padding ->
        if (item == null) {
            Text("Loading…", modifier = Modifier.padding(padding).padding(16.dp))
            return@InventoryScaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            locationPath?.let { LocationBreadcrumb(it.format()) }

            if (itemWithPhotos?.photos?.isNotEmpty() == true) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(itemWithPhotos!!.photos, key = { it.id }) { photo ->
                        AsyncImage(
                            model = photo.uri,
                            contentDescription = null,
                            modifier = Modifier
                                .height(160.dp)
                                .fillMaxWidth(0.6f),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }

            RowActions(
                isFavorite = item.isFavorite,
                onToggleFavorite = { viewModel.toggleFavorite(item) },
                onEdit = onEdit,
                onDelete = { viewModel.deleteItem(itemId, onBack) },
            )

            item.description?.let { Text(it) }
            item.brand?.let { Text("Brand: $it") }
            item.category?.let { Text("Category: $it") }
            Text("Quantity: ${item.quantity} ${item.unit}")
            if (item.isConsumable) {
                Text("Consumable", color = MaterialTheme.colorScheme.primary)
            }
            item.barcode?.let { Text("Barcode: $it") }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RowActions(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AssistChip(
            onClick = onToggleFavorite,
            label = { Text(if (isFavorite) "Favorited" else "Favorite") },
            leadingIcon = {
                Icon(
                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                )
            },
        )
        AssistChip(onClick = onEdit, label = { Text("Edit") })
        AssistChip(
            onClick = onDelete,
            label = { Text("Delete") },
            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) },
        )
    }
}

@HiltViewModel
class AddEditItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
) : ViewModel() {
    fun observeItem(itemId: Long) = itemRepository.observeItemWithPhotos(itemId)

    fun save(draft: ItemDraft, onSaved: (Long) -> Unit) {
        viewModelScope.launch {
            val id = itemRepository.saveItem(draft)
            onSaved(id)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditItemScreen(
    roomId: Long?,
    containerId: Long?,
    itemId: Long?,
    onBack: () -> Unit,
    onSaved: (Long) -> Unit,
    viewModel: AddEditItemViewModel = hiltViewModel(),
) {
    val safeItemId = itemId?.takeIf { it > 0 } ?: -1L
    val existing by viewModel.observeItem(safeItemId).collectAsStateWithLifecycle(initialValue = null)

    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var brand by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("1") }
    var unit by rememberSaveable { mutableStateOf("pcs") }
    var isConsumable by rememberSaveable { mutableStateOf(false) }
    var photoUris by rememberSaveable { mutableStateOf(listOf<String>()) }

    LaunchedEffect(existing, itemId) {
        if (itemId == null || itemId <= 0) return@LaunchedEffect
        existing?.let { data ->
            name = data.item.name
            description = data.item.description.orEmpty()
            brand = data.item.brand.orEmpty()
            category = data.item.category.orEmpty()
            quantity = data.item.quantity.toString()
            unit = data.item.unit
            isConsumable = data.item.isConsumable
            photoUris = data.photos.map { it.uri }
        }
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->
        uri?.let { photoUris = photoUris + it.toString() }
    }

    val effectiveRoomId = existing?.item?.roomId ?: roomId?.takeIf { it > 0 }
    val effectiveContainerId = existing?.item?.containerId ?: containerId?.takeIf { it > 0 }

    InventoryScaffold(
        title = if (itemId != null && itemId > 0) "Edit item" else "Add item",
        onBack = onBack,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Brand") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Qty") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unit") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("Consumable")
                Switch(checked = isConsumable, onCheckedChange = { isConsumable = it })
            }

            AssistChip(
                onClick = {
                    photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                label = { Text("Add photo") },
                leadingIcon = { Icon(Icons.Default.Photo, contentDescription = null) },
            )

            if (photoUris.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(photoUris, key = { it }) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier.height(96.dp),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val draft = ItemDraft(
                        id = itemId?.takeIf { it > 0 } ?: 0L,
                        name = name,
                        description = description,
                        brand = brand,
                        category = category,
                        quantity = quantity.toDoubleOrNull() ?: 1.0,
                        unit = unit,
                        isConsumable = isConsumable,
                        roomId = effectiveRoomId,
                        containerId = effectiveContainerId,
                        photoUris = photoUris,
                    )
                    viewModel.save(draft, onSaved)
                },
                enabled = name.isNotBlank() && (effectiveRoomId != null || effectiveContainerId != null),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Save")
            }
        }
    }
}
