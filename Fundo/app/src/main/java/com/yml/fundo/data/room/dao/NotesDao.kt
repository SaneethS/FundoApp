package com.yml.fundo.data.room.dao

import androidx.room.*
import com.yml.fundo.data.room.entity.NotesEntity

@Dao
interface NotesDao {

    @Insert
    suspend fun addNewNoteToDB(note: NotesEntity): Long

    @Query("select * from notes where archived = 0")
    suspend fun getNewNoteFromDB(): List<NotesEntity>

    @Query("select * from notes where archived = 1")
    suspend fun getArchivedNoteFromDB(): List<NotesEntity>

    @Update
    suspend fun updateNewNoteInDB(note: NotesEntity)

    @Delete
    suspend fun deleteNoteFromDB(note: NotesEntity)

    @Query("Delete from notes")
    suspend fun deleteNoteTable()
}