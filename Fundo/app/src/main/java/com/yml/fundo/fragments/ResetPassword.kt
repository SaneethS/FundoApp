package com.yml.fundo.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.databinding.ResetPasswordBinding
import com.yml.fundo.service.Authentication
import com.yml.fundo.util.Validator
import com.yml.fundo.viewmodel.SharedViewModel
import com.yml.fundo.viewmodel.SharedViewModelFactory

class ResetPassword: Fragment(R.layout.reset_password) {
    lateinit var binding: ResetPasswordBinding
    lateinit var dialog: Dialog
    lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ResetPasswordBinding.bind(view)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.loading_screen)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]

        binding.resetButton.setOnClickListener {
            resetPassword(binding.resetEmail.text.toString())
        }

        binding.backButton.setOnClickListener {
           sharedViewModel.setGoToLoginPageStatus(true)
        }
    }

    fun resetPassword(email: String){
        if(Validator.forgotPasswordValidator(binding.resetEmail)){
            dialog.show()
            Authentication.resetPassword(email){
                Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
                sharedViewModel.setGoToLoginPageStatus(true)
                dialog.dismiss()
            }
        }
    }
}