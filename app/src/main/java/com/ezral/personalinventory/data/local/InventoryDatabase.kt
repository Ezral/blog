package com.ezral.personalinventory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.ezral.personalinventory.data.local.dao.ContainerDao
import com.ezral.personalinventory.data.local.dao.HouseDao
import com.ezral.personalinventory.data.local.dao.ItemDao
import com.ezral.personalinventory.data.local.dao.ItemPhotoDao
import com.ezral.personalinventory.data.local.dao.RecentItemDao
import com.ezral.personalinventory.data.local.dao.RoomDao
import com.ezral.personalinventory.data.local.entity.ContainerEntity
import com.ezral.personalinventory.data.local.entity.ContainerType
import com.ezral.personalinventory.data.local.entity.HouseEntity
import com.ezral.personalinventory.data.local.entity.ItemEntity
import com.ezral.personalinventory.data.local.entity.ItemPhotoEntity
import com.ezral.personalinventory.data.local.entity.ItemType
import com.ezral.personalinventory.data.local.entity.RecentItemEntity
import com.ezral.personalinventory.data.local.entity.RoomEntity

class InventoryConverters {
    @TypeConverter
    fun fromContainerType(value: ContainerType): String = value.name

    @TypeConverter
    fun toContainerType(value: String): ContainerType = ContainerType.valueOf(value)

    @TypeConverter
    fun fromItemType(value: ItemType): String = value.name

    @TypeConverter
    fun toItemType(value: String): ItemType = ItemType.valueOf(value)
}

@Database(
    entities = [
        HouseEntity::class,
        RoomEntity::class,
        ContainerEntity::class,
        ItemEntity::class,
        ItemPhotoEntity::class,
        RecentItemEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
@TypeConverters(InventoryConverters::class)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun houseDao(): HouseDao
    abstract fun roomDao(): RoomDao
    abstract fun containerDao(): ContainerDao
    abstract fun itemDao(): ItemDao
    abstract fun itemPhotoDao(): ItemPhotoDao
    abstract fun recentItemDao(): RecentItemDao
}
