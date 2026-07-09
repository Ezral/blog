package com.ezral.personalinventory.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.UUID

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE houses ADD COLUMN uuid TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE rooms ADD COLUMN uuid TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE rooms ADD COLUMN houseUuid TEXT NOT NULL DEFAULT ''")

        db.query("SELECT id FROM houses").use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getLong(0)
                db.execSQL(
                    "UPDATE houses SET uuid = ? WHERE id = ?",
                    arrayOf(UUID.randomUUID().toString(), id),
                )
            }
        }

        db.query("SELECT rooms.id, houses.uuid FROM rooms INNER JOIN houses ON rooms.houseId = houses.id")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    val roomId = cursor.getLong(0)
                    val houseUuid = cursor.getString(1)
                    db.execSQL(
                        "UPDATE rooms SET uuid = ?, houseUuid = ? WHERE id = ?",
                        arrayOf(UUID.randomUUID().toString(), houseUuid, roomId),
                    )
                }
            }
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE items ADD COLUMN itemType TEXT NOT NULL DEFAULT 'OTHER'")
    }
}
