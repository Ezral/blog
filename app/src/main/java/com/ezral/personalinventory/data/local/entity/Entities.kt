package com.ezral.personalinventory.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

fun newEntityUuid(): String = UUID.randomUUID().toString()

@Entity(
    tableName = "houses",
    indices = [Index("uuid", unique = true)],
)
data class HouseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uuid: String = newEntityUuid(),
    val name: String,
    val address: String? = null,
    val notes: String? = null,
    val coverPhotoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "rooms",
    foreignKeys = [
        ForeignKey(
            entity = HouseEntity::class,
            parentColumns = ["id"],
            childColumns = ["houseId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("houseId"), Index("uuid", unique = true), Index("houseUuid")],
)
data class RoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uuid: String = newEntityUuid(),
    val houseId: Long,
    val houseUuid: String,
    val name: String,
    val floorLabel: String? = null,
    val notes: String? = null,
    val photoUri: String? = null,
    val sortOrder: Int = 0,
)

enum class ContainerType {
    CABINET,
    DRAWER,
    SHELF,
    BOX,
    BIN,
    OTHER,
}

@Entity(
    tableName = "containers",
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ContainerEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("roomId"), Index("parentId")],
)
data class ContainerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val roomId: Long,
    val parentId: Long? = null,
    val name: String,
    val type: ContainerType = ContainerType.OTHER,
    val description: String? = null,
    val photoUri: String? = null,
    val sortOrder: Int = 0,
)

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = ContainerEntity::class,
            parentColumns = ["id"],
            childColumns = ["containerId"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("containerId"),
        Index("roomId"),
        Index("name"),
        Index("barcode"),
        Index("isFavorite"),
    ],
)
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val containerId: Long? = null,
    val roomId: Long? = null,
    val name: String,
    val description: String? = null,
    val category: String? = null,
    val isConsumable: Boolean = false,
    val quantity: Double = 1.0,
    val unit: String = "pcs",
    val minQuantity: Double? = null,
    val barcode: String? = null,
    val qrPayload: String? = null,
    val brand: String? = null,
    val isFavorite: Boolean = false,
    val currentExpiryDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "item_photos",
    foreignKeys = [
        ForeignKey(
            entity = ItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("itemId")],
)
data class ItemPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemId: Long,
    val uri: String,
    val isPrimary: Boolean = false,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "recent_items",
    primaryKeys = ["itemId"],
)
data class RecentItemEntity(
    val itemId: Long,
    val viewedAt: Long = System.currentTimeMillis(),
)
