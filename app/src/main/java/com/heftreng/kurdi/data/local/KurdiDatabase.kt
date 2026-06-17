package com.heftreng.kurdi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [IlerlemeEntity::class], version = 1, exportSchema = false)
abstract class KurdiDatabase : RoomDatabase() {
    abstract fun ilerlemeDao(): IlerlemeDao

    companion object {
        @Volatile private var INSTANCE: KurdiDatabase? = null

        fun getInstance(context: Context): KurdiDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    KurdiDatabase::class.java,
                    "kurdi_db"
                ).build().also { INSTANCE = it }
            }
    }
}
