package com.yml.fundo.data.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.yml.fundo.common.NetworkService
import com.yml.fundo.ui.wrapper.Notes
import com.yml.fundo.ui.wrapper.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.yml.fundo.common.DELETE_OP_CODE as DELETE_OP_CODE

class SyncDatabase(val context: Context) {
    private var firebaseDatabase = FirebaseDatabase.getInstance()

    suspend fun syncNow(user: User) {
        if (NetworkService.isNetworkAvailable(context)) {
            val latestNotes = getLatestNotesFromDB(user)
            DatabaseService.getInstance(context).clearNoteAndOperation()
            latestNotes.forEach {
                DatabaseService.getInstance(context).addNoteToLocalDb(it)
            }
        } else {
            Toast.makeText(context, "No Internet Available", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun getLatestNotesFromDB(user: User): List<Notes> {
        return withContext(Dispatchers.IO) {
            val sqlLiteNoteList = DatabaseService.getInstance(context).getNewNoteFromDB()
            val localNotesList = mutableListOf<Notes>()
            if (sqlLiteNoteList != null) {
                localNotesList.addAll(sqlLiteNoteList)
            }
            val firebaseNotesList = DatabaseService.getInstance(context).getNewNoteFromCloud(user)
            val latestNotes = mutableListOf<Notes>()

            if (firebaseNotesList != null) {
                for (noteF in firebaseNotesList) {
                    var localNoteCounter = 0
                    for (noteL in localNotesList) {
                        if (noteF.key == noteL.key) {
                            val res = compareTimeStamp(noteL, noteF)
                            Log.i("SyncDb", res.toString())
                            if (res) {
                                latestNotes.add(noteL)
                                firebaseDatabase.updateNewNoteInDB(
                                    noteL,
                                    user
                                )
                            } else {
                                latestNotes.add(noteF)
                            }
                            break
                        }
                        localNoteCounter++
                    }
                    if (localNoteCounter == localNotesList.size) {
                        localNotesList.add(noteF)
                        latestNotes.add(noteF)
                    }
                }
                for (noteL in localNotesList) {
                    var cloudNoteCounter = 0
                    for (noteF in firebaseNotesList) {
                        if (noteL.key == noteF.key) {
                            if (getOpCode(noteL) == DELETE_OP_CODE) {
                                firebaseDatabase.deleteNoteFromDB(
                                    noteL,
                                    user
                                )
                                latestNotes.remove(noteL)
                            }
                            break
                        }
                        cloudNoteCounter++
                    }
                    if (cloudNoteCounter == firebaseNotesList.size) {
                        val opCode = getOpCode(noteL)
                        if (opCode != -1) {
                            latestNotes.add(noteL)
                            firebaseDatabase.addNewNoteToDB(
                                noteL,
                                user
                            )
                        }
                    }
                }
                return@withContext latestNotes
            } else {
                return@withContext listOf<Notes>()
            }
        }
    }

    private suspend fun getOpCode(noteL: Notes): Int {
        return withContext(Dispatchers.IO) {
            return@withContext DatabaseService.getInstance(context).getOpCode(noteL)
        }
    }

    private fun compareTimeStamp(noteL: Notes, noteF: Notes): Boolean {
        val localTime = noteL.dateModified
        val cloudTime = noteF.dateModified
        return if (localTime != null && cloudTime != null) {
            localTime.after(cloudTime)
        } else {
            false
        }
    }
}