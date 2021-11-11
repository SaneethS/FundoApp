package com.yml.fundo.ui.home

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yml.fundo.auth.Authentication
import com.yml.fundo.data.service.DatabaseService
import com.yml.fundo.data.service.Storage
import com.yml.fundo.data.model.NotesKey
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel: ViewModel() {
    private val _userAvatarStatus = MutableLiveData<Bitmap>()
    val userAvatarStatus = _userAvatarStatus as LiveData<Bitmap>

    private val _getNewNotesStatus = MutableLiveData<ArrayList<NotesKey>>()
    val getNewNotesStatus = _getNewNotesStatus as LiveData<ArrayList<NotesKey>>

    fun logoutFromHome(){
        Authentication.logOut()
    }

    fun setUserAvatar(bitmap: Bitmap){
        viewModelScope.launch {
            try {
                var status = Storage.setAvatar(bitmap)
                if(status){
                    _userAvatarStatus.value = bitmap
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun getUserAvatar(){
        viewModelScope.launch {
            var bitmap = Storage.getAvatar()
            if(bitmap != null){
                _userAvatarStatus.value = bitmap
            }
        }
    }



    fun getNewNotes(){
        viewModelScope.launch {
            var resultNotes = DatabaseService.getNewNoteFromDB()
            if(resultNotes != null){
                _getNewNotesStatus.value = resultNotes
            }
        }
    }
}