package com.yml.fundo.data.service

import android.content.Context
import android.util.Log
import com.yml.fundo.common.NetworkService
import com.yml.fundo.ui.wrapper.Label
import com.yml.fundo.ui.wrapper.User
import com.yml.fundo.ui.wrapper.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseService(val context: Context) {
    private var sqlDb: SqLiteDatabase = SqLiteDatabase(context)
    private var firebaseDatabase = FirebaseDatabase.getInstance()

    companion object {
        private val instance: DatabaseService? by lazy { null }

        fun getInstance(context: Context): DatabaseService = instance ?: DatabaseService(context)
    }

    suspend fun setToDatabase(user: User): User? {
        return withContext(Dispatchers.IO) {
            try {
                val userFirebase = firebaseDatabase.getFromDatabase(user.fUid)
                val userSql = sqlDb.setToDatabase(userFirebase)
                userSql
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun setNewUserToDatabase(user: User): User? {
        return withContext(Dispatchers.IO) {
            try {
                val userFirebase = firebaseDatabase.setToDatabase(user)
                val userSql = sqlDb.setToDatabase(userFirebase!!)
                userSql
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getFromDatabase(uid: Long): User? {
        return withContext(Dispatchers.IO) {
            try {
                sqlDb.getFromDatabase(uid)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


    suspend fun addCloudDataToLocalDB(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            val noteListFromCloud = firebaseDatabase.getNewNoteFromDB(user)
            if (noteListFromCloud != null) {
                for (i in noteListFromCloud) {
                    sqlDb.addNewNoteToDB(i)
                }
            }
            true
        }
    }

    suspend fun addNoteToLocalDb(notes: Notes): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                sqlDb.addNewNoteToDB(notes, false)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun addNewNoteToDB(notes: Notes, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (NetworkService.isNetworkAvailable(context)) {
                    val note = firebaseDatabase.addNewNoteToDB(notes, user)
                    sqlDb.addNewNoteToDB(note, true)
                } else {
                    sqlDb.addNewNoteToDB(notes, false)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun getNewNoteFromDB(): ArrayList<Notes>? {
        return withContext(Dispatchers.IO) {
            try {
                val notesList = sqlDb.getNewNoteFromDB()
                notesList
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getPagedNote(limit: Int, offset: Int): ArrayList<Notes>? {
        return withContext(Dispatchers.IO) {
            try {
                val notesList = sqlDb.getPagedNote(limit, offset)
                notesList
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    suspend fun getArchivePaged(limit: Int, offset: Int): ArrayList<Notes>? {
        return withContext(Dispatchers.IO) {
            try {
                val notesList = sqlDb.getArchivePaged(limit, offset)
                notesList
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    suspend fun getReminderPaged(limit: Int, offset: Int): ArrayList<Notes>? {
        return withContext(Dispatchers.IO) {
            try {
                val notesList = sqlDb.getReminderPaged(limit, offset)
                notesList
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getNoteCount(): Int {
        return withContext(Dispatchers.IO) {
            try {
                sqlDb.getNoteCount()
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
    }

    suspend fun getArchiveCount(): Int {
        return withContext(Dispatchers.IO) {
            try {
                sqlDb.getArchiveCount()
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
    }

    suspend fun getReminderCount(): Int {
        return withContext(Dispatchers.IO) {
            try {
                sqlDb.getReminderCount()
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
    }

    suspend fun getArchiveNotesFromDB(): ArrayList<Notes>? {
        return withContext(Dispatchers.IO) {
            try {
                val notesList = sqlDb.getArchiveNoteFromDB()
                notesList
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getNewNoteFromCloud(user: User): ArrayList<Notes>? {
        return withContext(Dispatchers.IO) {
            try {
                val notesList = firebaseDatabase.getNewNoteFromDB(user)
                notesList
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getReminderNotesFromDB(): ArrayList<Notes>? {
        return withContext(Dispatchers.IO) {
            try {
                val notesList = sqlDb.getReminderNoteFromDB()
                notesList
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


    suspend fun updateNewNoteInDB(notes: Notes, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (NetworkService.isNetworkAvailable(context)) {
                    sqlDb.updateNewNoteInDB(notes, true)
                    firebaseDatabase.updateNewNoteInDB(notes, user)
                } else {
                    sqlDb.updateNewNoteInDB(notes, false)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun deleteNoteFromDB(notes: Notes, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (NetworkService.isNetworkAvailable(context)) {
                    sqlDb.deleteNoteFromDB(notes, true)
                    firebaseDatabase.deleteNoteFromDB(notes, user)
                } else {
                    sqlDb.deleteNoteFromDB(notes, false)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun getOpCode(notes: Notes): Int {
        return withContext(Dispatchers.IO) {
            return@withContext sqlDb.getOpCode(notes)
        }
    }

    suspend fun clearNoteAndOperation() {
        sqlDb.clearNoteAndOperation()
    }

    suspend fun clearAllTables() {
        sqlDb.clearAllTables()
    }

    suspend fun addNewLabelToDB(label: Label, user: User): Label? {
        Log.i("FBDataLayer", "Label call in Data layer")
        return withContext(Dispatchers.IO) {
            try {
                val label = firebaseDatabase.addNewLabelToDB(label, user)
                label
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getLabel(user: User?): ArrayList<Label>? {
        return withContext(Dispatchers.IO) {
            try {
                val label = firebaseDatabase.getLabel(user)
                label
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun deleteLabel(label: Label, user: User?): Label? {
        return withContext(Dispatchers.IO) {
            try {
                val label = firebaseDatabase.deleteLabel(label, user)
                label
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun updateLabel(label: Label, user: User): Label? {
        return withContext(Dispatchers.IO) {
            try {
                val label = firebaseDatabase.updateLabel(label, user)
                label
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}