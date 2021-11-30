package com.yml.fundo.data.service

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yml.fundo.common.Util
import com.yml.fundo.data.model.FirebaseLabel
import com.yml.fundo.data.model.FirebaseNotes
import com.yml.fundo.data.model.FirebaseUserDetails
import com.yml.fundo.data.room.DateTypeConverter
import com.yml.fundo.ui.wrapper.Label
import com.yml.fundo.ui.wrapper.Note
import com.yml.fundo.ui.wrapper.User
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.coroutines.suspendCoroutine

class FirebaseDatabase {
    private val fireStore = Firebase.firestore

    companion object {
        private val instance: FirebaseDatabase? = null
        fun getInstance(): FirebaseDatabase = instance ?: FirebaseDatabase()
    }

    suspend fun setToDatabase(user: User): User? {
        val userDetails = FirebaseUserDetails(user.name, user.email, user.mobileNo)
        return suspendCoroutine { callback ->
            fireStore.collection("users").document(user.fUid)
                .set(userDetails).addOnCompleteListener {
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
            fireStore.collection("users").document(fUid)
                .get().addOnCompleteListener { status ->
                    if (status.isSuccessful) {
                        status.result.also {
                            val userDb = Util.createUser(it?.data as HashMap<*, *>)
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

    suspend fun addNewNoteToDB(note: Note, user: User): Note {
        val dateTime = DateTypeConverter().fromOffsetDateTime(note.dateModified).toString()
        val notesInfo = FirebaseNotes(note.title, note.content, dateTime, note.archived)
        return suspendCoroutine { callback ->
            val refId =
                fireStore.collection("users").document(user.fUid)
                    .collection("notes").document().id
            fireStore.collection("users").document(user.fUid)
                .collection("notes").document(refId)
                .set(notesInfo).addOnCompleteListener {
                    if (it.isSuccessful) {
                        note.key = refId
                        callback.resumeWith(Result.success(note))
                    } else {
                        callback.resumeWith(Result.failure(it.exception!!))
                    }
                }
        }
    }

    suspend fun getNewNoteFromDB(user: User): ArrayList<Note>? {
        return suspendCoroutine { callback ->
            fireStore.collection("users").document(user.fUid).collection("notes")
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val noteList = ArrayList<Note>()
                        val dataSnapshot = it.result

                        if (dataSnapshot != null) {
                            for (item in dataSnapshot.documents) {
                                val noteHashMap = item.data as HashMap<*, *>
                                val reminder = DateTypeConverter().toOffsetDateTime(noteHashMap["reminder"] as String?)
                                val note = Note(
                                    noteHashMap["title"].toString(),
                                    noteHashMap["content"].toString(),
                                    dateModified = DateTypeConverter().toOffsetDateTime(noteHashMap["dateModified"].toString()) as Date,
                                    item.id,
                                    archived = noteHashMap["archived"] as Boolean,
                                    reminder = reminder
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

    suspend fun updateNewNoteInDB(note: Note, user: User): Boolean {
        val notesMap = mapOf(
            "title" to note.title,
            "content" to note.content,
            "dateModified" to DateTypeConverter().fromOffsetDateTime(note.dateModified).toString(),
            "archived" to note.archived,
            "reminder" to DateTypeConverter().fromOffsetDateTime(note.reminder).toString()
        )
        return suspendCoroutine { callback ->
            fireStore.collection("users").document(user.fUid).collection("notes")
                .document(note.key).update(notesMap)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback.resumeWith(Result.success(true))
                    } else {
                        callback.resumeWith(Result.failure(it.exception!!))
                    }
                }
        }
    }

    suspend fun deleteNoteFromDB(note: Note, user: User): Boolean {
        return suspendCoroutine { callback ->
            fireStore.collection("users").document(user.fUid).collection("notes")
                .document(note.key).delete().addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback.resumeWith(Result.success(true))
                    } else {
                        callback.resumeWith(Result.failure(it.exception!!))
                    }
                }
        }
    }

    suspend fun addNewLabelToDB(label: Label, user: User): Label {
        val labelDb = FirebaseLabel(
            label.name, DateTypeConverter()
                .fromOffsetDateTime(label.dateModified).toString()
        )
        return suspendCoroutine { callback ->
            val refId = fireStore.collection("users").document(user.fUid)
                .collection("labels").document().id
            fireStore.collection("users").document(user.fUid)
                .collection("labels").document(refId)
                .set(labelDb).addOnCompleteListener {
                    if (it.isSuccessful) {
                        label.fid = refId
                        Log.i("FbDb", "$refId")
                        callback.resumeWith(Result.success(label))
                    } else {
                        callback.resumeWith(Result.failure(it.exception!!))
                    }
                }
        }
    }

    suspend fun getLabel(user: User?): ArrayList<Label>? {
        return suspendCoroutine { callback ->
            fireStore.collection("users").document(user!!.fUid).collection("labels")
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val labelList = ArrayList<Label>()
                        val dataSnapshot = it.result

                        if (dataSnapshot != null) {
                            for (item in dataSnapshot.documents) {
                                val labelHashMap = item.data as HashMap<*, *>
                                val label = Label(
                                    name = labelHashMap["name"].toString(),
                                    dateModified = DateTypeConverter().toOffsetDateTime(labelHashMap["dateModified"].toString()) as Date,
                                    fid = item.id
                                )
                                labelList.add(label)
                            }
                            callback.resumeWith(Result.success(labelList))
                        } else {
                            callback.resumeWith(Result.failure(it.exception!!))
                        }
                    } else {
                        callback.resumeWith(Result.failure(it.exception!!))
                    }
                }
        }
    }

    suspend fun deleteLabel(label: Label, user: User?): Label {
        return suspendCoroutine { callback ->
            fireStore.collection("users").document(user!!.fUid).collection("labels")
                .document(label.fid).delete().addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback.resumeWith(Result.success(label))
                    } else {
                        callback.resumeWith(Result.failure(it.exception!!))
                    }
                }
        }
    }

    suspend fun updateLabel(label: Label, user: User): Label {
        val labelMap = mapOf(
            "name" to label.name,
            "dateModified" to DateTypeConverter().fromOffsetDateTime(label.dateModified).toString()
        )
        return suspendCoroutine { callback ->
            fireStore.collection("users").document(user.fUid).collection("labels")
                .document(label.fid).update(labelMap)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback.resumeWith(Result.success(label))
                    } else {
                        callback.resumeWith(Result.failure(it.exception!!))
                    }
                }
        }
    }
}