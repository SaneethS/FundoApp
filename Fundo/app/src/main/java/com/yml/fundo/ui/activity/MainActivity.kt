package com.yml.fundo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.databinding.ActivityMainBinding
import com.yml.fundo.ui.home.HomePage
import com.yml.fundo.ui.login.LoginPage
import com.yml.fundo.ui.note.NotePage
import com.yml.fundo.ui.register.RegisterPage
import com.yml.fundo.ui.reset.ResetPassword
import com.yml.fundo.ui.splash.SplashScreen
import com.yml.fundo.common.SharedPref
import com.yml.fundo.data.service.DatabaseService

class MainActivity : AppCompatActivity() {
    lateinit var sharedViewModel: SharedViewModel
    lateinit var toggle:ActionBarDrawerToggle

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedViewModel = ViewModelProvider(this@MainActivity)[SharedViewModel::class.java]
        setSupportActionBar(binding.homePageToolbar)
        SharedPref.initSharedPref(this)
        DatabaseService.initSqliteDBService(this)
        observeNavigation()
        if(savedInstanceState == null){
            goToSplashScreen()
        }
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
        sharedViewModel.goToNotePageStatus.observe(this@MainActivity){
            if(it){
                goToNotePage()
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

    private fun goToNotePage(){
        switchFragment(NotePage())
    }

    fun switchFragment(fragment: Fragment){
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_view,fragment)
        fragmentTransaction.commit()
    }

    fun navigationDrawer(){
        toggle = object:ActionBarDrawerToggle(this,binding.drawerLayout,binding.homePageToolbar,R.string.open,R.string.close){
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                val header = binding.navigationDrawer.getHeaderView(0)
                val headerText:TextView = header.findViewById(R.id.drawer_name_text)
                headerText.text = SharedPref.get("userName")
            }
        }
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