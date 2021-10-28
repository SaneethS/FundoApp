package com.yml.fundo.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.yml.fundo.R
import com.yml.fundo.databinding.LoginPageBinding
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.Database
import com.yml.fundo.util.Util
import com.yml.fundo.util.Validator
import com.yml.fundo.viewmodel.LoginViewModel
import com.yml.fundo.viewmodel.LoginViewModelFactory
import com.yml.fundo.viewmodel.SharedViewModel
import com.yml.fundo.viewmodel.SharedViewModelFactory

class LoginPage:Fragment(R.layout.login_page) {
    lateinit var binding:LoginPageBinding
    lateinit var callbackManager: CallbackManager
    lateinit var sharedViewModel: SharedViewModel
    lateinit var loginViewModel: LoginViewModel

    companion object{
        lateinit var loading:Dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginPageBinding.bind(view)
        loading = Dialog(requireContext())
        loading.setContentView(R.layout.loading_screen)
        callbackManager = CallbackManager.Factory.create()
        sharedViewModel = ViewModelProvider(requireActivity(),SharedViewModelFactory())[SharedViewModel::class.java]
        loginViewModel = ViewModelProvider(this,LoginViewModelFactory())[LoginViewModel::class.java]
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        binding.registerLink.setOnClickListener {
            sharedViewModel.setGoToRegisterPageStatus(true)
        }

        binding.loginButton.setOnClickListener {
            loading.show()
            login()
        }

        binding.facebookLogin.setOnClickListener{
            loading.show()
            facbookLogin()
        }

        binding.forgotPasswordLink.setOnClickListener {
            sharedViewModel.setGoToResetPasswordStatus(true)
        }

        loginObserver()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode,resultCode,data)
    }

    private fun facbookLogin() {
        var fbLogin = binding.facebookLogin
        fbLogin.setReadPermissions("email","public_profile")
        fbLogin.registerCallback(callbackManager,object : FacebookCallback<LoginResult>{
            override fun onCancel() {
                loading.dismiss()
                Toast.makeText(requireContext(),"Canceled",Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException) {
                loading.dismiss()
                Toast.makeText(requireContext(),"error occurred",Toast.LENGTH_LONG).show()
            }

            override fun onSuccess(result: LoginResult) {
                loginViewModel.facebookLoginWithUser(result.accessToken)

            }

        })
    }

    private fun login() {
        var email = binding.usernameField
        var password = binding.passwordField
        if(Validator.loginValidation(email,password)){
            loginViewModel.loginWithEmailAndPassword(email.text.toString(),password.text.toString())
        }else{
            loading.dismiss()
        }
    }

    fun loginObserver(){
        loginViewModel.loginStatus.observe(viewLifecycleOwner) {
            if(it?.loginStatus == true){
                loading.dismiss()
                sharedViewModel.setGoToHomePageStatus(true)

            }else{
                loading.dismiss()
                Toast.makeText(requireContext(),"Log-in failed", Toast.LENGTH_LONG).show()
            }
        }

        loginViewModel.facebookLoginStatus.observe(viewLifecycleOwner){
            if(it?.loginStatus == true){
                loading.dismiss()
                sharedViewModel.setGoToHomePageStatus(true)

            }else{
                loading.dismiss()
                Toast.makeText(requireContext(),"Facebook Log-in failed", Toast.LENGTH_LONG).show()
            }
        }
    }
}

