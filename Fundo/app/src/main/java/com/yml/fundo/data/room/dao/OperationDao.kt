package com.yml.fundo.data.room.dao

import androidx.room.Insert
import androidx.room.Query
import com.yml.fundo.data.room.entity.OperationEntity

interface OperationDao {
    @Insert
    suspend fun addOp(opEntity: OperationEntity):Long

    @Query("SELECT * from operation_table")
    suspend fun getOps():List<OperationEntity>

    @Query("DELETE from operation_table")
    suspend fun deleteOp()
}