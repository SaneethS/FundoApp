package com.yml.fundo.data.room.dao

import androidx.room.*
import com.yml.fundo.data.room.entity.NotesEntity

@Dao
interface NotesDao {

    @Insert
    suspend fun addNewNoteToDB(note: NotesEntity): Long

    @Query("select * from notes where archived = 0 ")
    suspend fun getNewNoteFromDB(): List<NotesEntity>

    @Query("select * from notes where archived = 0 LIMIT :limit OFFSET :offset")
    suspend fun getPagedNote(limit: Int, offset: Int): List<NotesEntity>

    @Query("select * from notes where archived = 1 LIMIT :limit OFFSET :offset")
    suspend fun getArchivePaged(limit: Int, offset: Int): List<NotesEntity>

    @Query("select * from notes where NULLIF(reminder,'') IS NOT NULL LIMIT :limit OFFSET :offset")
    suspend fun getReminderPaged(limit: Int, offset: Int): List<NotesEntity>

    @Query("select count(*) from notes where archived = 0")
    suspend fun getNoteCount(): Int

    @Query("select count(*) from notes where archived = 1")
    suspend fun getArchiveCount(): Int

    @Query("select count(*) from notes where NULLIF(reminder,'') IS NOT NULL")
    suspend fun getReminderCount(): Int

    @Query("select * from notes where archived = 1")
    suspend fun getArchivedNoteFromDB(): List<NotesEntity>

    @Query("SELECT * from notes WHERE NULLIF(reminder,'') IS NOT NULL")
    suspend fun getReminderNotes(): List<NotesEntity>

    @Update
    suspend fun updateNewNoteInDB(note: NotesEntity)

    @Delete
    suspend fun deleteNoteFromDB(note: NotesEntity)

    @Query("Delete from notes")
    suspend fun deleteNoteTable()
}