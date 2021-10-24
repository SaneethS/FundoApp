package com.yml.fundo.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yml.fundo.R
import com.yml.fundo.activity.MainActivity
import com.yml.fundo.databinding.SplashScreenBinding
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.Database

class SplashScreen:Fragment(R.layout.splash_screen) {
    lateinit var binding: SplashScreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SplashScreenBinding.bind(view)

        binding.splashImage.alpha = 0f
        binding.splashImage.animate().setDuration(1500).alpha(1f).withEndAction {
            val user = Authentication.getCurrentUser()
            if (user != null) {
                Database.getFromDatabase(user.uid) { status, bundle ->
                    var home = HomePage()
                    home.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_view, home)
                        commit()
                    }
                }
            } else {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_view, LoginPage())
                    commit()
                }
            }
        }
    }
}
