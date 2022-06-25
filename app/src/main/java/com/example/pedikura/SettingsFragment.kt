package com.example.pedikura

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.pedikura.backup.BackupDialog


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
            setTitle(getString(R.string.setting_dialog_title))
            setMessage(getString(R.string.setting_dialog_message))
            setPositiveButton(getString(R.string.setting_dialog_positive)){_,_->
                BackupDialog().show(parentFragmentManager,"Backup Dialog")
            }
            setNegativeButton(getString(R.string.setting_dialog_negative)){_,_->

            }.create().show()
        }
    }


}


