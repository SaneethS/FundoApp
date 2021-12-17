package com.yml.fundo.data.service

import android.content.Context
import android.util.Log
import com.yml.fundo.common.NetworkService
import com.yml.fundo.networking.users.UserService
import com.yml.fundo.ui.wrapper.Label
import com.yml.fundo.ui.wrapper.User
import com.yml.fundo.ui.wrapper.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseService(val context: Context) {
    private var sqlDb: SqLiteDatabase = SqLiteDatabase(context)
    private var firebaseDatabase = FirebaseDatabase.getInstance()

    companion object {
        private val instance: DatabaseService? by lazy { null }

        fun getInstance(context: Context): DatabaseService = instance ?: DatabaseService(context)
    }

    suspend fun setUserToDatabase(user: User): User? {
        return withContext(Dispatchers.IO) {
            try {
//                val userFirebase = firebaseDatabase.getUserFromDatabase(user.fUid)
                val userService = UserService().getUser(user.fUid)
                val userSql = sqlDb.setUserToDatabase(userService)
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
//                val userFirebase = firebaseDatabase.setUserToDatabase(user)
                val userService = UserService().setUser(user)
                val userSql = sqlDb.setUserToDatabase(userService)
                userSql
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getUserFromDatabase(uid: Long): User? {
        return withContext(Dispatchers.IO) {
            try {
                sqlDb.getUserFromDatabase(uid)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


    suspend fun addCloudDataToLocalDB(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            val noteListFromCloud = firebaseDatabase.getNotesFromDB(user)
            if (noteListFromCloud != null) {
                for (i in noteListFromCloud) {
                    sqlDb.addNewNoteToDB(i)
                }
            }
            true
        }
    }

    suspend fun addNoteToLocalDb(note: Note): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                sqlDb.addNewNoteToDB(note, false)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun addNewNoteToDB(note: Note, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (NetworkService.isNetworkAvailable(context)) {
                    val note = firebaseDatabase.addNewNoteToDB(note, user)
                    sqlDb.addNewNoteToDB(note, true)
                } else {
                    sqlDb.addNewNoteToDB(note, false)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun getNewNoteFromDB(): ArrayList<Note>? {
        return withContext(Dispatchers.IO) {
            try {
                val notesList = sqlDb.getNotesFromDB()
                notesList
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getPagedNote(limit: Int, offset: Int): ArrayList<Note>? {
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
    suspend fun getArchivePaged(limit: Int, offset: Int): ArrayList<Note>? {
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
    suspend fun getReminderPaged(limit: Int, offset: Int): ArrayList<Note>? {
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

    suspend fun getArchiveNotesFromDB(): ArrayList<Note>? {
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

    suspend fun getNotesFromCloud(user: User): ArrayList<Note>? {
        return withContext(Dispatchers.IO) {
            try {
                val notesList = firebaseDatabase.getNotesFromDB(user)
                notesList
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getReminderNotesFromDB(): ArrayList<Note>? {
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


    suspend fun updateNotesInDB(note: Note, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (NetworkService.isNetworkAvailable(context)) {
                    sqlDb.updateNotesInDB(note, true)
                    firebaseDatabase.updateNotesInDB(note, user)
                } else {
                    sqlDb.updateNotesInDB(note, false)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun deleteNoteFromDB(note: Note, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (NetworkService.isNetworkAvailable(context)) {
                    sqlDb.deleteNoteFromDB(note, true)
                    firebaseDatabase.deleteNoteFromDB(note, user)
                } else {
                    sqlDb.deleteNoteFromDB(note, false)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun labelNoteAssociation(noteId: String, lables: ArrayList<Label>, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                lables.forEach {
                    firebaseDatabase.labelNoteAssociation(noteId, it.fid, user)
                }
                true
            }catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun removeLabelNoteLink(linkId: String, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                firebaseDatabase.removeLabelNoteLink(linkId, user)
                true
            }catch (e:Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun getLabelForNote(noteId: String, user: User): ArrayList<Label>? {
        return withContext(Dispatchers.IO) {
            try {
                firebaseDatabase.getLabelForNote(noteId, user)
            }catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getOpCode(note: Note): Int {
        return withContext(Dispatchers.IO) {
            return@withContext sqlDb.getOpCode(note)
        }
    }

    suspend fun clearNoteAndOperation() {
        sqlDb.clearNoteAndOperation()
    }

    fun clearAllTables() {
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