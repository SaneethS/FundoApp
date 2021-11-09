package com.yml.fundo.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.databinding.NotePageBinding
import com.yml.fundo.model.Notes
import com.yml.fundo.viewmodel.NoteViewModel
import com.yml.fundo.viewmodel.NoteViewModelFactory
import com.yml.fundo.viewmodel.SharedViewModel
import com.yml.fundo.viewmodel.SharedViewModelFactory
import com.yml.fundo.wrapper.NotesKey

class NotePage: Fragment(R.layout.note_page) {
    lateinit var binding: NotePageBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var noteViewModel: NoteViewModel
    var noteTitle:String? = null
    var noteContent:String? = null
    var noteKey:String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = NotePageBinding.bind(view)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory())[NoteViewModel::class.java]
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        binding.backButton.setOnClickListener {
            sharedViewModel.setGoToHomePageStatus(true)
        }

        binding.saveNoteButton.setOnClickListener{
            saveNote()
        }

        binding.deleteButton.setOnClickListener {
            if(noteKey == null){
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
    }

    private fun deleteNote() {
        var title = binding.titleText.text.toString()
        var content = binding.noteText.text.toString()
        if(title.isNotEmpty() || content.isNotEmpty()){
            var note = NotesKey(title, content, noteKey!!)
            noteViewModel.deleteNotes(note)
        }else{
            Toast.makeText(requireContext(),getString(R.string.deletion_not_posssible_toast),Toast.LENGTH_LONG).show()
        }
    }

    private fun noteContents() {
        noteTitle = arguments?.getString("title")
        noteContent = arguments?.getString("notes")
        noteKey = arguments?.getString("key")
        binding.titleText.setText(noteTitle)
        binding.noteText.setText(noteContent)
    }

    private fun saveNote() {
        var title = binding.titleText.text.toString()
        var content = binding.noteText.text.toString()
        if(noteKey == null) {
            var notes = Notes(title, content)
            noteViewModel.addNewNote(notes)
        }else{
            var note = NotesKey(title, content, noteKey!!)
            noteViewModel.updateNotes(note)
            Toast.makeText(requireContext(),getString(R.string.updated_successfully_toast),Toast.LENGTH_LONG).show()
        }
    }
}