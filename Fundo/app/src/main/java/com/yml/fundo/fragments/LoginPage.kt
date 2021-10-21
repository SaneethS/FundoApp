package com.yml.fundo.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.yml.fundo.R
import com.yml.fundo.databinding.LoginPageBinding
import com.yml.fundo.service.Authentication
import com.yml.fundo.util.Validator

class LoginPage:Fragment(R.layout.login_page) {
    lateinit var binding:LoginPageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginPageBinding.bind(view)

        binding.registerLink.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_view, RegisterPage())
                commit()
            }
        }

        binding.loginButton.setOnClickListener {
            var email = binding.usernameField
            var password = binding.passwordField
            if(Validator.loginValidation(email,password)){
                Authentication.loginEmailPassword(email.text.toString(),password.text.toString()){
                    if(it == null){
                        Toast.makeText(requireContext(),"Log-in Failed",Toast.LENGTH_LONG)
                    }else{
                        Toast.makeText(requireContext(),"Log-in Successful",Toast.LENGTH_LONG)
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