package com.yml.fundo.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.yml.fundo.R
import com.yml.fundo.databinding.RegisterPageBinding
import com.yml.fundo.model.User
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.Database
import com.yml.fundo.util.Util
import com.yml.fundo.util.Validator

class RegisterPage: Fragment(R.layout.register_page) {
    lateinit var binding: RegisterPageBinding
    lateinit var loading: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterPageBinding.bind(view)
        loading = Dialog(requireContext())
        loading.setContentView(R.layout.loading_screen)

        binding.loginLink.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_view, LoginPage())
                commit()
            }
        }

        binding.registerButton.setOnClickListener {
            loading.show()
            register()
        }
    }

    private fun register() {
        var name = binding.registerName
        var email = binding.registerEmail
        var password = binding.registerPassword
        var confirmPassword = binding.registerConfirmPassword
        var mobileNO = binding.registerMobile
        var bundle: Bundle

        if(Validator.registrationValidation(name,email,password,confirmPassword,mobileNO)){
            val user = User(name.text.toString(),email.text.toString(),mobileNO.text.toString())
            Authentication.registerEmailPassword(email.text.toString(),password.text.toString()){status,firebaseUser ->
                if(!status){
                    loading.dismiss()
                    Toast.makeText(requireContext(),"Sign in unsuccessful",Toast.LENGTH_LONG).show()
                }else{
                    Database.setToDatabase(user){
                        if(!it){
                            loading.dismiss()
                            Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG)
                        }else{
                            bundle = Util.createUser(user)
                            var home = HomePage()
                            home.arguments = bundle
                            loading.dismiss()
                            requireActivity().supportFragmentManager.beginTransaction().apply {
                                replace(R.id.fragment_view,HomePage())
                                commit()
                            }
                        }
                    }
                }
            }
        }
    }
}