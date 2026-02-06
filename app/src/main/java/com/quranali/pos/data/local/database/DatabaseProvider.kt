package com.quranali.pos.data.local.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    fun provide(context: Context): PosDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PosDatabase::class.java,
            "pos_database"
        ).build()
    }
}

