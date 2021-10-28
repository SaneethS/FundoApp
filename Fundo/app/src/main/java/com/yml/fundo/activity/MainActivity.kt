package com.yml.fundo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.databinding.ActivityMainBinding
import com.yml.fundo.fragments.*
import com.yml.fundo.model.UserDetails
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.Database
import com.yml.fundo.util.SharedPref
import com.yml.fundo.util.Util
import com.yml.fundo.viewmodel.SharedViewModel
import com.yml.fundo.viewmodel.SharedViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var sharedViewModel: SharedViewModel
    lateinit var toggle:ActionBarDrawerToggle

    companion object{
        lateinit var binding: ActivityMainBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedViewModel = ViewModelProvider(this@MainActivity, SharedViewModelFactory())[SharedViewModel::class.java]
        setSupportActionBar(binding.homePageToolbar)
        SharedPref.initSharedPref(this)
        observeNavigation()
        sharedViewModel.setGoToSplashScreenStatus(true)
        navigationDrawer()

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

    fun navigationDrawer(){
        toggle = ActionBarDrawerToggle(this,binding.drawerLayout,binding.homePageToolbar,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()



        binding.navigationDrawer.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.notes -> Toast.makeText(this,"Notes selected",Toast.LENGTH_LONG).show()
                R.id.reminders-> Toast.makeText(this,"Reminder selected",Toast.LENGTH_LONG).show()
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }


}