package com.yml.workshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchFragment(FragmentSquares())
    }

    private fun switchFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_view,fragment).commit()
    }
}