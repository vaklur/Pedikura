package com.example.pedikura

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.pedikura.backup.BackupDialog
import com.example.pedikura.functions.SharedPreferenceFunctions
import com.example.pedikura.volley_communication.CommunicationFunction


class SettingsFragment : Fragment() {



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.upload_test_BTN).setOnClickListener {
            showWarningDialog()

        }
    }

    private fun showWarningDialog(){
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.apply {
            setTitle("Nahrání dat ze zálohy na serveru")
            setMessage("Opravdu chcete nahrát data ze zálohy na serveru?\n" +
                    "Přijdete tak o všechny zákazníky uložené v aplikaci.")
            setPositiveButton("Ano"){_,_->
                BackupDialog().show(parentFragmentManager,"Backup Dialog")
            }
            setNegativeButton("Ne"){_,_->

            }.create().show()
        }
    }


}


