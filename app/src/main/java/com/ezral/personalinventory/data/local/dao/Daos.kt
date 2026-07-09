package com.ezral.personalinventory.data.local.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import com.ezral.personalinventory.data.local.entity.ContainerEntity
import com.ezral.personalinventory.data.local.entity.HouseEntity
import com.ezral.personalinventory.data.local.entity.ItemEntity
import com.ezral.personalinventory.data.local.entity.ItemPhotoEntity
import com.ezral.personalinventory.data.local.entity.RecentItemEntity
import com.ezral.personalinventory.data.local.entity.RoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseDao {
    @Query("SELECT * FROM houses ORDER BY name ASC")
    fun observeAll(): Flow<List<HouseEntity>>

    @Query("SELECT * FROM houses WHERE id = :id")
    fun observeById(id: Long): Flow<HouseEntity?>

    @Query("SELECT COUNT(*) FROM houses")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(house: HouseEntity): Long

    @Update
    suspend fun update(house: HouseEntity)

    @Query("DELETE FROM houses WHERE id = :id")
    suspend fun delete(id: Long)
}

@Dao
interface RoomDao {
    @Query("SELECT * FROM rooms WHERE houseId = :houseId ORDER BY sortOrder, name ASC")
    fun observeByHouse(houseId: Long): Flow<List<RoomEntity>>

    @Query("SELECT * FROM rooms WHERE id = :id")
    fun observeById(id: Long): Flow<RoomEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(room: RoomEntity): Long

    @Update
    suspend fun update(room: RoomEntity)

    @Query("DELETE FROM rooms WHERE id = :id")
    suspend fun delete(id: Long)
}

@Dao
interface ContainerDao {
    @Query(
        """
        SELECT * FROM containers
        WHERE roomId = :roomId AND parentId IS NULL
        ORDER BY sortOrder, name ASC
        """,
    )
    fun observeTopLevelByRoom(roomId: Long): Flow<List<ContainerEntity>>

    @Query(
        """
        SELECT * FROM containers
        WHERE parentId = :parentId
        ORDER BY sortOrder, name ASC
        """,
    )
    fun observeChildren(parentId: Long): Flow<List<ContainerEntity>>

    @Query("SELECT * FROM containers WHERE id = :id")
    fun observeById(id: Long): Flow<ContainerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(container: ContainerEntity): Long

    @Update
    suspend fun update(container: ContainerEntity)

    @Query("DELETE FROM containers WHERE id = :id")
    suspend fun delete(id: Long)
}

data class ItemWithPhotos(
    @Embedded val item: ItemEntity,
    @Relation(parentColumn = "id", entityColumn = "itemId")
    val photos: List<ItemPhotoEntity>,
)

data class ItemLocationRow(
    val itemId: Long,
    val itemName: String,
    val houseName: String,
    val roomName: String,
    val containerName: String?,
    val isFavorite: Boolean,
    val primaryPhotoUri: String?,
)

@Dao
interface ItemDao {
    @Transaction
    @Query("SELECT * FROM items WHERE id = :id")
    fun observeWithPhotos(id: Long): Flow<ItemWithPhotos?>

    @Query("SELECT * FROM items WHERE id = :id")
    fun observeById(id: Long): Flow<ItemEntity?>

    @Query(
        """
        SELECT * FROM items
        WHERE roomId = :roomId AND containerId IS NULL
        ORDER BY name ASC
        """,
    )
    fun observeUnassignedInRoom(roomId: Long): Flow<List<ItemEntity>>

    @Query(
        """
        SELECT * FROM items
        WHERE containerId = :containerId
        ORDER BY name ASC
        """,
    )
    fun observeByContainer(containerId: Long): Flow<List<ItemEntity>>

    @Query(
        """
        SELECT * FROM items
        WHERE isFavorite = 1
        ORDER BY name ASC
        """,
    )
    fun observeFavorites(): Flow<List<ItemEntity>>

    @Query(
        """
        SELECT items.* FROM items
        WHERE name LIKE '%' || :query || '%'
           OR description LIKE '%' || :query || '%'
           OR brand LIKE '%' || :query || '%'
           OR category LIKE '%' || :query || '%'
        ORDER BY name ASC
        """,
    )
    fun search(query: String): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getById(id: Long): ItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity): Long

    @Update
    suspend fun update(item: ItemEntity)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun delete(id: Long)

    @Query(
        """
        SELECT
            i.id AS itemId,
            i.name AS itemName,
            h.name AS houseName,
            r.name AS roomName,
            c.name AS containerName,
            i.isFavorite AS isFavorite,
            (
                SELECT uri FROM item_photos
                WHERE itemId = i.id AND isPrimary = 1
                LIMIT 1
            ) AS primaryPhotoUri
        FROM items i
        INNER JOIN rooms r ON r.id = COALESCE(
            (SELECT roomId FROM containers WHERE id = i.containerId),
            i.roomId
        )
        INNER JOIN houses h ON h.id = r.houseId
        LEFT JOIN containers c ON c.id = i.containerId
        WHERE i.id = :itemId
        """,
    )
    suspend fun getLocationRow(itemId: Long): ItemLocationRow?

    @Query(
        """
        SELECT
            i.id AS itemId,
            i.name AS itemName,
            h.name AS houseName,
            r.name AS roomName,
            c.name AS containerName,
            i.isFavorite AS isFavorite,
            (
                SELECT uri FROM item_photos
                WHERE itemId = i.id AND isPrimary = 1
                LIMIT 1
            ) AS primaryPhotoUri
        FROM items i
        INNER JOIN rooms r ON r.id = COALESCE(
            (SELECT roomId FROM containers WHERE id = i.containerId),
            i.roomId
        )
        INNER JOIN houses h ON h.id = r.houseId
        LEFT JOIN containers c ON c.id = i.containerId
        WHERE i.name LIKE '%' || :query || '%'
           OR i.description LIKE '%' || :query || '%'
           OR i.brand LIKE '%' || :query || '%'
        ORDER BY i.name ASC
        """,
    )
    fun searchWithLocation(query: String): Flow<List<ItemLocationRow>>
}

@Dao
interface ItemPhotoDao {
    @Query("SELECT * FROM item_photos WHERE itemId = :itemId ORDER BY sortOrder, id ASC")
    fun observeByItem(itemId: Long): Flow<List<ItemPhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: ItemPhotoEntity): Long

    @Query("DELETE FROM item_photos WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM item_photos WHERE itemId = :itemId")
    suspend fun deleteAllForItem(itemId: Long)
}

@Dao
interface RecentItemDao {
    @Query(
        """
        SELECT items.* FROM items
        INNER JOIN recent_items ON recent_items.itemId = items.id
        ORDER BY recent_items.viewedAt DESC
        LIMIT :limit
        """,
    )
    fun observeRecent(limit: Int = 10): Flow<List<ItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(recent: RecentItemEntity)
}
