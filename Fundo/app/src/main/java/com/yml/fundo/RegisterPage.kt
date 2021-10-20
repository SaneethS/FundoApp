package com.yml.fundo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yml.fundo.databinding.RegisterPageBinding

class RegisterPage: Fragment(R.layout.register_page) {
    lateinit var binding: RegisterPageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}