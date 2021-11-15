package com.yml.fundo.data.service

import android.util.Log
import com.yml.fundo.common.DELETE_OP_CODE
import com.yml.fundo.data.wrapper.NotesKey
import com.yml.fundo.data.wrapper.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SyncDatabase {

    suspend fun syncNow(user: User){
        val latestNotes = getLatestNotesFromDB(user)
        DatabaseService.clearNoteAndOperation()
        latestNotes.forEach{
            DatabaseService.addNoteToLocalDb(it)
        }
    }

    suspend fun getLatestNotesFromDB(user: User): List<NotesKey>{
        return withContext(Dispatchers.IO){
            val sqlLiteNoteList = DatabaseService.getNewNoteFromDB()
            val localNotesList = mutableListOf<NotesKey>()
            if (sqlLiteNoteList != null) {
                localNotesList.addAll(sqlLiteNoteList)
            }
            val firebaseNotesList = DatabaseService.getNewNoteFromCloud(user)
            val latestNotes = mutableListOf<NotesKey>()

            if(firebaseNotesList != null){
                for(noteF in firebaseNotesList){
                    var localNoteCounter = 0
                    for(noteL in localNotesList){
                        if(noteF.key  == noteL.key){
                            val res = compareTimeStamp(noteL,noteF)
                            Log.i("SyncDb",res.toString())
                            if(res){
                                latestNotes.add(noteL)
                                FirebaseDatabase.updateNewNoteInDB(
                                    noteL,
                                    user
                                )
                            }else{
                                latestNotes.add(noteF)
                            }
                            break
                        }
                        localNoteCounter++
                    }
                    if(localNoteCounter == localNotesList.size){
                        localNotesList.add(noteF)
                        latestNotes.add(noteF)
                    }
                }
                for(noteL in localNotesList){
                    var cloudNoteCounter = 0
                    for(noteF in firebaseNotesList){
                        if(noteL.key == noteF.key){
                            if(getOpCode(noteL) == DELETE_OP_CODE){
                                FirebaseDatabase.deleteNoteFromDB(
                                    noteL,
                                    user
                                )
                                latestNotes.remove(noteL)
                            }
                            break
                        }
                        cloudNoteCounter++
                    }
                    if(cloudNoteCounter == firebaseNotesList.size){
                        val opCode = getOpCode(noteL)
                        if(opCode != -1){
                            latestNotes.add(noteL)
                            FirebaseDatabase.addNewNoteToDB(
                                noteL,
                                user
                            )
                        }
                    }
                }
                return@withContext latestNotes
            }else{
                return@withContext listOf<NotesKey>()
            }
        }
    }

    private suspend fun getOpCode(noteL: NotesKey): Int {
        return withContext(Dispatchers.IO){
            return@withContext DatabaseService.getOpCode(noteL)
        }
    }

    private fun compareTimeStamp(noteL: NotesKey, noteF: NotesKey): Boolean{
        var localTime = noteL.dateModified
        var cloudTime = noteF.dateModified
        Log.i("SyncDblocal",localTime.toString())
        Log.i("SyncDbcloud",cloudTime.toString())
        return if(localTime != null && cloudTime != null){
            localTime.after(cloudTime)
        }else{
            false
        }
    }
}