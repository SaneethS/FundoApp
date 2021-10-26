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
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.Database
import com.yml.fundo.util.Util
import com.yml.fundo.util.Validator
import com.yml.fundo.viewmodel.SharedViewModel
import com.yml.fundo.viewmodel.SharedViewModelFactory

class RegisterPage: Fragment(R.layout.register_page) {
    lateinit var binding: RegisterPageBinding
    lateinit var sharedViewModel: SharedViewModel

    companion object{
        lateinit var loading: Dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterPageBinding.bind(view)
        loading = Dialog(requireContext())
        loading.setContentView(R.layout.loading_screen)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]

        binding.loginLink.setOnClickListener {
            sharedViewModel.setGoToLoginPageStatus(true)
        }

        binding.registerButton.setOnClickListener {
            loading.show()
            register()
        }

        sharedViewModel.registerStatus.observe(viewLifecycleOwner){
            var name = binding.registerName
            var email = binding.registerEmail
            var mobileNO = binding.registerMobile
            val user = User(name.text.toString(),email.text.toString(),mobileNO.text.toString())
            if(it?.loginStatus == true){
                loading.dismiss()
                Database.setToDatabase(user){
                    if(!it){
                        loading.dismiss()
                        Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG)
                    }else{
                        sharedViewModel.setGoToHomePageStatus(true)
                    }
                }
            }else{
                loading.dismiss()
                Toast.makeText(requireContext(),"Sign up unsuccessful",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun register() {
        var name = binding.registerName
        var email = binding.registerEmail
        var password = binding.registerPassword
        var confirmPassword = binding.registerConfirmPassword
        var mobileNO = binding.registerMobile

        if(Validator.registrationValidation(name,email,password,confirmPassword,mobileNO)){
            sharedViewModel.registerNewUser(email.text.toString(), password.text.toString())
        }
    }
}