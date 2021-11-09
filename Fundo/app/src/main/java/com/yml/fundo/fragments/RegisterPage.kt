package com.yml.fundo.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.databinding.RegisterPageBinding
import com.yml.fundo.model.User
import com.yml.fundo.util.Validator
import com.yml.fundo.viewmodel.RegisterViewModel
import com.yml.fundo.viewmodel.RegisterViewModelFactory
import com.yml.fundo.viewmodel.SharedViewModel
import com.yml.fundo.viewmodel.SharedViewModelFactory

class RegisterPage: Fragment(R.layout.register_page) {

    lateinit var sharedViewModel: SharedViewModel
    lateinit var registerViewModel: RegisterViewModel
    lateinit var loading: Dialog
    lateinit var binding: RegisterPageBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterPageBinding.bind(view)
        loading = Dialog(requireContext())
        loading.setContentView(R.layout.loading_screen)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]
        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())[RegisterViewModel::class.java]
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
        var name = binding.registerName
        var email = binding.registerEmail
        var password = binding.registerPassword
        var confirmPassword = binding.registerConfirmPassword
        var mobileNO = binding.registerMobile
        val user = User(name.text.toString(),email.text.toString(),mobileNO.text.toString())

        if(Validator.registrationValidation(name,email,password,confirmPassword,mobileNO,requireContext())){
            registerViewModel.registerNewUser(user, password.text.toString())
        }else{
            loading.dismiss()
        }
    }

    fun registerObserver(){
        registerViewModel.registerStatus.observe(viewLifecycleOwner){
            if(it){
                loading.dismiss()
                sharedViewModel.setGoToHomePageStatus(true)
            }else{
                loading.dismiss()
                Toast.makeText(requireContext(),getString(R.string.sign_up_unsuccessful_toast),Toast.LENGTH_LONG).show()
            }
        }
    }
}