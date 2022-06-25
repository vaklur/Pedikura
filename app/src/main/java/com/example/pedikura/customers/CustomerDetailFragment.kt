package com.example.pedikura.customers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pedikura.R
import com.example.pedikura.data.Customer
import com.example.pedikura.databinding.FragmentCustomerDetailBinding
import com.example.pedikura.functions.PhotoFilesFunctions
import com.example.pedikura.functions.Splitters
import com.example.pedikura.volley_communication.CommunicationFunction
import com.jsibbold.zoomage.ZoomageView

class CustomerDetailFragment : Fragment() {

    private var _binding: FragmentCustomerDetailBinding? = null
    private val binding get() = _binding!!

    private val photoFilesFunc: PhotoFilesFunctions = PhotoFilesFunctions()

    private lateinit var customerVM: CustomerViewModel
    private lateinit var splitters: Splitters

    private lateinit var customer: Customer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomerDetailBinding.inflate(inflater, container, false)
        //Initialize customer View Model
        customerVM = ViewModelProvider(requireActivity()).get(CustomerViewModel::class.java)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = customerVM.getActualCustomerID()

        splitters = Splitters()

        // Search selected customer
        customerVM.readCustomerById(id)
            .observe(viewLifecycleOwner
            ) { databaseCustomer ->
                customer = databaseCustomer

                // Set actual customer in View Model
                customerVM.setCustomer(customer, resources)
                customerVM.setDisplayableCustomer(customer)
                // set View Model for data binding
                binding.customerVM = customerVM

                // Load customer foot image to Image View
                loadCustomerFootImage(customer)
                // Load customer photos to Image View
                loadCustomerPhotos(customer)
            }

        // Delete customer button
        binding.deleteCustomerBTN.setOnClickListener {
            showWarningDialog(customerVM.getCustomer())
        }

        // Edit customer button
        binding.editCustomerBTN.setOnClickListener {
            findNavController().navigate(R.id.action_customerDetailFragment_to_addCustomerFragment2)
        }
    }


    private fun showWarningDialog(customer: Customer) {
        val alertDialog = AlertDialog.Builder(requireContext())
        val customerName = customer.last_name + " " + customer.first_name

        alertDialog.apply {
            setTitle("Smazání klienta z databáze")
            setMessage("Opravdu chcete smazat $customerName z databáze klientů?")
            setPositiveButton("Ano") { _, _ ->
                customerVM.deleteCustomer(customer)
                CommunicationFunction(requireContext()).deleteCustomerInServer(
                    customer.id.toString(),
                    requireContext()
                )
                photoFilesFunc.deleteImageInInternalStorage(requireContext(), customer.foot_image)
                for (item in splitters.splitStringComma(customer.photos)) {
                    photoFilesFunc.deleteImageInInternalStorage(requireContext(), item)
                }
                findNavController().navigate(R.id.action_customerDetailFragment_to_customersFragment)
            }
            setNegativeButton("Ne") { _, _ ->

            }.create().show()
        }
    }

    private fun loadCustomerFootImage(customer: Customer) {
        val myImage = binding.footIV
        if (photoFilesFunc.existImageInInternalStorage(requireContext(), customer.foot_image)) {
            myImage.setImageBitmap(
                photoFilesFunc.loadImageFromInternalStorage(
                    requireContext(),
                    customer.foot_image
                )
            )
        } else {
            myImage.setImageResource(R.drawable.foot)
        }
    }

    private fun loadCustomerPhotos(customer: Customer) {
        val photosList = splitters.splitStringComma(customer.photos)
        for (item in photosList) {
            val linearLayout = binding.imageLinearLayout
            val imageView = ZoomageView(requireContext())

            imageView.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1100)

            if (photoFilesFunc.existImageInInternalStorage(requireContext(), item)) {
                val bmp = photoFilesFunc.loadImageFromInternalStorage(requireContext(), item)
                imageView.setImageBitmap(bmp)
                imageView.rotation = 90F
            } else {
                imageView.setImageResource(R.drawable.foot)
            }
            linearLayout.addView(imageView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null


    }


}







