package com.yml.fundo.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.common.SharedPref
import com.yml.fundo.databinding.ActivityMainBinding
import com.yml.fundo.ui.home.HomePage
import com.yml.fundo.ui.login.LoginPage
import com.yml.fundo.ui.note.NotePage
import com.yml.fundo.ui.register.RegisterPage
import com.yml.fundo.ui.reset.ResetPassword
import com.yml.fundo.ui.splash.SplashScreen

class MainActivity : AppCompatActivity() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedViewModel = ViewModelProvider(this@MainActivity)[SharedViewModel::class.java]
        setSupportActionBar(binding.homePageToolbar)
        SharedPref.initSharedPref(this)
        observeNavigation()
        if (savedInstanceState == null) {
            goToSplashScreen()
        }
        navigationDrawer()
    }

    private fun observeNavigation() {
        sharedViewModel.goToHomePageStatus.observe(this@MainActivity) {
            if (it) {
                goToHomePage()
            }
        }
        sharedViewModel.goToLoginPageStatus.observe(this@MainActivity) {
            if (it) {
                goToLoginPage()
            }
        }
        sharedViewModel.goToRegisterPageStatus.observe(this@MainActivity) {
            if (it) {
                goToRegisterPage()
            }
        }
        sharedViewModel.goToSplashScreenStatus.observe(this@MainActivity) {
            if (it) {
                goToSplashScreen()
            }
        }
        sharedViewModel.goToResetPasswordStatus.observe(this@MainActivity) {
            if (it) {
                goToResetPassword()
            }
        }
        sharedViewModel.goToNotePageStatus.observe(this@MainActivity) {
            if (it) {
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

    private fun goToNotePage() {
        switchFragment(NotePage())
    }

    private fun switchFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_view, fragment)
        fragmentTransaction.commit()
    }

    private fun navigationDrawer() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.homePageToolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()
        val navNotes = binding.navigationDrawer.menu.getItem(0)
        navNotes.isChecked = true

        binding.navigationDrawer.setNavigationItemSelectedListener {
            navNotes.isChecked = false
            when (it.itemId) {
                R.id.notes -> sharedViewModel.setGoToHomePageStatus(true)
                R.id.reminders -> Toast.makeText(
                    this, "Reminder selected",
                    Toast.LENGTH_LONG
                ).show()
            }
            it.isCheckable = true
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}