package com.yml.fundo.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.databinding.HomePageBinding
import com.yml.fundo.service.Authentication
import com.yml.fundo.viewmodel.SharedViewModel
import com.yml.fundo.viewmodel.SharedViewModelFactory

class HomePage:Fragment(R.layout.home_page) {
    lateinit var binding: HomePageBinding
    lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomePageBinding.bind(view)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]

        var name = arguments?.get("name").toString()

        binding.welcomeText.text = "Welcome !!"

        binding.button.setOnClickListener {
            Authentication.logOut()
            sharedViewModel.setGoToLoginPageStatus(true)
        }
    }
}