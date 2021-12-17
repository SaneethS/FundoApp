package com.yml.fundo.data.room.dao

import androidx.room.*
import com.yml.fundo.data.room.entity.NotesEntity

@Dao
interface NotesDao {

    @Insert
    fun addNewNoteToDB(note: NotesEntity): Long

    @Query("select * from notes where archived = 0 ")
    fun getNewNoteFromDB(): List<NotesEntity>

    @Query("select * from notes where archived = 0 LIMIT :limit OFFSET :offset")
    fun getPagedNote(limit: Int, offset: Int): List<NotesEntity>

    @Query("select * from notes where archived = 1 LIMIT :limit OFFSET :offset")
    fun getArchivePaged(limit: Int, offset: Int): List<NotesEntity>

    @Query("select * from notes where NULLIF(reminder,'') IS NOT NULL LIMIT :limit OFFSET :offset")
    fun getReminderPaged(limit: Int, offset: Int): List<NotesEntity>

    @Query("select count(*) from notes where archived = 0")
    fun getNoteCount(): Int

    @Query("select count(*) from notes where archived = 1")
    fun getArchiveCount(): Int

    @Query("select count(*) from notes where NULLIF(reminder,'') IS NOT NULL")
    fun getReminderCount(): Int

    @Query("select * from notes where archived = 1")
    fun getArchivedNoteFromDB(): List<NotesEntity>

    @Query("SELECT * from notes WHERE NULLIF(reminder,'') IS NOT NULL")
    fun getReminderNotes(): List<NotesEntity>

    @Update
    fun updateNewNoteInDB(note: NotesEntity)

    @Delete
    fun deleteNoteFromDB(note: NotesEntity)

    @Query("Delete from notes")
    fun deleteNoteTable()
}