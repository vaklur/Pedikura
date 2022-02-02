package com.example.pedikura.backup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.pedikura.DataBaseHandler
import com.example.pedikura.R
import com.example.pedikura.functions.SharedPreferenceFunctions
import com.example.pedikura.volley_communication.CommunicationFunction

class BackupDialog: DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.backup_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernameET = view.findViewById<EditText>(R.id.dialog_username_ET)
        usernameET.setText(SharedPreferenceFunctions().getUsername(requireContext()).toString())

        view.findViewById<Button>(R.id.download_backup_BTN).setOnClickListener {
            val username = usernameET.text.toString()
            val password = view.findViewById<EditText>(R.id.dialog_password_ET).text.toString()
            if (username!=""&& password!=""){
               downloadBackUp(username,password)
            }
            else {
                Toast.makeText(requireContext(),"Údaje musí být vyplněné!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun downloadBackUp(username:String,password:String){

        val comFunc = CommunicationFunction(requireContext())
        comFunc.getCustomersTableFromServer(username,password,requireContext(),this)
    }
}