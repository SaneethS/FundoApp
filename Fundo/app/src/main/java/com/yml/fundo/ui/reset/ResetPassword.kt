package com.yml.fundo.ui.reset

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.databinding.ResetPasswordBinding
import com.yml.fundo.common.Validator
import com.yml.fundo.ui.activity.SharedViewModel

class ResetPassword: Fragment(R.layout.reset_password) {
    lateinit var binding: ResetPasswordBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var resetPasswordViewModel: ResetPasswordViewModel

    companion object{
        lateinit var loading: Dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ResetPasswordBinding.bind(view)
        loading = Dialog(requireContext())
        loading.setContentView(R.layout.loading_screen)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        resetPasswordViewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]

        binding.resetButton.setOnClickListener {
            loading.show()
            resetPassword(binding.resetEmail.text.toString())
        }

        binding.backButton.setOnClickListener {
           sharedViewModel.setGoToLoginPageStatus(true)
        }

        resetPasswordObserver()
    }

    fun resetPassword(email: String){
        if(Validator.forgotPasswordValidator(binding.resetEmail, requireContext())){
            resetPasswordViewModel.resetPasswordOfUser(email)

        }
    }

    fun resetPasswordObserver(){
        resetPasswordViewModel.resetPasswordStatus.observe(viewLifecycleOwner){
            loading.dismiss()
            Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
            sharedViewModel.setGoToLoginPageStatus(true)
        }
    }
}