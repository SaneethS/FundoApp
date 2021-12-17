package com.yml.fundo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.yml.fundo.R
import com.yml.fundo.networking.users.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val apiButton = findViewById<Button>(R.id.button_call_api)
        val userService = UserService()
        val textViewApi = findViewById<TextView>(R.id.textView_api_response)
        apiButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
//                val users = userService.getUsers()
                withContext(Dispatchers.Main) {
//                    textViewApi.text = users.toString()
                }
            }
        }
    }
}