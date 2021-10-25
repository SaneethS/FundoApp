package com.yml.fundo.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.yml.fundo.R
import com.yml.fundo.databinding.ResetPasswordBinding
import com.yml.fundo.service.Authentication
import com.yml.fundo.util.Validator

class ResetPassword: Fragment(R.layout.reset_password) {
    lateinit var binding: ResetPasswordBinding
    lateinit var dialog: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ResetPasswordBinding.bind(view)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.loading_screen)

        binding.resetButton.setOnClickListener {
            resetPassword(binding.resetEmail.text.toString())
        }

        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_view,LoginPage()).commit()
        }
    }

    fun resetPassword(email: String){
        if(Validator.forgotPasswordValidator(binding.resetEmail)){
            dialog.show()
            Authentication.resetPassword(email){
                Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_view,LoginPage()).commit()
                dialog.dismiss()
            }
        }
    }
}