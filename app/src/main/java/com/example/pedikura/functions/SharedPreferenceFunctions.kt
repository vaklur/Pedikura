package com.example.pedikura.functions

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceFunctions {

    fun saveSP(username:String,loggedIn:Boolean,context: Context){
        val sharedPrefFile = "pedicure"
        val sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString("username",username)
        editor.putBoolean("loggedIn",loggedIn)
        editor.apply()
        editor.commit()
    }

    fun getUsername (context: Context): String? {
        val sharedPrefFile = "pedicure"
        val sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getString("username","default")
    }

    fun getLoggedIn (context: Context):Boolean{
        val sharedPrefFile = "pedicure"
        val sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("loggedIn",false)
    }
}
