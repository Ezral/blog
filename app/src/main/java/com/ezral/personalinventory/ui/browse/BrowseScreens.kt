package com.ezral.personalinventory.ui.browse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.ezral.personalinventory.data.local.entity.ContainerEntity
import com.ezral.personalinventory.data.local.entity.ContainerType
import com.ezral.personalinventory.data.local.entity.HouseEntity
import com.ezral.personalinventory.data.local.entity.ItemEntity
import com.ezral.personalinventory.data.local.entity.RoomEntity
import com.ezral.personalinventory.data.repository.ContainerRepository
import com.ezral.personalinventory.data.repository.HouseRepository
import com.ezral.personalinventory.data.repository.ItemRepository
import com.ezral.personalinventory.data.repository.RoomRepository
import com.ezral.personalinventory.ui.components.EmptyState
import com.ezral.personalinventory.ui.components.InventoryScaffold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HousesViewModel @Inject constructor(
    private val houseRepository: HouseRepository,
) : ViewModel() {
    val houses: StateFlow<List<HouseEntity>> = houseRepository.observeHouses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addHouse(name: String) {
        viewModelScope.launch { houseRepository.createHouse(name) }
    }
}

@Composable
fun HousesScreen(
    onOpenHouse: (Long) -> Unit,
    viewModel: HousesViewModel = hiltViewModel(),
) {
    val houses by viewModel.houses.collectAsStateWithLifecycle()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var houseName by rememberSaveable { mutableStateOf("") }

    InventoryScaffold(
        title = "Houses",
        floatingAction = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add house")
            }
        },
    ) { padding ->
        if (houses.isEmpty()) {
            EmptyState(
                message = "Add your first house to start organizing inventory.",
                actionLabel = "Add house",
                onAction = { showDialog = true },
                modifier = Modifier.padding(padding),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(houses, key = { it.id }) { house ->
                    ListItem(
                        headlineContent = { Text(house.name) },
                        supportingContent = { house.address?.let { Text(it) } },
                        leadingContent = { Icon(Icons.Default.Home, contentDescription = null) },
                        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenHouse(house.id) },
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("New house") },
            text = {
                OutlinedTextField(
                    value = houseName,
                    onValueChange = { houseName = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addHouse(houseName)
                        houseName = ""
                        showDialog = false
                    },
                    enabled = houseName.isNotBlank(),
                ) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            },
        )
    }
}

@HiltViewModel
class HouseDetailViewModel @Inject constructor(
    private val houseRepository: HouseRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {
    fun observeHouse(id: Long) = houseRepository.observeHouse(id)
    fun observeRooms(houseId: Long) = roomRepository.observeRooms(houseId)

    fun addRoom(houseId: Long, name: String) {
        viewModelScope.launch { roomRepository.createRoom(houseId, name) }
    }
}

@Composable
fun HouseDetailScreen(
    houseId: Long,
    onBack: () -> Unit,
    onOpenRoom: (Long) -> Unit,
    viewModel: HouseDetailViewModel = hiltViewModel(),
) {
    val house by viewModel.observeHouse(houseId).collectAsStateWithLifecycle(initialValue = null)
    val rooms by viewModel.observeRooms(houseId).collectAsStateWithLifecycle(initialValue = emptyList())
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var roomName by rememberSaveable { mutableStateOf("") }

    InventoryScaffold(
        title = house?.name ?: "House",
        onBack = onBack,
        floatingAction = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add room")
            }
        },
    ) { padding ->
        if (rooms.isEmpty()) {
            EmptyState(
                message = "Add rooms like Kitchen, Bedroom, or Garage.",
                actionLabel = "Add room",
                onAction = { showDialog = true },
                modifier = Modifier.padding(padding),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(rooms, key = { it.id }) { room ->
                    ListItem(
                        headlineContent = { Text(room.name) },
                        supportingContent = { room.floorLabel?.let { Text(it) } },
                        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenRoom(room.id) },
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("New room") },
            text = {
                OutlinedTextField(
                    value = roomName,
                    onValueChange = { roomName = it },
                    label = { Text("Room name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addRoom(houseId, roomName)
                        roomName = ""
                        showDialog = false
                    },
                    enabled = roomName.isNotBlank(),
                ) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            },
        )
    }
}

@HiltViewModel
class RoomDetailViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val containerRepository: ContainerRepository,
    private val itemRepository: ItemRepository,
) : ViewModel() {
    fun observeRoom(id: Long) = roomRepository.observeRoom(id)
    fun observeContainers(roomId: Long) = containerRepository.observeTopLevel(roomId)
    fun observeUnassignedItems(roomId: Long) = itemRepository.observeUnassignedInRoom(roomId)

    fun addContainer(roomId: Long, name: String) {
        viewModelScope.launch {
            containerRepository.createContainer(roomId, name, ContainerType.CABINET)
        }
    }
}

@Composable
fun RoomDetailScreen(
    roomId: Long,
    onBack: () -> Unit,
    onOpenContainer: (Long) -> Unit,
    onOpenItem: (Long) -> Unit,
    onAddItem: () -> Unit,
    viewModel: RoomDetailViewModel = hiltViewModel(),
) {
    val room by viewModel.observeRoom(roomId).collectAsStateWithLifecycle(initialValue = null)
    val containers by viewModel.observeContainers(roomId).collectAsStateWithLifecycle(initialValue = emptyList())
    val items by viewModel.observeUnassignedItems(roomId).collectAsStateWithLifecycle(initialValue = emptyList())
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var containerName by rememberSaveable { mutableStateOf("") }

    InventoryScaffold(
        title = room?.name ?: "Room",
        onBack = onBack,
        floatingAction = {
            FloatingActionButton(onClick = onAddItem) {
                Icon(Icons.Default.Add, contentDescription = "Add item")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (containers.isNotEmpty()) {
                item { SectionHeader("Containers") }
                items(containers, key = { "c-${it.id}" }) { container ->
                    ContainerRow(container, onClick = { onOpenContainer(container.id) })
                }
            }
            if (items.isNotEmpty()) {
                item { SectionHeader("Items in room") }
                items(items, key = { "i-${it.id}" }) { item ->
                    ItemRow(item, onClick = { onOpenItem(item.id) })
                }
            }
            if (containers.isEmpty() && items.isEmpty()) {
                item {
                    EmptyState(
                        message = "Add a container or item to this room.",
                        actionLabel = "Add container",
                        onAction = { showDialog = true },
                    )
                }
            }
            item {
                TextButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.padding(8.dp),
                ) { Text("Add container") }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("New container") },
            text = {
                OutlinedTextField(
                    value = containerName,
                    onValueChange = { containerName = it },
                    label = { Text("Name (e.g. Pantry cabinet)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addContainer(roomId, containerName)
                        containerName = ""
                        showDialog = false
                    },
                    enabled = containerName.isNotBlank(),
                ) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            },
        )
    }
}

@HiltViewModel
class ContainerDetailViewModel @Inject constructor(
    private val containerRepository: ContainerRepository,
    private val itemRepository: ItemRepository,
) : ViewModel() {
    fun observeContainer(id: Long) = containerRepository.observeContainer(id)
    fun observeChildContainers(parentId: Long) = containerRepository.observeChildren(parentId)
    fun observeItems(containerId: Long) = itemRepository.observeByContainer(containerId)

    fun addNestedContainer(roomId: Long, parentId: Long, name: String) {
        viewModelScope.launch {
            containerRepository.createContainer(roomId, name, ContainerType.DRAWER, parentId)
        }
    }
}

@Composable
fun ContainerDetailScreen(
    containerId: Long,
    onBack: () -> Unit,
    onOpenContainer: (Long) -> Unit,
    onOpenItem: (Long) -> Unit,
    onAddItem: () -> Unit,
    viewModel: ContainerDetailViewModel = hiltViewModel(),
) {
    val container by viewModel.observeContainer(containerId).collectAsStateWithLifecycle(initialValue = null)
    val childContainers by viewModel.observeChildContainers(containerId)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val items by viewModel.observeItems(containerId).collectAsStateWithLifecycle(initialValue = emptyList())

    InventoryScaffold(
        title = container?.name ?: "Container",
        onBack = onBack,
        floatingAction = {
            FloatingActionButton(onClick = onAddItem) {
                Icon(Icons.Default.Add, contentDescription = "Add item")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            container?.description?.let { desc ->
                item {
                    Text(desc, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
            }
            if (childContainers.isNotEmpty()) {
                item { SectionHeader("Nested storage") }
                items(childContainers, key = { "c-${it.id}" }) { child ->
                    ContainerRow(child, onClick = { onOpenContainer(child.id) })
                }
            }
            if (items.isNotEmpty()) {
                item { SectionHeader("Items") }
                items(items, key = { "i-${it.id}" }) { item ->
                    ItemRow(item, onClick = { onOpenItem(item.id) })
                }
            }
            if (childContainers.isEmpty() && items.isEmpty()) {
                item {
                    EmptyState(
                        message = "This container is empty. Add items or nested drawers.",
                        actionLabel = "Add item",
                        onAction = onAddItem,
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun ContainerRow(container: ContainerEntity, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(container.name) },
        supportingContent = { Text(container.type.name.lowercase().replaceFirstChar { it.uppercase() }) },
        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    )
}

@Composable
private fun ItemRow(item: ItemEntity, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(item.name) },
        supportingContent = {
            Column {
                item.brand?.let { Text(it) }
                Text("Qty ${item.quantity} ${item.unit}")
            }
        },
        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    )
}
