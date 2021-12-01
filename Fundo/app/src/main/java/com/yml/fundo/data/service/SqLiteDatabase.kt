package com.yml.fundo.data.service

import android.content.Context
import android.util.Log
import com.yml.fundo.auth.Authentication
import com.yml.fundo.common.CREATE_OP_CODE
import com.yml.fundo.common.DELETE_OP_CODE
import com.yml.fundo.common.UPDATE_OP_CODE
import com.yml.fundo.data.room.database.LocalDatabase
import com.yml.fundo.data.room.entity.NotesEntity
import com.yml.fundo.data.room.entity.OperationEntity
import com.yml.fundo.data.room.entity.UserEntity
import com.yml.fundo.ui.wrapper.Note
import com.yml.fundo.ui.wrapper.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class SqLiteDatabase(context: Context) {

    private val localDatabase = LocalDatabase.getInstance(context)
    private val userDao = localDatabase.userDao()
    private val notesDao = localDatabase.notesDao()
    private val operationDao = localDatabase.operationDao()

    suspend fun setUserToDatabase(user: User): User {
        val fUid = Authentication.getCurrentUser()?.uid.toString()
        return withContext(Dispatchers.IO) {
            val userEntity = UserEntity(
                fid = fUid, name = user.name, email = user.email, mobile = user.mobileNo
            )
            user.uid = userDao.addUserToDB(userEntity)
            user
        }
    }

    suspend fun getUserFromDatabase(uid: Long): User {
        return withContext(Dispatchers.IO) {
            val userInfo = userDao.getUserFromDB(uid)
            val user = User(
                name = userInfo.name,
                email = userInfo.email,
                mobileNo = userInfo.mobile,
                fUid = userInfo.fid,
                uid = userInfo.uid
            )
            user
        }
    }

    suspend fun addNewNoteToDB(note: Note, onlineStatus: Boolean = true): Note {
        return withContext(Dispatchers.IO) {
            val noteEntity = NotesEntity(
                fNid = note.key,
                title = note.title,
                content = note.content,
                dateModified = note.dateModified,
                nid = note.id,
                archived = note.archived,
                reminder = note.reminder
            )
            note.id = notesDao.addNewNoteToDB(noteEntity)
            if (!onlineStatus) {
                val opEntity = OperationEntity(note.key, CREATE_OP_CODE)
                operationDao.addOp(opEntity)
            }
            note
        }
    }

    suspend fun getNotesFromDB(): ArrayList<Note> {
        return withContext(Dispatchers.IO) {
            val notesEntity = notesDao.getNewNoteFromDB()
            val notesList = arrayListOf<Note>()
            for (i in notesEntity) {
                val notesKey = Note(
                    title = i.title, content = i.content,
                    key = i.fNid, dateModified = i.dateModified, id = i.nid,
                    archived = i.archived, reminder = i.reminder
                )
                notesList.add(notesKey)
            }
            notesList
        }
    }

    suspend fun getPagedNote(limit: Int, offset: Int): ArrayList<Note> {
        return withContext(Dispatchers.IO) {
            val notesEntity = notesDao.getPagedNote(limit, offset)
            val notesList = arrayListOf<Note>()
            for (i in notesEntity) {
                val notesKey = Note(
                    title = i.title, content = i.content,
                    key = i.fNid, dateModified = i.dateModified, id = i.nid,
                    archived = i.archived, reminder = i.reminder
                )
                notesList.add(notesKey)
            }
            notesList
        }
    }

    suspend fun getArchivePaged(limit: Int, offset: Int): ArrayList<Note> {
        return withContext(Dispatchers.IO) {
            val notesEntity = notesDao.getArchivePaged(limit, offset)
            val notesList = arrayListOf<Note>()
            for (i in notesEntity) {
                val notesKey = Note(
                    title = i.title, content = i.content,
                    key = i.fNid, dateModified = i.dateModified, id = i.nid,
                    archived = i.archived, reminder = i.reminder
                )
                notesList.add(notesKey)
            }
            notesList
        }
    }

    suspend fun getReminderPaged(limit: Int, offset: Int): ArrayList<Note> {
        return withContext(Dispatchers.IO) {
            val notesEntity = notesDao.getReminderPaged(limit, offset)
            val notesList = arrayListOf<Note>()
            for (i in notesEntity) {
                val notesKey = Note(
                    title = i.title, content = i.content,
                    key = i.fNid, dateModified = i.dateModified, id = i.nid,
                    archived = i.archived, reminder = i.reminder
                )
                notesList.add(notesKey)
            }
            notesList
        }
    }

    suspend fun getNoteCount(): Int {
        return withContext(Dispatchers.IO) {
            return@withContext notesDao.getNoteCount()
        }
    }

    suspend fun getArchiveCount(): Int {
        return withContext(Dispatchers.IO) {
            return@withContext notesDao.getArchiveCount()
        }
    }

    suspend fun getReminderCount(): Int {
        return withContext(Dispatchers.IO) {
            return@withContext notesDao.getReminderCount()
        }
    }

    suspend fun updateNotesInDB(note: Note, onlineStatus: Boolean = true): Boolean {
        return withContext(Dispatchers.IO) {
            val noteEntity = NotesEntity(
                fNid = note.key, title = note.title, content = note.content,
                dateModified = note.dateModified, nid = note.id, archived = note.archived,
                reminder = note.reminder
            )
            notesDao.updateNewNoteInDB(noteEntity)
            if (!onlineStatus) {
                val opEntity = OperationEntity(note.key, UPDATE_OP_CODE)
                operationDao.addOp(opEntity)
            }
            true
        }
    }

    suspend fun deleteNoteFromDB(note: Note, onlineStatus: Boolean = true): Boolean {
        return withContext(Dispatchers.IO) {
            val noteEntity = NotesEntity(
                fNid = note.key, title = note.title, content = note.content,
                dateModified = note.dateModified, nid = note.id,
                archived = note.archived, reminder = note.reminder
            )
            notesDao.deleteNoteFromDB(noteEntity)
            if (!onlineStatus) {
                if (note.key.isNotEmpty()) {
                    val opEntity = OperationEntity(note.key, DELETE_OP_CODE)
                    operationDao.addOp(opEntity)
                }
            }
            true
        }
    }

    suspend fun getOpCode(note: Note): Int {
        Log.i("Opcode", "in op code")
        return withContext(Dispatchers.IO) {
            val opc = operationDao.getOpCode(note.key)
            if (opc != null) {
                return@withContext opc.opCode
            } else {
                return@withContext -1
            }
        }
    }

    suspend fun clearNoteAndOperation() {
        notesDao.deleteNoteTable()
        operationDao.deleteOp()
    }

    fun clearAllTables() {
        localDatabase.clearAllTables()
    }

    suspend fun getArchiveNoteFromDB(): ArrayList<Note> {
        return withContext(Dispatchers.IO) {
            val notesEntity = notesDao.getArchivedNoteFromDB()
            Log.i("SQLService","$notesEntity")
            val notesList = arrayListOf<Note>()
            for (i in notesEntity) {
                val notesKey = Note(
                    title = i.title, content = i.content,
                    key = i.fNid, dateModified = i.dateModified, id = i.nid,
                    archived = i.archived, reminder = i.reminder
                )
                try{
                    notesList.add(notesKey)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
            return@withContext notesList
//            return@withContext ArrayList(notesDao.getArchivedNoteFromDB().map { it.toNotes() })
        }
    }

    suspend fun getReminderNoteFromDB(): ArrayList<Note> {
        return withContext(Dispatchers.IO) {
            val notesEntity = notesDao.getReminderNotes()
            val notesList = arrayListOf<Note>()
            for (i in notesEntity) {
                val notesKey = Note(
                    title = i.title, content = i.content,
                    key = i.fNid, dateModified = i.dateModified, id = i.nid,
                    archived = i.archived, reminder = i.reminder
                )
                notesList.add(notesKey)
            }
            notesList
        }
    }
}