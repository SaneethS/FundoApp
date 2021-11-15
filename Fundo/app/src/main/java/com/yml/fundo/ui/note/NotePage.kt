package com.yml.fundo.ui.note

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.common.SharedPref
import com.yml.fundo.databinding.NotePageBinding
import com.yml.fundo.data.model.Notes
import com.yml.fundo.data.room.DateTypeConverter
import com.yml.fundo.ui.activity.SharedViewModel
import com.yml.fundo.data.wrapper.NotesKey
import com.yml.fundo.data.wrapper.User
import com.yml.fundo.ui.home.HomePage
import java.util.*

class NotePage: Fragment(R.layout.note_page) {
    lateinit var binding: NotePageBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var noteViewModel: NoteViewModel
    var noteTitle:String? = null
    var noteContent:String? = null
    var noteKey:String = ""
    var bundleDateModified: Date? = null
    var bundleNoteId:Long? = null
    private var userId:Long = 0L
    var currentUser: User = User(name = "Name", email = "EmailID", mobileNo = "MobileNumber")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = NotePageBinding.bind(view)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        userId = SharedPref.getId()
        noteViewModel.getUserInfo(userId)

        binding.backButton.setOnClickListener {
            sharedViewModel.setGoToHomePageStatus(true)
        }

        binding.saveNoteButton.setOnClickListener{
            saveNote()
        }

        binding.deleteButton.setOnClickListener {
            if(bundleNoteId == null){
                Toast.makeText(requireContext(),getString(R.string.create_a_note_toast),Toast.LENGTH_LONG).show()
            }else{
                deleteNote()
            }
        }

        noteViewModel.addNewNoteStatus.observe(viewLifecycleOwner){
            if(it){
                sharedViewModel.setGoToHomePageStatus(true)
            }else{
                Toast.makeText(requireContext(),getString(R.string.note_not_saved_toast),Toast.LENGTH_LONG).show()
            }
        }

        noteContents()

        noteViewModel.updateNoteStatus.observe(viewLifecycleOwner){
            if(it){
                sharedViewModel.setGoToHomePageStatus(true)
            }else{
                Toast.makeText(requireContext(),getString(R.string.update_not_succesful_toast),Toast.LENGTH_LONG).show()
            }
        }

        noteViewModel.deleteNoteStatus.observe(viewLifecycleOwner){
            if(it){
                sharedViewModel.setGoToHomePageStatus(true)
            }else{
                Toast.makeText(requireContext(),getString(R.string.deletion_failed_toast),Toast.LENGTH_LONG).show()
            }
        }

        noteViewModel.userDataStatus.observe(viewLifecycleOwner){
            currentUser = it
        }
    }

    private fun deleteNote() {
        var title = binding.titleText.text.toString()
        var content = binding.noteText.text.toString()
        if(title.isNotEmpty() || content.isNotEmpty()){
            var note = NotesKey(title, content, dateModified = bundleDateModified,noteKey,bundleNoteId!!)
            noteViewModel.deleteNotes(requireContext(),note, currentUser)
        }else{
            Toast.makeText(requireContext(),getString(R.string.deletion_not_posssible_toast),Toast.LENGTH_LONG).show()
        }
    }

    private fun noteContents() {
        val dateTime = DateTypeConverter().toOffsetDateTime(arguments?.getString("dateModified"))
        noteTitle = arguments?.getString("title")
        noteContent = arguments?.getString("notes")
        noteKey = arguments?.getString("key").toString()
        bundleDateModified = dateTime
        bundleNoteId = arguments?.getLong("id")
        Log.i("BundleKey","$bundleNoteId")
        binding.titleText.setText(noteTitle)
        binding.noteText.setText(noteContent)
    }

    private fun saveNote() {
        var title = binding.titleText.text.toString()
        var content = binding.noteText.text.toString()
        if(bundleNoteId == null) {
            var notes = NotesKey(title, content, dateModified = null)
            Log.i("NoteCurrent","$currentUser")
            noteViewModel.addNewNote(requireContext(),notes, currentUser)
        }else{
            var note = NotesKey(title, content, dateModified = bundleDateModified,noteKey, bundleNoteId!!)
            noteViewModel.updateNotes(requireContext(),note, currentUser)
            Toast.makeText(requireContext(),getString(R.string.updated_successfully_toast),Toast.LENGTH_LONG).show()
        }
    }
}