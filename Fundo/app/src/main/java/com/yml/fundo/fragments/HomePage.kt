package com.yml.fundo.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yml.fundo.R
import com.yml.fundo.databinding.HomePageBinding
import com.yml.fundo.service.Authentication

class HomePage:Fragment(R.layout.home_page) {
    lateinit var binding: HomePageBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomePageBinding.bind(view)

        var name = arguments?.get("name").toString()

        binding.welcomeText.text = "Welcome $name!!"

        binding.button.setOnClickListener {
            Authentication.logOut()
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_view, LoginPage())
                commit()
            }
        }
    }
}