package com.yml.fundo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yml.fundo.auth.Authentication
import com.yml.fundo.pushnotification.FirebaseTopicMessaging
import com.yml.fundo.ui.wrapper.Note

class SharedViewModel : ViewModel() {
    private val _goToHomePageStatus = MutableLiveData<Boolean>()
    val goToHomePageStatus = _goToHomePageStatus as LiveData<Boolean>

    private val _goToLoginPageStatus = MutableLiveData<Boolean>()
    val goToLoginPageStatus = _goToLoginPageStatus as LiveData<Boolean>

    private val _goToRegisterPageStatus = MutableLiveData<Boolean>()
    val goToRegisterPageStatus = _goToRegisterPageStatus as LiveData<Boolean>

    private val _goToSplashScreenStatus = MutableLiveData<Boolean>()
    val goToSplashScreenStatus = _goToSplashScreenStatus as LiveData<Boolean>

    private val _goToResetPasswordStatus = MutableLiveData<Boolean>()
    val goToResetPasswordStatus = _goToResetPasswordStatus as LiveData<Boolean>

    private val _goToNotePageStatus = MutableLiveData<Boolean>()
    val goToNotePageStatus = _goToNotePageStatus as LiveData<Boolean>

    private val _goToLabelCreateStatus = MutableLiveData<Boolean>()
    val goToLabelCreateStatus = _goToLabelCreateStatus as LiveData<Boolean>

    private val _goToArchivedNotePageStatus = MutableLiveData<Boolean>()
    val goToArchiveNotePageStatus = _goToArchivedNotePageStatus as LiveData<Boolean>

    private val _goToReminderNotePageStatus = MutableLiveData<Boolean>()
    val goToReminderNotePageStatus = _goToReminderNotePageStatus as LiveData<Boolean>

    private val _goToExistNotePageStatus = MutableLiveData<Note>()
    val goToExistNotePageStatus = _goToExistNotePageStatus as LiveData<Note>

    private val _goToSelectLabelPageStatus = MutableLiveData<Note>()
    val goToSelectLabelPageStatus = _goToSelectLabelPageStatus as LiveData<Note>

    fun setGoToHomePageStatus(status: Boolean) {
        _goToHomePageStatus.value = status
    }

    fun setGoToLoginPageStatus(status: Boolean) {
        _goToLoginPageStatus.value = status
    }

    fun setGoToRegisterPageStatus(status: Boolean) {
        _goToRegisterPageStatus.value = status
    }

    fun setGoToResetPasswordStatus(status: Boolean) {
        _goToResetPasswordStatus.value = status
    }

    fun setGoToNotePageStatus(status: Boolean) {
        _goToNotePageStatus.value = status
    }

    fun setGoToLabelCreateStatus(status: Boolean) {
        _goToLabelCreateStatus.value = status
    }

    fun setGoToArchivedNotePageStatus(status: Boolean){
        _goToArchivedNotePageStatus.value = status
    }

    fun setGoToReminderNotePageStatus(status: Boolean){
        _goToReminderNotePageStatus.value = status
    }

    fun setGoToExistingNotePage(note: Note){
        _goToExistNotePageStatus.value = note
    }

    fun setGoToSelectLabelPage(note: Note) {
        _goToSelectLabelPageStatus.value = note
    }

    fun checkUser(): Boolean {
        return Authentication.getCurrentUser() != null
    }

    fun setTopicToSubscribe() {
        FirebaseTopicMessaging.setTopicToSubscribe()
    }
}