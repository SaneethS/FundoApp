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
import com.yml.fundo.ui.wrapper.Notes
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

    suspend fun setToDatabase(user: User): User {
        val fUid = Authentication.getCurrentUser()?.uid.toString()
        return withContext(Dispatchers.IO) {
            val userEntity = UserEntity(
                fid = fUid, name = user.name, email = user.email, mobile = user.mobileNo
            )
            user.uid = userDao.addUserToDB(userEntity)
            user
        }
    }

    suspend fun getFromDatabase(uid: Long): User {
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

    suspend fun addNewNoteToDB(notes: Notes, onlineStatus: Boolean = true): Notes {
        return withContext(Dispatchers.IO) {
            val noteEntity = NotesEntity(
                fNid = notes.key,
                title = notes.title,
                content = notes.content,
                dateModified = notes.dateModified,
                nid = notes.id,
                archived = notes.archived,
                reminder = notes.reminder
            )
            notes.id = notesDao.addNewNoteToDB(noteEntity)
            if (!onlineStatus) {
                val opEntity = OperationEntity(notes.key, CREATE_OP_CODE)
                operationDao.addOp(opEntity)
            }
            notes
        }
    }

    suspend fun getNewNoteFromDB(): ArrayList<Notes> {
        return withContext(Dispatchers.IO) {
            val notesEntity = notesDao.getNewNoteFromDB()
            val notesList = arrayListOf<Notes>()
            for (i in notesEntity) {
                val notesKey = Notes(
                    title = i.title, content = i.content,
                    key = i.fNid, dateModified = i.dateModified, id = i.nid,
                    archived = i.archived, reminder = i.reminder
                )
                notesList.add(notesKey)
            }
            notesList
        }
    }

    suspend fun updateNewNoteInDB(notes: Notes, onlineStatus: Boolean = true): Boolean {
        return withContext(Dispatchers.IO) {
            val noteEntity = NotesEntity(
                fNid = notes.key, title = notes.title, content = notes.content,
                dateModified = notes.dateModified, nid = notes.id, archived = notes.archived,
                reminder = notes.reminder
            )
            notesDao.updateNewNoteInDB(noteEntity)
            if (!onlineStatus) {
                val opEntity = OperationEntity(notes.key, UPDATE_OP_CODE)
                operationDao.addOp(opEntity)
            }
            true
        }
    }

    suspend fun deleteNoteFromDB(notes: Notes, onlineStatus: Boolean = true): Boolean {
        return withContext(Dispatchers.IO) {
            val noteEntity = NotesEntity(
                fNid = notes.key, title = notes.title, content = notes.content,
                dateModified = notes.dateModified, nid = notes.id,
                archived = notes.archived, reminder = notes.reminder
            )
            notesDao.deleteNoteFromDB(noteEntity)
            if (!onlineStatus) {
                if (notes.key.isNotEmpty()) {
                    val opEntity = OperationEntity(notes.key, DELETE_OP_CODE)
                    operationDao.addOp(opEntity)
                }
            }
            true
        }
    }

    suspend fun getOpCode(note: Notes): Int {
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

    suspend fun getArchiveNoteFromDB(): ArrayList<Notes> {
        return withContext(Dispatchers.IO) {
            val notesEntity = notesDao.getArchivedNoteFromDB()
            val notesList = arrayListOf<Notes>()
            for (i in notesEntity) {
                val notesKey = Notes(
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
            notesList
//            return@withContext ArrayList(notesDao.getArchivedNoteFromDB().map { it.toNotes() })
        }
    }

    suspend fun getReminderNoteFromDB(): ArrayList<Notes> {
        return withContext(Dispatchers.IO) {
            val notesEntity = notesDao.getReminderNotes()
            val notesList = arrayListOf<Notes>()
            for (i in notesEntity) {
                val notesKey = Notes(
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