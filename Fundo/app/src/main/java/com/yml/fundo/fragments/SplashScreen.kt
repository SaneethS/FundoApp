package com.yml.fundo.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yml.fundo.R
import com.yml.fundo.activity.MainActivity
import com.yml.fundo.databinding.SplashScreenBinding

class SplashScreen:Fragment(R.layout.splash_screen) {
    lateinit var binding: SplashScreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SplashScreenBinding.bind(view)

        binding.splashImage.alpha = 0f
        binding.splashImage.animate().setDuration(1500).alpha(1f).withEndAction {
            val intent =Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
        }
    }
}