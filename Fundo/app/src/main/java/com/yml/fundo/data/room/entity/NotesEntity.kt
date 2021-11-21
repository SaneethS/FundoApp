package com.yml.fundo.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class NotesEntity(
    @PrimaryKey(autoGenerate = true) val nid: Long = 0L,
    val fNid: String = "",
    val title: String,
    val content: String,
    val dateModified: Date?,
    var archived: Boolean
)
