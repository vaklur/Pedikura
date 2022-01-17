package com.example.pedikura.customers


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pedikura.Customer
import com.example.pedikura.R


class CustomersAdapter(
        private var customers: List<Customer>,
) : RecyclerView.Adapter<CustomersAdapter.ItemViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just an Affirmation object.
        class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
            val item = customers[position].lname +" "+ customers[position].fname
            holder.customerCardBTN.text = item


            holder.customerCardBTN.setOnClickListener {
                val id = customers[position].id
                val bundle = Bundle()
                bundle.putInt("id", id)
                holder.customerCardBTN.findNavController().navigate(R.id.action_customersFragment_to_customerDetailFragment,bundle)

                //Navigation.createNavigateOnClickListener(R.id.action_customersFragment_to_customerDetailFragment).onClick(holder.customerCardBTN)
            }

        }

        /**
         * Return the size of your dataset (invoked by the layout manager)
         */
        override fun getItemCount() = customers.size
    }
