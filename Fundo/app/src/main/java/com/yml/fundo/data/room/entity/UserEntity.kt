package com.yml.fundo.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0L,
    val fid: String = "",
    val name: String,
    val email: String,
    val mobile: String
)


