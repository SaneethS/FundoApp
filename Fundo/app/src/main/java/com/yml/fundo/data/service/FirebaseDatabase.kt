package com.yml.fundo.data.service

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yml.fundo.auth.Authentication
import com.yml.fundo.data.model.Notes
import com.yml.fundo.data.wrapper.User
import com.yml.fundo.data.model.UserDetails
import com.yml.fundo.common.Util
import com.yml.fundo.data.room.DateTypeConverter
import com.yml.fundo.data.wrapper.NotesKey
import kotlin.coroutines.suspendCoroutine

object FirebaseDatabase {
    private var database: DatabaseReference = Firebase.database.reference

    suspend fun setToDatabase(user: User): User? {
        var userDetails = UserDetails(user.name, user.email, user.mobileNo)
        return suspendCoroutine { callback ->
            database.child("users").child(user.fUid)
                .setValue(userDetails).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Util.createUserInSharedPref(userDetails)
                        callback.resumeWith(Result.success(user))

                    } else {
                        callback.resumeWith(Result.failure(it.exception!!))
                    }
                }
        }
    }

    suspend fun getFromDatabase(fUid: String): User {
        return suspendCoroutine { callback ->
            database.child("users").child(fUid)
                .get().addOnCompleteListener { status ->
                    if (status.isSuccessful) {
                        status.result.also {
                            val userDb = Util.createUser(it?.value as HashMap<*, *>)
//                        Util.createUserInSharedPref(user)
                            val user = User(
                                name = userDb.name, email = userDb.email,
                                mobileNo = userDb.mobileNo, fUid = fUid
                            )
                            callback.resumeWith(Result.success(user))
                        }
                    } else {
                        callback.resumeWith(Result.failure(status.exception!!))
                    }
                }
        }
    }

    suspend fun addNewNoteToDB(notes: NotesKey, user: User): NotesKey {
        var notesInfo = Notes(notes.title, notes.content, notes.dateModified)
        return suspendCoroutine { callback ->
            Log.i("NoteFB","${user.fUid}")
            val ref = database.child("note").child(user.fUid).push()
            ref.setValue(notesInfo).addOnCompleteListener {
                if (it.isSuccessful) {
                    notes.key = ref.key.toString()
                    callback.resumeWith(Result.success(notes))
                } else {
                    callback.resumeWith(Result.failure(it.exception!!))
                }
            }
        }
    }

    suspend fun getNewNoteFromDB(user: User): ArrayList<NotesKey>? {
        return suspendCoroutine { callback ->
            database.child("note").child(user.fUid).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    var noteList = ArrayList<NotesKey>()
                    var dataSnapshot = it.result

                    if (dataSnapshot != null) {
                        for (item in dataSnapshot.children) {
                            var dateModified  = item.child("dateModified").value.toString()
                            val dateTime = DateTypeConverter().toOffsetDateTime(dateModified)
                            var note = NotesKey(
                                item.child("title").value.toString(),
                                item.child("content").value.toString(), dateModified = dateTime,
                                item.key.toString()
                            )
                            noteList.add(note)
                        }
                        callback.resumeWith(Result.success(noteList))
                    } else {
                        callback.resumeWith(Result.failure(it.exception!!))
                    }
                } else {
                    callback.resumeWith(Result.failure(it.exception!!))
                }
            }
        }
    }

    suspend fun updateNewNoteInDB(notes: NotesKey, user: User): Boolean {
        val notesMap = mapOf(
            "title" to notes.title,
            "content" to notes.content,
            "dateModified" to notes.dateModified
        )
        return suspendCoroutine { callback ->
            database.child("note").child(user.fUid).child(notes.key).updateChildren(notesMap)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback.resumeWith(Result.success(true))
                    } else {
                        callback.resumeWith(Result.failure(it.exception!!))
                    }
                }
        }
    }

    suspend fun deleteNoteFromDB(notes: NotesKey, user: User): Boolean {
        return suspendCoroutine { callback ->
            database.child("note").child(user.fUid).child(notes.key).removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.resumeWith(Result.success(true))
                } else {
                    callback.resumeWith(Result.failure(it.exception!!))
                }
            }

        }
    }
}