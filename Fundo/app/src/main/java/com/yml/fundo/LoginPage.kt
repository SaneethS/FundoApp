package com.yml.fundo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yml.fundo.databinding.LoginPageBinding

class LoginPage:Fragment(R.layout.login_page) {
    lateinit var binding:LoginPageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginPageBinding.bind(view)
    }


}