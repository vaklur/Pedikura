package com.example.pedikura

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.pedikura.databinding.ActivityMainBinding
import com.example.pedikura.volley_communication.CommunicationFunction
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.ceil


class MainActivity : AppCompatActivity() {

    val firstCallTime = ceil(System.currentTimeMillis()/60_000.0).toLong()*60


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)

        lifecycleScope.launch { delay(firstCallTime - System.currentTimeMillis())
            while (true){
                launch {
                    Log.d("test","minute")
                    val comFunc = CommunicationFunction()
                    comFunc.connectionToServer(applicationContext)
                }
                delay(60_000)
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
            R.id.action_settings -> {Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.action_global_settingsFragment)
            return true}
            else -> super.onOptionsItemSelected(item)
        }
    }




}