package com.example.pedikura.customers.add_customer.foot_drawing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pedikura.customers.CustomerViewModel
import com.example.pedikura.functions.PhotoFilesFunctions


class FootDrawFragment : Fragment() {

    private lateinit var myCanvasView: MyCanvasView
    private lateinit var customerViewModel:CustomerViewModel

    private val photoFilesFunc: PhotoFilesFunctions = PhotoFilesFunctions()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        myCanvasView = MyCanvasView(requireContext())
        customerViewModel = ViewModelProvider(requireActivity()).get(CustomerViewModel::class.java)
        return myCanvasView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //val imageId = requireArguments().getInt("imageId")
        val imageId = customerViewModel.getActualCustomerID()
        Log.i("test","DestroyView")
        val saveBmp = myCanvasView.getBitmap()
        if (saveBmp != null) {
            val text = photoFilesFunc.saveImageToInternalStorage(saveBmp,requireContext(),imageId)
            Log.i("test",text)
        }

    }

}