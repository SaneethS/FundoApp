package com.yml.fundo.ui.register

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.databinding.RegisterPageBinding
import com.yml.fundo.ui.wrapper.User
import com.yml.fundo.common.Validator
import com.yml.fundo.ui.SharedViewModel

class RegisterPage : Fragment(R.layout.register_page) {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var loading: Dialog
    private lateinit var binding: RegisterPageBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterPageBinding.bind(view)
        loading = Dialog(requireContext())
        loading.setContentView(R.layout.loading_screen)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding.loginLink.setOnClickListener {
            sharedViewModel.setGoToLoginPageStatus(true)
        }

        binding.registerButton.setOnClickListener {
            loading.show()
            register()
        }

        registerObserver()

    }

    private fun register() {
        val name = binding.registerName
        val email = binding.registerEmail
        val password = binding.registerPassword
        val confirmPassword = binding.registerConfirmPassword
        val mobileNO = binding.registerMobile
        val user = User(name.text.toString(), email.text.toString(), mobileNO.text.toString())

        if (Validator.registrationValidation(
                name,
                email,
                password,
                confirmPassword,
                mobileNO,
                requireContext()
            )
        ) {
            registerViewModel.registerNewUser(requireContext(), user, password.text.toString())
        } else {
            loading.dismiss()
        }
    }

    private fun registerObserver() {
        registerViewModel.registerStatus.observe(viewLifecycleOwner) {
            if (it) {
                loading.dismiss()
                sharedViewModel.setGoToHomePageStatus(true)
            } else {
                loading.dismiss()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.sign_up_unsuccessful_toast),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}