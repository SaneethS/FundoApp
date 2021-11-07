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

class NotePage: Fragment(R.layout.note_page) {
    lateinit var binding: NotePageBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var noteViewModel: NoteViewModel

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
    }

    private fun noteContents() {
        var title = arguments?.getString("title")
        var notes = arguments?.getString("notes")
        binding.titleText.setText(title)
        binding.noteText.setText(notes)
    }

    private fun saveNote() {
        var title = binding.titleText.text.toString()
        var content = binding.noteText.text.toString()
        var notes = Notes(title, content)
        noteViewModel.addNewNote(notes)
    }
}