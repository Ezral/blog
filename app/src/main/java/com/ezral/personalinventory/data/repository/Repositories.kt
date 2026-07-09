package com.ezral.personalinventory.data.repository

import com.ezral.personalinventory.data.local.dao.ContainerDao
import com.ezral.personalinventory.data.local.dao.HouseDao
import com.ezral.personalinventory.data.local.dao.ItemDao
import com.ezral.personalinventory.data.local.dao.ItemLocationRow
import com.ezral.personalinventory.data.local.dao.ItemPhotoDao
import com.ezral.personalinventory.data.local.dao.ItemWithPhotos
import com.ezral.personalinventory.data.local.dao.RecentItemDao
import com.ezral.personalinventory.data.local.dao.RoomDao
import com.ezral.personalinventory.data.local.entity.ContainerEntity
import com.ezral.personalinventory.data.local.entity.HouseEntity
import com.ezral.personalinventory.data.local.entity.ItemEntity
import com.ezral.personalinventory.data.local.entity.ItemPhotoEntity
import com.ezral.personalinventory.data.local.entity.RecentItemEntity
import com.ezral.personalinventory.data.local.entity.newEntityUuid
import com.ezral.personalinventory.data.local.entity.RoomEntity
import com.ezral.personalinventory.domain.model.ItemDraft
import com.ezral.personalinventory.domain.model.LocationPath
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HouseRepository @Inject constructor(
    private val houseDao: HouseDao,
    private val roomDao: RoomDao,
) {
    fun observeHouses(): Flow<List<HouseEntity>> = houseDao.observeAll()

    fun observeHouse(id: Long): Flow<HouseEntity?> = houseDao.observeById(id)

    suspend fun hasAnyHouse(): Boolean = houseDao.count() > 0

    suspend fun createHouse(name: String, address: String? = null): Long {
        return houseDao.insert(
            HouseEntity(
                name = name,
                uuid = newEntityUuid(),
                address = address?.ifBlank { null },
            ),
        )
    }

    suspend fun createHouseWithRooms(name: String, roomNames: List<String>): Long {
        val houseId = createHouse(name)
        val house = houseDao.getById(houseId) ?: error("House not found after insert")
        roomNames.forEachIndexed { index, roomName ->
            roomDao.insert(
                RoomEntity(
                    uuid = newEntityUuid(),
                    houseId = houseId,
                    houseUuid = house.uuid,
                    name = roomName,
                    sortOrder = index,
                ),
            )
        }
        return houseId
    }

    suspend fun updateHouse(houseId: Long, name: String, address: String? = null, notes: String? = null) {
        val house = houseDao.getById(houseId) ?: return
        houseDao.update(
            house.copy(
                name = name.trim(),
                address = address?.ifBlank { null },
                notes = notes?.ifBlank { null },
                updatedAt = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun deleteHouse(id: Long) = houseDao.delete(id)
}

@Singleton
class RoomRepository @Inject constructor(
    private val roomDao: RoomDao,
    private val houseDao: HouseDao,
) {
    fun observeRooms(houseId: Long): Flow<List<RoomEntity>> = roomDao.observeByHouse(houseId)

    fun observeRoom(id: Long): Flow<RoomEntity?> = roomDao.observeById(id)

    suspend fun createRoom(houseId: Long, name: String, floorLabel: String? = null): Long {
        val house = houseDao.getById(houseId) ?: error("House not found")
        return roomDao.insert(
            RoomEntity(
                uuid = newEntityUuid(),
                houseId = houseId,
                houseUuid = house.uuid,
                name = name,
                floorLabel = floorLabel?.ifBlank { null },
            ),
        )
    }

    suspend fun updateRoom(
        roomId: Long,
        name: String,
        floorLabel: String? = null,
        notes: String? = null,
    ) {
        val room = roomDao.getById(roomId) ?: return
        roomDao.update(
            room.copy(
                name = name.trim(),
                floorLabel = floorLabel?.ifBlank { null },
                notes = notes?.ifBlank { null },
            ),
        )
    }

    suspend fun getHouseForRoom(roomId: Long): HouseEntity? {
        val room = roomDao.getById(roomId) ?: return null
        return houseDao.getById(room.houseId)
    }

    suspend fun deleteRoom(id: Long) = roomDao.delete(id)
}

@Singleton
class ContainerRepository @Inject constructor(
    private val containerDao: ContainerDao,
) {
    fun observeTopLevel(roomId: Long): Flow<List<ContainerEntity>> =
        containerDao.observeTopLevelByRoom(roomId)

    fun observeChildren(parentId: Long): Flow<List<ContainerEntity>> =
        containerDao.observeChildren(parentId)

    fun observeContainer(id: Long): Flow<ContainerEntity?> = containerDao.observeById(id)

    suspend fun createContainer(
        roomId: Long,
        name: String,
        type: com.ezral.personalinventory.data.local.entity.ContainerType,
        parentId: Long? = null,
        description: String? = null,
    ): Long {
        return containerDao.insert(
            ContainerEntity(
                roomId = roomId,
                parentId = parentId,
                name = name,
                type = type,
                description = description,
            ),
        )
    }

    suspend fun deleteContainer(id: Long) = containerDao.delete(id)
}

@Singleton
class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val itemPhotoDao: ItemPhotoDao,
    private val recentItemDao: RecentItemDao,
) {
    fun observeItemWithPhotos(id: Long): Flow<ItemWithPhotos?> = itemDao.observeWithPhotos(id)

    fun observeUnassignedInRoom(roomId: Long): Flow<List<ItemEntity>> =
        itemDao.observeUnassignedInRoom(roomId)

    fun observeByContainer(containerId: Long): Flow<List<ItemEntity>> =
        itemDao.observeByContainer(containerId)

    fun observeFavorites(): Flow<List<ItemEntity>> = itemDao.observeFavorites()

    fun observeRecent(limit: Int = 10): Flow<List<ItemEntity>> = recentItemDao.observeRecent(limit)

    fun searchWithLocation(query: String): Flow<List<ItemLocationRow>> =
        itemDao.searchWithLocation(query.trim())

    suspend fun getLocationPath(itemId: Long): LocationPath? {
        val row = itemDao.getLocationRow(itemId) ?: return null
        return LocationPath(
            houseName = row.houseName,
            roomName = row.roomName,
            containerName = row.containerName,
        )
    }

    suspend fun markViewed(itemId: Long) {
        recentItemDao.upsert(RecentItemEntity(itemId = itemId))
    }

    suspend fun toggleFavorite(item: ItemEntity) {
        itemDao.update(item.copy(isFavorite = !item.isFavorite, updatedAt = System.currentTimeMillis()))
    }

    suspend fun saveItem(draft: ItemDraft): Long {
        require(draft.roomId != null || draft.containerId != null) {
            "Item must belong to a room or container"
        }
        require(draft.name.isNotBlank()) { "Item name is required" }

        val now = System.currentTimeMillis()
        val entity = ItemEntity(
            id = draft.id,
            containerId = draft.containerId,
            roomId = if (draft.containerId == null) draft.roomId else null,
            name = draft.name.trim(),
            description = draft.description.ifBlank { null },
            category = draft.category.ifBlank { null },
            brand = draft.brand.ifBlank { null },
            isConsumable = draft.isConsumable,
            quantity = draft.quantity,
            unit = draft.unit.ifBlank { "pcs" },
            isFavorite = draft.isFavorite,
            updatedAt = now,
            createdAt = if (draft.id == 0L) now else now,
        )

        val itemId = if (draft.id == 0L) {
            itemDao.insert(entity)
        } else {
            val existing = itemDao.getById(draft.id)
            itemDao.update(
                entity.copy(createdAt = existing?.createdAt ?: now),
            )
            draft.id
        }

        if (draft.photoUris.isNotEmpty()) {
            itemPhotoDao.deleteAllForItem(itemId)
            draft.photoUris.forEachIndexed { index, uri ->
                itemPhotoDao.insert(
                    ItemPhotoEntity(
                        itemId = itemId,
                        uri = uri,
                        isPrimary = index == 0,
                        sortOrder = index,
                    ),
                )
            }
        }

        return itemId
    }

    suspend fun moveItem(item: ItemEntity, roomId: Long?, containerId: Long?) {
        require(roomId != null || containerId != null)
        itemDao.update(
            item.copy(
                roomId = if (containerId == null) roomId else null,
                containerId = containerId,
                updatedAt = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun deleteItem(id: Long) = itemDao.delete(id)
}
