package com.yml.fundo.data.service

import android.content.Context
import com.yml.fundo.auth.Authentication
import com.yml.fundo.common.CREATE_OP_CODE
import com.yml.fundo.common.DELETE_OP_CODE
import com.yml.fundo.common.UPDATE_OP_CODE
import com.yml.fundo.common.Util
import com.yml.fundo.data.model.Notes
import com.yml.fundo.data.model.UserDetails
import com.yml.fundo.data.room.database.LocalDatabase
import com.yml.fundo.data.room.entity.NotesEntity
import com.yml.fundo.data.room.entity.OperationEntity
import com.yml.fundo.data.room.entity.UserEntity
import com.yml.fundo.data.wrapper.NotesKey
import com.yml.fundo.data.wrapper.User
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

    suspend fun getFromDatabase(uid: Long): User{
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

    suspend fun addNewNoteToDB(notesKey: NotesKey, onlineStatus:Boolean = true): NotesKey {
        return withContext(Dispatchers.IO) {
            val noteEntity = NotesEntity(
                fNid = notesKey.key,
                title = notesKey.title,
                content = notesKey.content,
                dateModified = notesKey.dateModified,
                nid = notesKey.id
            )
            notesKey.id = notesDao.addNewNoteToDB(noteEntity)
            if(!onlineStatus){
                val opEntity = OperationEntity(notesKey.key, CREATE_OP_CODE)
                operationDao.addOp(opEntity)
            }
            notesKey
        }
    }

    suspend fun getNewNoteFromDB(): ArrayList<NotesKey>{
        return withContext(Dispatchers.IO){
            val notesEntity = notesDao.getNewNoteFromDB()
            val notesList = arrayListOf<NotesKey>()
            for (i in notesEntity){
                val notesKey = NotesKey(title = i.title, content = i.content,
                    key = i.fNid, dateModified = i.dateModified, id = i.nid)
                notesList.add(notesKey)
            }
            notesList
        }
    }

    suspend fun updateNewNoteInDB(notes: NotesKey, onlineStatus: Boolean = true): Boolean {
        return withContext(Dispatchers.IO) {
            val noteEntity = NotesEntity(
                fNid = notes.key, title = notes.title, content = notes.content,
                dateModified = notes.dateModified, nid = notes.id
            )
            notesDao.updateNewNoteInDB(noteEntity)
            if(!onlineStatus){
                val opEntity = OperationEntity(notes.key, UPDATE_OP_CODE)
                operationDao.addOp(opEntity)
            }
            true
        }
    }

    suspend fun deleteNoteFromDB(notes: NotesKey, onlineStatus: Boolean = true): Boolean {
        return withContext(Dispatchers.IO) {
            val noteEntity = NotesEntity(
                fNid = notes.key, title = notes.title, content = notes.content,
                dateModified = notes.dateModified, nid = notes.id
            )
            notesDao.deleteNoteFromDB(noteEntity)
            if(!onlineStatus){
                if(notes.key.isNotEmpty()){
                    val opEntity = OperationEntity(notes.key, DELETE_OP_CODE)
                    operationDao.addOp(opEntity)
                }
            }
            true
        }
    }

    suspend fun getOpCode(note: NotesKey): Int{
        return withContext(Dispatchers.IO){
            val opc = operationDao.getOpCode(note.key)
            if(opc != null){
                return@withContext opc.opCode
            }else{
                return@withContext -1
            }
        }
    }

    suspend fun clearNoteAndOperation(){
        notesDao.deleteNoteTable()
        operationDao.deleteOp()
    }
}