package com.example.pedikura

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.pedikura.customers.CustomerViewModel
import com.example.pedikura.databinding.ActivityMainBinding
import com.example.pedikura.functions.SharedPreferenceFunctions


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var customerVM:CustomerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)

        customerVM = ViewModelProvider(this).get(CustomerViewModel::class.java)

        if (savedInstanceState==null){
            if (SharedPreferenceFunctions().getLoggedIn(this)){
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.customersFragment)
            }
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
            R.id.action_settings -> {
                if(SharedPreferenceFunctions().getLoggedIn(this)) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_global_settingsFragment)
                }
            return true}
            R.id.action_log -> {
                if(SharedPreferenceFunctions().getLoggedIn(this)) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_global_logFragment)
                }
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
        if (navigationController.currentDestination?.id == R.id.customerDetailFragment){
            customerVM.clearActualCustomer()
            //super.onBackPressed()
        }
        super.onBackPressed()
    }
}