package com.yml.fundo.ui.login

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
import com.yml.fundo.common.Validator
import com.yml.fundo.ui.SharedViewModel

class LoginFragment : Fragment(R.layout.login_page) {
    private lateinit var binding: LoginPageBinding
    private lateinit var callbackManager: CallbackManager
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var loginViewModel: LoginViewModel

    companion object {
        private lateinit var loading: Dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginPageBinding.bind(view)
        loading = Dialog(requireContext())
        loading.setContentView(R.layout.loading_screen)
        callbackManager = CallbackManager.Factory.create()
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        binding.registerLink.setOnClickListener {
            sharedViewModel.setGoToRegisterPageStatus(true)
        }

        binding.loginButton.setOnClickListener {
            loading.show()
            login()
        }

        binding.facebookLogin.setOnClickListener {
            loading.show()
            facebookLogin()
        }

        binding.forgotPasswordLink.setOnClickListener {
            sharedViewModel.setGoToResetPasswordStatus(true)
        }

        loginObserver()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun facebookLogin() {
        val fbLogin = binding.facebookLogin
        fbLogin.setReadPermissions("email", "public_profile")
        fbLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                loading.dismiss()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.cancelled_toast),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onError(error: FacebookException) {
                loading.dismiss()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_occured_toast),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onSuccess(result: LoginResult) {
                loginViewModel.facebookLoginWithUser(requireContext(), result.accessToken)

            }

        })
    }

    private fun login() {
        val email = binding.usernameField
        val password = binding.passwordField
        if (Validator.loginValidation(email, password, requireContext())) {
            loginViewModel.loginWithEmailAndPassword(
                requireContext(),
                email.text.toString(),
                password.text.toString()
            )
        } else {
            loading.dismiss()
        }
    }

    private fun loginObserver() {
        loginViewModel.loginStatus.observe(viewLifecycleOwner) {
            if (it?.loginStatus == true) {
                loading.dismiss()
                sharedViewModel.setGoToHomePageStatus(true)

            } else {
                loading.dismiss()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.login_failed_toast),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        loginViewModel.facebookLoginStatus.observe(viewLifecycleOwner) {
            if (it?.loginStatus == true) {
                loading.dismiss()
                sharedViewModel.setGoToHomePageStatus(true)

            } else {
                loading.dismiss()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.facebook_login_failed_toast),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

