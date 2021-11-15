package com.yml.fundo.data.room.dao

import androidx.room.*
import com.yml.fundo.data.room.entity.NotesEntity

@Dao
interface NotesDao {

    @Insert
    suspend fun addNewNoteToDB(note: NotesEntity): Long

    @Query("select * from notes")
    suspend fun getNewNoteFromDB(): List<NotesEntity>

    @Update
    suspend fun updateNewNoteInDB(note: NotesEntity)

    @Delete
    suspend fun deleteNoteFromDB(note: NotesEntity)

    @Query("Delete from notes")
    suspend fun deleteNoteTable()
}