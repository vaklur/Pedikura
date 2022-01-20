package com.example.pedikura.loging

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pedikura.R
import com.example.pedikura.databinding.FragmentLogInBinding
import com.example.pedikura.volley_communication.CommunicationFunction


class LogInFragment : Fragment() {
    private var _binding: FragmentLogInBinding?=null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.signUpText.setOnClickListener {
            findNavController().navigate(R.id.action_logIn_to_signUp)
        }
        binding.buttonLogin.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            if (username!=""&& password!=""){
            val comFunc = CommunicationFunction()
            comFunc.logInToServer(username,password,view,requireContext())
            }
            else {
                Toast.makeText(requireContext(),"Údaje musí být vyplněné!",Toast.LENGTH_LONG).show()
            }
            
        }
    }

}