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

        noteViewModel.addNewNoteStatus.observe(viewLifecycleOwner){
            if(it){
                sharedViewModel.setGoToHomePageStatus(true)
            }else{
                Toast.makeText(requireContext(),"note is not saved",Toast.LENGTH_LONG).show()
            }
        }

        noteContents()

        noteViewModel.updateNoteStatus.observe(viewLifecycleOwner){
            sharedViewModel.setGoToHomePageStatus(true)
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
            val updateNotes = HomePage.notesList.find {
                it.key == noteKey
            }
            var note = NotesKey(title, content, updateNotes!!.key)
            noteViewModel.updateNotes(note)
            Toast.makeText(requireContext(),"Updated successfully",Toast.LENGTH_LONG).show()
        }
    }
}