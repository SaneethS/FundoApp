package com.yml.fundo.ui.note

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yml.fundo.common.SharedPref
import com.yml.fundo.data.service.DatabaseService
import com.yml.fundo.ui.wrapper.Label
import com.yml.fundo.ui.wrapper.Note
import com.yml.fundo.ui.wrapper.User
import kotlinx.coroutines.launch
import java.util.*

class NoteViewModel : ViewModel() {
    private val _addNewNoteStatus = MutableLiveData<Boolean>()
    val addNewNoteStatus = _addNewNoteStatus as LiveData<Boolean>

    private val _updateNoteStatus = MutableLiveData<Boolean>()
    val updateNoteStatus = _updateNoteStatus as LiveData<Boolean>

    private val _deleteNoteStatus = MutableLiveData<Boolean>()
    val deleteNoteStatus = _deleteNoteStatus as LiveData<Boolean>

    private val _userDataStatus = MutableLiveData<User>()
    val userDataStatus = _userDataStatus as LiveData<User>

    private val _getLabelForNotesStatus = MutableLiveData<ArrayList<Label>>()
    val getLabelForNotesStatus = _getLabelForNotesStatus as LiveData<ArrayList<Label>>

    fun addNewNote(context: Context, note: Note, user: User) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            note.dateModified = cal.time
            val result = DatabaseService.getInstance(context).addNewNoteToDB(note, user)
            if (result) {
                _addNewNoteStatus.value = result
            }
        }
    }

    fun updateNotes(context: Context, note: Note, user: User) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            note.dateModified = cal.time
            val status = DatabaseService.getInstance(context).updateNotesInDB(note, user)
            if (status) {
                _updateNoteStatus.value = status
            }
        }
    }

    fun deleteNotes(context: Context, note: Note, user: User) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            note.dateModified = cal.time
            val status = DatabaseService.getInstance(context).deleteNoteFromDB(note, user)
            if (status) {
                _deleteNoteStatus.value = status
            }
        }
    }

    fun getUserInfo(context: Context, uid: Long) {
        viewModelScope.launch {
            val userData = DatabaseService.getInstance(context).getUserFromDatabase(uid)
            if (userData != null) {
                _userDataStatus.postValue(userData)
            }
        }
    }

    fun getNoteFromLabel(context: Context, note: Note) {
        viewModelScope.launch {
            val userId = SharedPref.getId()
            val user = DatabaseService.getInstance(context).getUserFromDatabase(userId)
            val labelNoteList =
                user?.let { DatabaseService.getInstance(context).getLabelForNote(note.key, it) }
            _getLabelForNotesStatus.value = labelNoteList
        }
    }
}