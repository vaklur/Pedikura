package com.example.pedikura.customers

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pedikura.R
import com.example.pedikura.data.Customer
import com.example.pedikura.databinding.FragmentCustomersBinding
import com.example.pedikura.functions.SharedPreferenceFunctions
import com.example.pedikura.volley_communication.CommunicationFunction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.ceil

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CustomersFragment : Fragment() {
    private var _binding: FragmentCustomersBinding? = null
    private val binding get() = _binding!!

    private lateinit var customerVM: CustomerViewModel

    private lateinit var recyclerView: RecyclerView
    private var customers: List<Customer> = listOf()

    private val firstCallTime = ceil(System.currentTimeMillis() / 60_000.0).toLong() * 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(firstCallTime - System.currentTimeMillis())
            while (true) {
                launch {
                    Log.d("test", "minute")
                    CommunicationFunction(requireContext()).connectionToServer(requireContext())
                }
                delay(60_000)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        customerVM = ViewModelProvider(requireActivity()).get(CustomerViewModel::class.java)
        _binding = FragmentCustomersBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = SharedPreferenceFunctions().getUsername(requireContext())
        binding.usernameTV.text = "Přihlášen jako $username"

        recyclerView = binding.recyclerView
        customerVM.readAllCustomer.observe(
            viewLifecycleOwner
        ) { customerList ->
            customers = customerList
            attachAdapter(customerList)
            toggleRecyclerView(customerList)
            customerVM.setLastCustomerID(customerList.last().id)
        }


        binding.searchCustomerET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query = p0.toString().lowercase(Locale.getDefault())
                filterWithQuery(query)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        // Add customer button
        binding.addBTN.setOnClickListener {
            findNavController().navigate(R.id.action_customersFragment_to_addCustomerFragment)
        }


    }

    private fun attachAdapter(list: List<Customer>) {
        val searchAdapter = CustomersAdapter(list, customerVM)
        recyclerView.adapter = searchAdapter
    }

    private fun toggleRecyclerView(customerList: List<Customer>) {
        if (customerList.isEmpty()) {
            recyclerView.visibility = View.INVISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
        }
    }

    // Takes user submitted query and returns filtered array list.
    private fun onQueryChanged(filterQuery: String): List<Customer> {
        // Empty new array list which contains new strings
        val filteredList = ArrayList<Customer>()
        // Loop through each item in list
        for (currentCustomer in customers) {
            // Before checking string matching convert string to lower case.
            if (currentCustomer.last_name.lowercase(Locale.getDefault()).contains(filterQuery)) {
                // If the match is success, add item to list.
                filteredList.add(currentCustomer)
            }
        }
        return filteredList
    }

    private fun filterWithQuery(query: String) {
        // Perform operation only is query is not empty
        if (query.isNotEmpty()) {
            // Call the function with valid query and take new filtered list.
            val filteredList: List<Customer> = onQueryChanged(query)
            // Call the adapter with new filtered array list
            attachAdapter(filteredList)
            // If the matches are empty hide RecyclerView and show error text
            toggleRecyclerView(filteredList)
        } else if (query.isEmpty()) {
            // If query is empty don't make changes to list
            attachAdapter(customers)
        }
    }


}



