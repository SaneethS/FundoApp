package com.yml.fundo.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yml.fundo.data.room.entity.OperationEntity

@Dao
interface OperationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOp(opEntity: OperationEntity):Long

    @Query("SELECT * from operation_table where fNid = :fNid")
    suspend fun getOpCode(fNid: String):OperationEntity

    @Query("DELETE from operation_table")
    suspend fun deleteOp()
}