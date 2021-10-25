package com.yml.fundo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.databinding.ActivityMainBinding
import com.yml.fundo.fragments.*
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.Database
import com.yml.fundo.viewmodel.SharedViewModel
import com.yml.fundo.viewmodel.SharedViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedViewModel = ViewModelProvider(this@MainActivity, SharedViewModelFactory())[SharedViewModel::class.java]
        observeNavigation()
        goToSplashScreen()
    }

    fun observeNavigation(){
        sharedViewModel.goToHomePageStatus.observe(this@MainActivity){
            if(it){
                goToHomePage()
            }
        }
        sharedViewModel.goToLoginPageStatus.observe(this@MainActivity){
            if(it){
                goToLoginPage()
            }
        }
        sharedViewModel.goToRegisterPageStatus.observe(this@MainActivity){
            if(it){
                goToRegisterPage()
            }
        }
        sharedViewModel.goToSplashScreenStatus.observe(this@MainActivity){
            if(it){
                goToSplashScreen()
            }
        }
        sharedViewModel.goToResetPasswordStatus.observe(this@MainActivity){
            if(it){
                goToResetPassword()
            }
        }

    }

    private fun goToResetPassword() {
        switchFragment(ResetPassword())
    }

    private fun goToSplashScreen() {
        switchFragment(SplashScreen())
    }

    private fun goToRegisterPage() {
        switchFragment(RegisterPage())
    }

    private fun goToLoginPage() {
        switchFragment(LoginPage())
    }

    private fun goToHomePage() {
        switchFragment(HomePage())
    }

    fun switchFragment(fragment: Fragment){
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_view,fragment)
        fragmentTransaction.commit()
    }



}