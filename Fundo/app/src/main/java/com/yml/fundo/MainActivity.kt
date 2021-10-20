package com.yml.fundo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yml.fundo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        switchFragment(LoginPage())

        binding.registerLink.setOnClickListener {
            if(binding.registerLink.text.equals("Register")) {
                switchFragment(RegisterPage())
                binding.askText.setText("Already have an account?")
                binding.registerLink.setText("Login")
            }else{
                switchFragment(LoginPage())
                binding.askText.setText(R.string.don_t_have_account)
                binding.registerLink.setText(R.string.register)
            }
//            binding.registerLink.setOnClickListener {
//                val intent = Intent(this,MainActivity::class.java)
//                startActivity(intent)
//            }
        }
    }

    fun switchFragment(fragment: Fragment){
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_view,fragment)
        fragmentTransaction.commit()
    }

}