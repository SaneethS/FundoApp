package com.yml.fundo.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yml.fundo.data.room.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUserToDB(user:UserEntity) : Long

    @Query("select * from users where uid = :uid")
    fun getUserFromDB(uid: Long): UserEntity
}