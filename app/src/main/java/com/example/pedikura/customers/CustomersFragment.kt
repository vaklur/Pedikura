package com.example.pedikura.customers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pedikura.Customer
import com.example.pedikura.DataBaseHandler
import com.example.pedikura.R
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CustomersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var customers = ArrayList<Customer>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.customers_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = DataBaseHandler(requireContext())
        customers = db.loadCustomers()

        recyclerView = view.findViewById(R.id.recyclerView)
        attachAdapter(customers)
        toggleRecyclerView(customers)

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        val searchET = view.findViewById<EditText>(R.id.search_customer_ET)
        searchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query = p0.toString().toLowerCase(Locale.getDefault())
                filterWithQuery(query)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })


        view.findViewById<Button>(R.id.add_BTN).setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", -1)
            findNavController().navigate(R.id.action_customersFragment_to_addCustomerFragment, bundle)
        }



    }

    private fun attachAdapter(list: List<Customer>) {
        val searchAdapter = CustomersAdapter(list)
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
            if (currentCustomer.lname.toLowerCase(Locale.getDefault()).contains(filterQuery)) {
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