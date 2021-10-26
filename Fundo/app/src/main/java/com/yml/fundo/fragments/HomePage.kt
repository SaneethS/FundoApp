package com.yml.fundo.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.homePageToolbar)
        setHasOptionsMenu(true)


        binding.welcomeText.text = "Welcome !!"

        binding.button.setOnClickListener {
            Authentication.logOut()
            sharedViewModel.setGoToLoginPageStatus(true)
        }

        binding.homePageToolbar.setNavigationOnClickListener {
            Toast.makeText(requireContext(),"Navigation button clicked",Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemView = item.itemId

        when(itemView){
            R.id.profile_icon -> Toast.makeText(requireContext(),"Profile menu clicked",Toast.LENGTH_LONG).show()
        }
        return false
    }
}

