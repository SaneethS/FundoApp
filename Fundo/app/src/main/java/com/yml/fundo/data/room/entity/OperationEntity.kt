package com.yml.fundo.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "operation_table")
data class OperationEntity(
    @PrimaryKey val fNid: String,
    val opCode: Int
)
