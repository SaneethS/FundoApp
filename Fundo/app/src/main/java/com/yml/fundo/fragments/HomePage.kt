package com.yml.fundo.fragments

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.yml.fundo.R
import com.yml.fundo.databinding.HomePageBinding
import com.yml.fundo.service.Authentication
import com.yml.fundo.util.SharedPref
import com.yml.fundo.viewmodel.HomeViewModel
import com.yml.fundo.viewmodel.HomeViewModelFactory
import com.yml.fundo.viewmodel.SharedViewModel
import com.yml.fundo.viewmodel.SharedViewModelFactory

class HomePage:Fragment(R.layout.home_page) {
    lateinit var binding: HomePageBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var homeViewModel: HomeViewModel
    lateinit var alertDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomePageBinding.bind(view)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory())[HomeViewModel::class.java]

        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        setHasOptionsMenu(true)

        profilePage()

        binding.welcomeText.text = "Welcome!!"

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemView = item.itemId

        when(itemView){
            R.id.profile_icon -> showDialog()
        }
        return false
    }

    private fun showDialog(){
        alertDialog.show()
    }

    private fun dismissDialog(){
        alertDialog.dismiss()
    }

    private fun profilePage(){
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.profile_dialog,null)
        alertDialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        val logout:MaterialButton = dialogView.findViewById(R.id.dialog_logout)
        logout.setOnClickListener {
            homeViewModel.logoutFromHome()
            dismissDialog()
            sharedViewModel.setGoToLoginPageStatus(true)
        }

        val close:ImageView = dialogView.findViewById(R.id.close_icon)
        close.setOnClickListener {
            dismissDialog()
        }

        val name:TextView = dialogView.findViewById(R.id.dialog_name)
        val email:TextView = dialogView.findViewById(R.id.dialog_email)

        name.text = SharedPref.get("userName")
        email.text = SharedPref.get("userEmail")


    }


}

