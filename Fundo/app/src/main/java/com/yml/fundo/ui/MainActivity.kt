package com.yml.fundo.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.common.*
import com.yml.fundo.databinding.ActivityMainBinding
import com.yml.fundo.ui.home.HomeFragment
import com.yml.fundo.ui.label.LabelCreateFragment
import com.yml.fundo.ui.label.LabelCreateFragment.Companion.ADD_MODE
import com.yml.fundo.ui.label.LabelCreateFragment.Companion.SELECT_MODE
import com.yml.fundo.ui.login.LoginFragment
import com.yml.fundo.ui.note.NoteFragment
import com.yml.fundo.ui.register.RegisterFragment
import com.yml.fundo.ui.reset.ResetPasswordFragment
import com.yml.fundo.ui.splash.SplashScreenFragment
import com.yml.fundo.ui.wrapper.Note

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
        setNotificationNavigationPage()
        sharedViewModel.setTopicToSubscribe()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setNotificationNavigationPage()
    }

    private fun setNotificationNavigationPage() {
        when(intent?.getStringExtra("destination")){
            "home" -> {
                if(sharedViewModel.checkUser()) {
                    val notes = intent.getSerializableExtra("notes") as Note
                    goToExistingNotePage(notes)
                }
            }
        }
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

        sharedViewModel.goToLabelCreateStatus.observe(this@MainActivity) {
            if (it) {
                goToLabelCreate()
            }
        }

        sharedViewModel.goToArchiveNotePageStatus.observe(this@MainActivity) {
            if(it) {
                goToArchivedNotePage()
            }
        }

        sharedViewModel.goToReminderNotePageStatus.observe(this@MainActivity) {
            if(it) {
                goToReminderNotePage()
            }
        }

        sharedViewModel.goToExistNotePageStatus.observe(this@MainActivity) {
            goToExistingNotePage(it)
        }

    }



    private fun goToReminderNotePage() {
        val bundle = Bundle()
        val homepage = HomeFragment()
        bundle.putString(TYPE, REMINDER)
        homepage.arguments = bundle
        switchFragment(homepage)
    }

    private fun goToArchivedNotePage() {
        val bundle = Bundle()
        val homepage = HomeFragment()
        bundle.putString(TYPE, ARCHIVE)
        homepage.arguments = bundle
        switchFragment(homepage)
    }

    private fun goToResetPassword() {
        switchFragment(ResetPasswordFragment())
    }

    private fun goToSplashScreen() {
        switchFragment(SplashScreenFragment())
    }

    private fun goToRegisterPage() {
        switchFragment(RegisterFragment())
    }

    private fun goToLoginPage() {
        switchFragment(LoginFragment())
    }

    private fun goToHomePage() {
        val bundle = Bundle()
        val homepage = HomeFragment()
        bundle.putString(TYPE, HOME)
        homepage.arguments = bundle
        switchFragment(homepage)
    }

    private fun goToNotePage() {
        switchFragment(NoteFragment())
    }

    private fun goToExistingNotePage(note: Note) {
        val notePage = NoteFragment()
        val bundle = Util.setToNotesBundle(note)
        notePage.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_view, notePage).commit()
    }

    private fun goToLabelCreate() {
        val labelPage = LabelCreateFragment()
        val bundle = Bundle()
        bundle.putInt("mode",ADD_MODE)
        labelPage.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment_view, labelPage).
        addToBackStack(null).commit()
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_view, fragment).commit()
    }

    private fun navigationDrawer() {
        toggle = ActionBarDrawerToggle(
            this@MainActivity,
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
                R.id.reminders -> sharedViewModel.setGoToReminderNotePageStatus(true)
                R.id.labels -> sharedViewModel.setGoToLabelCreateStatus(true)
                R.id.archive -> sharedViewModel.setGoToArchivedNotePageStatus(true)
            }
            it.isCheckable = true
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}