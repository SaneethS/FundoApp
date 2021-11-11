package com.yml.fundo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yml.fundo.model.Notes
import com.yml.fundo.service.DatabaseService
import com.yml.fundo.service.FirebaseDatabase
import com.yml.fundo.wrapper.NotesKey
import kotlinx.coroutines.launch

class NoteViewModel: ViewModel() {
    private val _addNewNoteStatus = MutableLiveData<Boolean>()
    val addNewNoteStatus = _addNewNoteStatus as LiveData<Boolean>

    private val _updateNoteStatus = MutableLiveData<Boolean>()
    val updateNoteStatus = _updateNoteStatus as LiveData<Boolean>

    private val _deleteNoteStatus = MutableLiveData<Boolean>()
    val deleteNoteStatus = _deleteNoteStatus as LiveData<Boolean>

    fun addNewNote(notes: Notes){
        viewModelScope.launch {
            var result = DatabaseService.addNewNoteToDB(notes)
            if(result){
                _addNewNoteStatus.value = result
            }
        }
    }

    fun updateNotes(notes: NotesKey){
        viewModelScope.launch {
            var status = DatabaseService.updateNewNoteInDB(notes)
            if (status){
                _updateNoteStatus.value = status
            }
        }
    }

    fun deleteNotes(notes: NotesKey){
        viewModelScope.launch {
            var status = DatabaseService.deleteNoteFromDB(notes)
            if(status){
                _deleteNoteStatus.value = status
            }
        }
    }
}