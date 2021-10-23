package com.yml.fundo.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.yml.fundo.R
import com.yml.fundo.databinding.LoginPageBinding
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.Database
import com.yml.fundo.util.Util
import com.yml.fundo.util.Validator

class LoginPage:Fragment(R.layout.login_page) {
    lateinit var binding:LoginPageBinding
    lateinit var loading:Dialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginPageBinding.bind(view)
        loading = Dialog(requireContext())
        loading.setContentView(R.layout.loading_screen)

        binding.registerLink.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_view, RegisterPage())
                commit()
            }
        }

        binding.loginButton.setOnClickListener {
            loading.show()
            login()
        }
    }

    private fun login() {
        var email = binding.usernameField
        var password = binding.passwordField
        if(Validator.loginValidation(email,password)){
            Authentication.loginEmailPassword(email.text.toString(),password.text.toString()){status,user ->
                if(status){
                    Database.getFromDatabase(user!!.uid){status,bundle ->
                        if(status){
                            Toast.makeText(requireContext(),"Log-in Successful",Toast.LENGTH_LONG).show()
                            var home = HomePage()
                            home.arguments = bundle
                            loading.dismiss()
                            requireActivity().supportFragmentManager.beginTransaction().apply {
                                replace(R.id.fragment_view,HomePage())
                                commit()
                            }
                        }else{
                            loading.dismiss()
                            Toast.makeText(requireContext(),"read failed",Toast.LENGTH_LONG).show()
                        }
                    }

                }else{
                    loading.dismiss()
                    Toast.makeText(requireContext(),"Log-in failed",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}