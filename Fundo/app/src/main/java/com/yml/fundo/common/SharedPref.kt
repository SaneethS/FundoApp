package com.yml.fundo.common

import android.content.Context
import android.content.SharedPreferences


object SharedPref {

    private lateinit var sharedPreferences: SharedPreferences

    fun initSharedPref(context: Context) {
        sharedPreferences =
            context.getSharedPreferences("FundoSharedPreference", Context.MODE_PRIVATE)
    }

    fun addString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun addUid(value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong("uid", value)
        editor.apply()
    }

    fun getId(): Long {
        return sharedPreferences.getLong("uid", 0L)
    }

    fun get(key: String): String? = sharedPreferences.getString(key, key)

    fun clearAll() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun removeString(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()

    }
}