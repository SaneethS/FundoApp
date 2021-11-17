package com.yml.fundo.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yml.fundo.data.room.entity.UserEntity
import kotlinx.coroutines.selects.select

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUserToDB(user:UserEntity) : Long

    @Query("select * from users where uid = :uid")
    suspend fun getUserFromDB(uid: Long): UserEntity
}