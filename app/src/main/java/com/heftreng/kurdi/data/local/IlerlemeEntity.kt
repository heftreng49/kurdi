package com.heftreng.kurdi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ilerleme")
data class IlerlemeEntity(
    @PrimaryKey val unitId: String,
    val tamamlandiMi: Boolean = false,
    val puan: Int = 0,
    val yildiz: Int = 0,           // 0-3
    val sonGuncelleme: Long = System.currentTimeMillis()
)
