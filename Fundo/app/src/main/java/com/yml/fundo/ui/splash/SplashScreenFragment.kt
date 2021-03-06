package com.yml.fundo.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.databinding.SplashScreenBinding
import com.yml.fundo.auth.Authentication
import com.yml.fundo.ui.SharedViewModel

class SplashScreenFragment : Fragment(R.layout.splash_screen) {
    private lateinit var binding: SplashScreenBinding
    private lateinit var sharedViewModel: SharedViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SplashScreenBinding.bind(view)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        binding.splashImage.alpha = 0f
        binding.splashImage.animate().setDuration(1500).alpha(1f).withEndAction {
            val user = Authentication.getCurrentUser()
            if (user == null) {
                sharedViewModel.setGoToLoginPageStatus(true)
            } else {
                sharedViewModel.setGoToHomePageStatus(true)
            }
        }
    }
}
