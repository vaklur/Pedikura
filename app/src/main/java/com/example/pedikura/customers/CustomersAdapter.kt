package com.example.pedikura.customers


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pedikura.R
import com.example.pedikura.data.Customer


class CustomersAdapter(
        private var customers: List<Customer>,
        private val customerViewModel: CustomerViewModel
) : RecyclerView.Adapter<CustomersAdapter.ItemViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just an Affirmation object.
        inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val customerCardBTN: TextView = view.findViewById(R.id.customer_card_BTN)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            // create a new view
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.customer_item, parent, false)
            return ItemViewHolder(adapterLayout)
        }

        /**
         * Replace the contents of a view (invoked by the layout manager)
         */
        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = customers[position].last_name +" "+ customers[position].first_name
            holder.customerCardBTN.text = item


            holder.customerCardBTN.setOnClickListener {
                val id = customers[position].id
                customerViewModel.setActualCustomerID(id)
                //val bundle = Bundle()
                //bundle.putInt("id", id)
                Log.d("test",id.toString())
                holder.customerCardBTN.findNavController().navigate(R.id.action_customersFragment_to_customerDetailFragment)

                //Navigation.createNavigateOnClickListener(R.id.action_customersFragment_to_customerDetailFragment).onClick(holder.customerCardBTN)
            }

        }

        /**
         * Return the size of your dataset (invoked by the layout manager)
         */
        override fun getItemCount() = customers.size
    }
