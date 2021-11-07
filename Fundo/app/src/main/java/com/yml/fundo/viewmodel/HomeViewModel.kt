package com.yml.fundo.viewmodel

import android.app.Service
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yml.fundo.model.Notes
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.Database
import com.yml.fundo.service.Storage
import com.yml.fundo.wrapper.NotesKey

class HomeViewModel: ViewModel() {
    private val _userAvatarStatus = MutableLiveData<Bitmap>()
    val userAvatarStatus = _userAvatarStatus as LiveData<Bitmap>

    private val _getNewNotesStatus = MutableLiveData<ArrayList<NotesKey>>()
    val getNewNotesStatus = _getNewNotesStatus as LiveData<ArrayList<NotesKey>>

    fun logoutFromHome(){
        Authentication.logOut()
    }

    fun setUserAvatar(bitmap: Bitmap){
        Storage.setAvatar(bitmap){
            if(it){
                _userAvatarStatus.value = bitmap
            }
        }
    }

    fun getUserAvatar(){
        Storage.getAvatar {
            if(it != null){
                _userAvatarStatus.value = it
            }
        }
    }

    fun getNewNotes(){
        Database.getNewNoteFromDB {
            _getNewNotesStatus.value = it
        }
    }
}