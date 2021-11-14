package com.yml.fundo.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "operation_table",
        foreignKeys = [ForeignKey(entity = NotesEntity::class,
                        parentColumns = arrayOf("nid"),
                        childColumns = arrayOf("nid"),
                        onDelete = ForeignKey.CASCADE

        )]
)
data class OperationEntity(
    @PrimaryKey var nid: Long,
    var opCode: Int
)
