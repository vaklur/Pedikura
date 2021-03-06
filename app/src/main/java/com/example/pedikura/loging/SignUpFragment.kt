package com.example.pedikura.loging

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pedikura.R
import com.example.pedikura.databinding.FragmentSignUpBinding
import com.example.pedikura.volley_communication.CommunicationFunction


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding?=null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginText.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_logIn)
        }

        val comFunc = CommunicationFunction(requireContext())
        binding.buttonSignUp.setOnClickListener {
            Log.d("test","signup")
            val fullname = binding.fullname.text.toString()
            val email = binding.email.text.toString()
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            if (fullname!="" && email!="" && username!="" && password!="") {
                comFunc.signUpToServer(fullname, email, username, password, view, requireContext())
            }
            else {
                Toast.makeText(requireContext(),"Údaje musí být vyplněné!", Toast.LENGTH_LONG).show()
            }
        }
    }


}