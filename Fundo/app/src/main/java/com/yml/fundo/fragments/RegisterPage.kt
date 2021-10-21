package com.yml.fundo.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.yml.fundo.R
import com.yml.fundo.databinding.RegisterPageBinding
import com.yml.fundo.model.User
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.Database
import com.yml.fundo.util.Validator

class RegisterPage: Fragment(R.layout.register_page) {
    lateinit var binding: RegisterPageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterPageBinding.bind(view)

        binding.loginLink.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_view, LoginPage())
                commit()
            }
        }

        binding.registerButton.setOnClickListener {
            var name = binding.registerName
            var email = binding.registerEmail
            var password = binding.registerPassword
            var confirmPassword = binding.registerConfirmPassword
            var mobileNO = binding.registerMobile

            if(Validator.registrationValidation(name,email,password,confirmPassword,mobileNO)){
                val user = User(name.text.toString(),email.text.toString(),mobileNO.text.toString())
                Authentication.registerEmailPassword(email.text.toString(),password.text.toString()){firebaseUser ->
                    if(firebaseUser == null){
                        Toast.makeText(requireContext(),"Sign in unsuccessful",Toast.LENGTH_LONG).show()
                    }else{
                        Database.getDatabase(user){
                            if(!it){
                                Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG)
                            }else{
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
}