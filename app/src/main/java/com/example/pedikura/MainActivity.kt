package com.example.pedikura

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.pedikura.databinding.ActivityMainBinding
import com.example.pedikura.functions.SharedPreferenceFunctions


class MainActivity : AppCompatActivity() {




    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)

        if (SharedPreferenceFunctions().getLoggedIn(this)){
            Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.customersFragment)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            // Go to settings
            R.id.action_settings -> {Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.action_global_settingsFragment)
            return true}
            R.id.action_logout ->{Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.logInFragment)
                SharedPreferenceFunctions().saveSP("",false,this)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {

        val navigationController = Navigation.findNavController(this,R.id.nav_host_fragment)
        if (navigationController.currentDestination?.id == R.id.customersFragment) {
            finish()
        }
        if (navigationController.currentDestination?.id == R.id.logInFragment) {
            finish()
        }
        else{
            super.onBackPressed()
        }

    }




}