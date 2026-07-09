package com.ezral.personalinventory.domain.model

import com.ezral.personalinventory.data.local.entity.ContainerType
import com.ezral.personalinventory.data.local.entity.ItemType

data class LocationPath(
    val houseName: String,
    val roomName: String,
    val containerName: String? = null,
) {
    fun format(): String = buildList {
        add(houseName)
        add(roomName)
        containerName?.let { add(it) }
    }.joinToString(" › ")
}

data class ItemDraft(
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val itemType: ItemType = ItemType.OTHER,
    val category: String = "",
    val brand: String = "",
    val quantity: Double = 1.0,
    val unit: String = "pcs",
    val isConsumable: Boolean = false,
    val expiryDateMillis: Long? = null,
    val barcode: String = "",
    val isFavorite: Boolean = false,
    val roomId: Long? = null,
    val containerId: Long? = null,
    val photoUris: List<String> = emptyList(),
)

data class ContainerDraft(
    val name: String = "",
    val type: ContainerType = ContainerType.CABINET,
    val description: String = "",
    val photoUri: String? = null,
)

data class RoomDraft(
    val name: String = "",
    val floorLabel: String = "",
    val photoUri: String? = null,
)

data class HouseDraft(
    val name: String = "",
    val address: String = "",
    val coverPhotoUri: String? = null,
)

val DefaultRoomTemplates = listOf(
    "Kitchen",
    "Bedroom",
    "Bathroom",
    "Garage",
    "Living Room",
    "Office",
)
