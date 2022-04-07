package com.example.pedikura.customers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pedikura.DataBaseHandler
import com.example.pedikura.R
import com.example.pedikura.databinding.FragmentCustomerDetailBinding
import com.example.pedikura.functions.PhotoFilesFunctions
import com.example.pedikura.functions.SharedPreferenceFunctions
import com.example.pedikura.functions.Splitters
import com.example.pedikura.volley_communication.CommunicationFunction
import com.jsibbold.zoomage.ZoomageView

class CustomerDetailFragment : Fragment() {

    private var _binding: FragmentCustomerDetailBinding? = null
    private val binding get() = _binding!!

    private val photoFilesFunc: PhotoFilesFunctions = PhotoFilesFunctions()

    private lateinit var customerVM:CustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomerDetailBinding.inflate(inflater,container,false)
        customerVM = ViewModelProvider(requireActivity()).get(CustomerViewModel::class.java)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = DataBaseHandler(requireContext(),SharedPreferenceFunctions().getUsername(requireContext()).toString())
        val position = requireArguments().getInt("id")
        Log.d("test",position.toString())
        val customer = db.searchCustomer(position)

        customerVM.setCustomer(db.searchCustomer(position),resources)
        customerVM.setDisplayableCustomer(db.searchCustomer(position))
        binding.customerVM = customerVM

        val splitters = Splitters()

        val myImage = binding.footIV
        if (photoFilesFunc.existImageInInternalStorage(requireContext(),customer.foot_image)) {
            myImage.setImageBitmap(photoFilesFunc.loadImageFromInternalStorage(requireContext(),customer.foot_image))
        }
        else{
            myImage.setImageResource(R.drawable.foot)
        }

        //test
        val photosList = splitters.splitStringComma(customer.photos)
        Log.i("test",customer.photos)
        for (item in photosList){
            val ll = binding.imageLinearLayout
            val imageView = ZoomageView(requireContext())

            imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1100)


            if (photoFilesFunc.existImageInInternalStorage(requireContext(),item)) {
                val bmp = photoFilesFunc.loadImageFromInternalStorage(requireContext(),item)
                imageView.setImageBitmap(bmp)
                imageView.rotation = 90F
            }
            else{
                imageView.setImageResource(R.drawable.foot)
            }
            ll.addView(imageView)
        }




        // Delete customer
        binding.deleteCustomerBTN.setOnClickListener {
            showWarningDialog(customerVM.getCustomer().id,customer.lname+" "+customer.fname,customer.foot_image,photosList)
        }

        binding.editCustomerBTN.setOnClickListener {
            findNavController().navigate(R.id.action_customerDetailFragment_to_addCustomerFragment2)
        }

    }


    private fun showWarningDialog(customerId:Int,customerName:String,footImage:String,photosList:List<String>){
        val alertDialog = AlertDialog.Builder(requireContext())

        alertDialog.apply {
            setTitle("Smazání klienta z databáze")
            setMessage("Opravdu chcete smazat $customerName z databáze klientů?")
            setPositiveButton("Ano"){_,_->
                val db = DataBaseHandler(requireContext(), SharedPreferenceFunctions().getUsername(requireContext()).toString())
                db.deleteCustomer(customerId)
                val comFunc = CommunicationFunction(requireContext())
                comFunc.deleteCustomerInServer(customerId.toString(),requireContext())
                photoFilesFunc.deleteImageInInternalStorage(requireContext(),footImage)
                for (item in photosList){
                    photoFilesFunc.deleteImageInInternalStorage(requireContext(),item)
                }

                findNavController().navigate(R.id.action_customerDetailFragment_to_customersFragment)
            }
            setNegativeButton("Ne"){_,_->

            }.create().show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null


    }



}







