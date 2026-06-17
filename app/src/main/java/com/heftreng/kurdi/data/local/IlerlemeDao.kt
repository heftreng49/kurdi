package com.heftreng.kurdi.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IlerlemeDao {
    @Query("SELECT * FROM ilerleme ORDER BY unitId ASC")
    fun hepsiniAl(): Flow<List<IlerlemeEntity>>

    @Query("SELECT * FROM ilerleme WHERE unitId = :id")
    suspend fun getir(id: String): IlerlemeEntity?

    @Upsert
    suspend fun kaydet(ilerleme: IlerlemeEntity)

    @Query("DELETE FROM ilerleme")
    suspend fun hepsiniSil()
}
