package com.yml.fundo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.databinding.ActivityMainBinding
import com.yml.fundo.fragments.*
import com.yml.fundo.model.UserDetails
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.Database
import com.yml.fundo.util.Util
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
        sharedViewModel.setGoToSplashScreenStatus(true)
        loginObserver()
        registerObserver()
        resetPasswordObserver()

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

    fun loginObserver(){
        sharedViewModel.loginStatus.observe(this@MainActivity) {
            if(it?.loginStatus == true){
                LoginPage.loading.dismiss()
                Database.getFromDatabase{
                    var userDetails = Util.createUser(it)
                    Toast.makeText(this,userDetails.toString(),Toast.LENGTH_LONG).show()
                }
                sharedViewModel.setGoToHomePageStatus(true)
            }else{
                LoginPage.loading.dismiss()
                Toast.makeText(this,"Log-in failed", Toast.LENGTH_LONG).show()
            }
        }

        sharedViewModel.facebookLoginStatus.observe(this@MainActivity){
            if(it?.loginStatus == true){
                LoginPage.loading.dismiss()
                Database.getFromDatabase{
                    var userDetails = Util.createUser(it)
                    Toast.makeText(this,userDetails.toString(),Toast.LENGTH_LONG).show()
                }
                sharedViewModel.setGoToHomePageStatus(true)

            }else{
                LoginPage.loading.dismiss()
                Toast.makeText(this,"Facebook Log-in failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun registerObserver(){
        sharedViewModel.registerStatus.observe(this@MainActivity){
            var name = RegisterPage.binding.registerName
            var email = RegisterPage.binding.registerEmail
            var mobileNO = RegisterPage.binding.registerMobile
            val userDetails = UserDetails(name.text.toString(),email.text.toString(),mobileNO.text.toString())
            if(it?.loginStatus == true){
                RegisterPage.loading.dismiss()
                Database.setToDatabase(userDetails){
                    if(!it){
                        RegisterPage.loading.dismiss()
                        Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG)
                    }else{
                        sharedViewModel.setGoToHomePageStatus(true)
                    }
                }
            }else{
                RegisterPage.loading.dismiss()
                Toast.makeText(this,"Sign up unsuccessful",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun resetPasswordObserver(){
        sharedViewModel.resetPasswordStatus.observe(this@MainActivity){
            ResetPassword.loading.dismiss()
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
            sharedViewModel.setGoToLoginPageStatus(true)
        }
    }


}