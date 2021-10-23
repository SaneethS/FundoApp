package com.yml.fundo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.yml.fundo.R
import com.yml.fundo.databinding.ActivityMainBinding
import com.yml.fundo.fragments.HomePage
import com.yml.fundo.fragments.LoginPage
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.Database

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkUserLogin()
        switchFragment(LoginPage())

    }

    fun switchFragment(fragment: Fragment){
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_view,fragment)
        fragmentTransaction.commit()
    }

    fun checkUserLogin(){
        val user = Authentication.getCurrentUser()
        if(user!=null){
            Database.getFromDatabase(user.uid){status, bundle->
                var home = HomePage()
                home.arguments = bundle
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_view, HomePage())
                    commit()
                }
            }
        }
    }

}