package com.ezral.personalinventory.ui.browse

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Share
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.ezral.personalinventory.data.repository.ShareConnectRepository
import com.ezral.personalinventory.domain.model.ImportInviteResult
import com.ezral.personalinventory.ui.components.EditHouseDialog
import com.ezral.personalinventory.ui.components.EditRoomDialog
import com.ezral.personalinventory.ui.components.EmptyState
import com.ezral.personalinventory.ui.components.ImportInviteDialog
import com.ezral.personalinventory.ui.components.LinkRoomToHouseDialog
import com.ezral.personalinventory.ui.components.ShareInviteDialog
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
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
    private val shareConnectRepository: ShareConnectRepository,
) : ViewModel() {
    val houses: StateFlow<List<HouseEntity>> = houseRepository.observeHouses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addHouse(name: String) {
        viewModelScope.launch { houseRepository.createHouse(name) }
    }

    fun importInvite(raw: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val message = when (val result = shareConnectRepository.importInvite(raw)) {
                is ImportInviteResult.HouseLinked -> "Connected house: ${result.houseName}"
                is ImportInviteResult.RoomLinked -> "Connected room: ${result.roomName}"
                is ImportInviteResult.Updated -> result.message
            }
            onResult(message)
        }
    }
}

@Composable
fun HousesScreen(
    onOpenHouse: (Long) -> Unit,
    viewModel: HousesViewModel = hiltViewModel(),
) {
    val houses by viewModel.houses.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var showImport by rememberSaveable { mutableStateOf(false) }
    var houseName by rememberSaveable { mutableStateOf("") }

    InventoryScaffold(
        title = "Houses",
        actions = {
            IconButton(onClick = { showImport = true }) {
                Icon(Icons.Default.Link, contentDescription = "Connect")
            }
        },
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

    if (showImport) {
        ImportInviteDialog(
            onDismiss = { showImport = false },
            onImport = { code ->
                viewModel.importInvite(code) { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
                showImport = false
            },
        )
    }
}

@HiltViewModel
class HouseDetailViewModel @Inject constructor(
    private val houseRepository: HouseRepository,
    private val roomRepository: RoomRepository,
    private val shareConnectRepository: ShareConnectRepository,
) : ViewModel() {
    fun observeHouse(id: Long) = houseRepository.observeHouse(id)
    fun observeRooms(houseId: Long) = roomRepository.observeRooms(houseId)

    fun addRoom(houseId: Long, name: String) {
        viewModelScope.launch { roomRepository.createRoom(houseId, name) }
    }

    fun updateHouse(houseId: Long, name: String, address: String) {
        viewModelScope.launch { houseRepository.updateHouse(houseId, name, address) }
    }

    fun updateRoom(roomId: Long, name: String, floorLabel: String) {
        viewModelScope.launch { roomRepository.updateRoom(roomId, name, floorLabel) }
    }

    fun houseInvite(house: HouseEntity): String = shareConnectRepository.houseInvite(house)

    fun roomInvite(room: RoomEntity, house: HouseEntity): String =
        shareConnectRepository.roomInvite(room, house)
}

@OptIn(ExperimentalFoundationApi::class)
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
    var showEditHouse by rememberSaveable { mutableStateOf(false) }
    var showShareHouse by rememberSaveable { mutableStateOf(false) }
    var editingRoom by rememberSaveable { mutableStateOf<RoomEntity?>(null) }
    var sharingRoom by rememberSaveable { mutableStateOf<RoomEntity?>(null) }
    var roomName by rememberSaveable { mutableStateOf("") }

    InventoryScaffold(
        title = house?.name ?: "House",
        onBack = onBack,
        actions = {
            IconButton(onClick = { showEditHouse = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit house")
            }
            IconButton(onClick = { showShareHouse = true }, enabled = house != null) {
                Icon(Icons.Default.Share, contentDescription = "Share house")
            }
        },
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
                        supportingContent = {
                            Column {
                                room.floorLabel?.let { Text(it) }
                                Text(
                                    "UUID: ${room.uuid.take(8)}…",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        },
                        trailingContent = {
                            Row {
                                IconButton(onClick = { sharingRoom = room }) {
                                    Icon(Icons.Default.Share, contentDescription = "Share room")
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { onOpenRoom(room.id) },
                                onLongClick = { editingRoom = room },
                            ),
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

    val currentHouse = house
    if (showEditHouse && currentHouse != null) {
        EditHouseDialog(
            initialName = currentHouse.name,
            initialAddress = currentHouse.address.orEmpty(),
            onDismiss = { showEditHouse = false },
            onSave = { name, address ->
                viewModel.updateHouse(houseId, name, address)
                showEditHouse = false
            },
        )
    }

    if (showShareHouse && currentHouse != null) {
        ShareInviteDialog(
            title = "Share house",
            inviteCode = viewModel.houseInvite(currentHouse),
            uuidLabel = "House UUID",
            uuid = currentHouse.uuid,
            onDismiss = { showShareHouse = false },
        )
    }

    editingRoom?.let { room ->
        EditRoomDialog(
            initialName = room.name,
            initialFloor = room.floorLabel.orEmpty(),
            onDismiss = { editingRoom = null },
            onSave = { name, floor ->
                viewModel.updateRoom(room.id, name, floor)
                editingRoom = null
            },
        )
    }

    val shareRoom = sharingRoom
    if (shareRoom != null && currentHouse != null) {
        ShareInviteDialog(
            title = "Share room",
            inviteCode = viewModel.roomInvite(shareRoom, currentHouse),
            uuidLabel = "Room UUID",
            uuid = shareRoom.uuid,
            onDismiss = { sharingRoom = null },
        )
    }
}

@HiltViewModel
class RoomDetailViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val containerRepository: ContainerRepository,
    private val itemRepository: ItemRepository,
    private val shareConnectRepository: ShareConnectRepository,
) : ViewModel() {
    fun observeRoom(id: Long) = roomRepository.observeRoom(id)
    fun observeContainers(roomId: Long) = containerRepository.observeTopLevel(roomId)
    fun observeUnassignedItems(roomId: Long) = itemRepository.observeUnassignedInRoom(roomId)

    fun addContainer(roomId: Long, name: String) {
        viewModelScope.launch {
            containerRepository.createContainer(roomId, name, ContainerType.CABINET)
        }
    }

    suspend fun getHouseForRoom(roomId: Long) = roomRepository.getHouseForRoom(roomId)

    fun updateRoom(roomId: Long, name: String, floorLabel: String) {
        viewModelScope.launch { roomRepository.updateRoom(roomId, name, floorLabel) }
    }

    fun roomInvite(room: RoomEntity, house: HouseEntity): String =
        shareConnectRepository.roomInvite(room, house)

    fun linkRoomToHouse(roomId: Long, houseUuid: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            onResult(shareConnectRepository.linkRoomToHouseByUuid(roomId, houseUuid))
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
    val context = LocalContext.current
    val room by viewModel.observeRoom(roomId).collectAsStateWithLifecycle(initialValue = null)
    var house by remember { mutableStateOf<HouseEntity?>(null) }
    val containers by viewModel.observeContainers(roomId).collectAsStateWithLifecycle(initialValue = emptyList())
    val items by viewModel.observeUnassignedItems(roomId).collectAsStateWithLifecycle(initialValue = emptyList())
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var showEditRoom by rememberSaveable { mutableStateOf(false) }
    var showShareRoom by rememberSaveable { mutableStateOf(false) }
    var showLinkHouse by rememberSaveable { mutableStateOf(false) }
    var containerName by rememberSaveable { mutableStateOf("") }

    androidx.compose.runtime.LaunchedEffect(roomId, room?.houseUuid) {
        house = viewModel.getHouseForRoom(roomId)
    }

    val currentRoom = room
    val currentHouse = house

    InventoryScaffold(
        title = currentRoom?.name ?: "Room",
        onBack = onBack,
        actions = {
            IconButton(onClick = { showEditRoom = true }, enabled = currentRoom != null) {
                Icon(Icons.Default.Edit, contentDescription = "Edit room")
            }
            IconButton(onClick = { showShareRoom = true }, enabled = currentRoom != null && currentHouse != null) {
                Icon(Icons.Default.Share, contentDescription = "Share room")
            }
            IconButton(onClick = { showLinkHouse = true }, enabled = currentRoom != null) {
                Icon(Icons.Default.Link, contentDescription = "Link to house")
            }
        },
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
            currentRoom?.let { r ->
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Text("Room UUID: ${r.uuid}", style = MaterialTheme.typography.bodySmall)
                        Text("House UUID: ${r.houseUuid}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
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

    if (showEditRoom && currentRoom != null) {
        EditRoomDialog(
            initialName = currentRoom.name,
            initialFloor = currentRoom.floorLabel.orEmpty(),
            onDismiss = { showEditRoom = false },
            onSave = { name, floor ->
                viewModel.updateRoom(roomId, name, floor)
                showEditRoom = false
            },
        )
    }

    if (showShareRoom && currentRoom != null && currentHouse != null) {
        ShareInviteDialog(
            title = "Share room",
            inviteCode = viewModel.roomInvite(currentRoom, currentHouse),
            uuidLabel = "Room UUID",
            uuid = currentRoom.uuid,
            onDismiss = { showShareRoom = false },
        )
    }

    if (showLinkHouse) {
        LinkRoomToHouseDialog(
            onDismiss = { showLinkHouse = false },
            onLink = { houseUuid ->
                viewModel.linkRoomToHouse(roomId, houseUuid) { ok ->
                    val message = if (ok) "Room linked to house" else "House UUID not found on this device"
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
                showLinkHouse = false
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
