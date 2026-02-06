package com.quranali.pos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.quranali.pos.data.local.database.PosDao
import com.quranali.pos.data.local.entity.CategoryEntity
import com.quranali.pos.data.local.entity.ProductEntity

@Database(
    entities = [
        CategoryEntity::class,
        ProductEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PosDatabase : RoomDatabase() {
    abstract fun posDao(): PosDao
}