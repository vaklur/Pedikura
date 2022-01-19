package com.example.pedikura

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.text.trimmedLength
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pedikura.databinding.FragmentCustomerDetailBinding
import com.example.pedikura.volley_communication.CommunicationFunction
import com.jsibbold.zoomage.ZoomageView

class CustomerDetailFragment : Fragment() {

    private var _binding: FragmentCustomerDetailBinding? = null
    private val binding get() = _binding!!

    private val photoFilesFunc: PhotoFilesFunctions = PhotoFilesFunctions()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomerDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = DataBaseHandler(requireContext())
        val position = requireArguments().getInt("id")

        val customer = db.searchCustomer(position)
        val id = customer.id

        val splitters = Splitters()

        var problemsString = splitters.splitStringDots(customer.problems)
        problemsString = problemsString.replace(",Jiné","")
        if ((customer.problems_other != "") && (customer.problems.isNotEmpty())){
            problemsString += ","+customer.problems_other
        }
        else if (customer.problems_other != ""){
            problemsString =customer.problems_other
        }
        var treatmentString = splitters.splitStringDots(customer.treatment)
        treatmentString = treatmentString.replace(",Jiné","")
        if ((customer.treatment_other != "") && (customer.treatment.isNotEmpty())){
            treatmentString += ","+customer.treatment_other
        }
        else if (customer.treatment_other != ""){
            treatmentString =customer.treatment_other
        }

        binding.clientCardTV.text = customer.id.toString()
       binding.lname1TV.text =  customer.lname
       binding.fname1TV.text = customer.fname
       binding.age1TV.text = customer.age
       binding.profession1TV.text = customer.profession
       binding.contact1TV.text = customer.contact
       binding.address1TV.text = customer.address
       binding.problems1TV.text = problemsString
       binding.treatment1TV.text = treatmentString
       binding.notes1TV.text = customer.notes
       binding.recommendationTV1.text = customer.recommendation


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
            showWarningDialog(id,customer.lname+" "+customer.fname,customer.foot_image,photosList)
        }

        binding.editCustomerBTN.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", id)
            findNavController().navigate(R.id.action_customerDetailFragment_to_addCustomerFragment2,bundle)
        }

    }


    private fun showWarningDialog(customerId:Int,customerName:String,footImage:String,photosList:List<String>){
        val alertDialog = AlertDialog.Builder(requireContext())

        alertDialog.apply {
            setTitle("Smazání klienta z databáze")
            setMessage("Opravdu chcete smazat $customerName z databáze klientů?")
            setPositiveButton("Ano"){_,_->
                val db = DataBaseHandler(requireContext())
                db.deleteCustomer(customerId)
                val comFunc = CommunicationFunction()
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







