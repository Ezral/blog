package com.ezral.personalinventory.di

import android.content.Context
import androidx.room.Room
import com.ezral.personalinventory.data.local.InventoryDatabase
import com.ezral.personalinventory.data.local.MIGRATION_1_2
import com.ezral.personalinventory.data.local.MIGRATION_2_3
import com.ezral.personalinventory.data.local.dao.ContainerDao
import com.ezral.personalinventory.data.local.dao.HouseDao
import com.ezral.personalinventory.data.local.dao.ItemDao
import com.ezral.personalinventory.data.local.dao.ItemPhotoDao
import com.ezral.personalinventory.data.local.dao.RecentItemDao
import com.ezral.personalinventory.data.local.dao.RoomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): InventoryDatabase {
        return Room.databaseBuilder(
            context,
            InventoryDatabase::class.java,
            "personal_inventory.db",
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides fun provideHouseDao(db: InventoryDatabase): HouseDao = db.houseDao()
    @Provides fun provideRoomDao(db: InventoryDatabase): RoomDao = db.roomDao()
    @Provides fun provideContainerDao(db: InventoryDatabase): ContainerDao = db.containerDao()
    @Provides fun provideItemDao(db: InventoryDatabase): ItemDao = db.itemDao()
    @Provides fun provideItemPhotoDao(db: InventoryDatabase): ItemPhotoDao = db.itemPhotoDao()
    @Provides fun provideRecentItemDao(db: InventoryDatabase): RecentItemDao = db.recentItemDao()
}
