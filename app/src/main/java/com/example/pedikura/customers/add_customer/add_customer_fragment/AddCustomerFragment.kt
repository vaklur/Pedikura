package com.example.pedikura.customers.add_customer.add_customer_fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pedikura.DataBaseHandler
import com.example.pedikura.R
import com.example.pedikura.customers.Customer
import com.example.pedikura.customers.CustomerViewModel
import com.example.pedikura.customers.add_customer.photos.OnPhotosClickListener
import com.example.pedikura.customers.add_customer.photos.Photo
import com.example.pedikura.customers.add_customer.photos.PhotosAdapter
import com.example.pedikura.databinding.FragmentAddCustomerBinding
import com.example.pedikura.functions.PhotoFilesFunctions
import com.example.pedikura.functions.SharedPreferenceFunctions
import com.example.pedikura.functions.Splitters
import com.example.pedikura.volley_communication.CommunicationFunction


class AddCustomerFragment : Fragment() {

    private var _binding:FragmentAddCustomerBinding? = null
    private val binding get() = _binding!!

    private var existingCustomer:Boolean = false
    private var customerID:Int = 0
    private lateinit var photoList: RecyclerView
    private lateinit var mProductListAdapter: PhotosAdapter

    private val addCustomerFunc:AddCustomerFunctions = AddCustomerFunctions()
    private val photoFilesFunc: PhotoFilesFunctions = PhotoFilesFunctions()

    private lateinit var customerVM:CustomerViewModel

    private lateinit var db:DataBaseHandler
    private lateinit var username:String




    private val mOnProductClickListener = object : OnPhotosClickListener {
        override fun onDelete(model: Photo) {
            // just remove the item from list
            mProductListAdapter.removePhoto(model)
        }
    }

    private var launchGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri  = result.data?.data
            val id = mProductListAdapter.getNextItemId()
            val model = Photo(id, imageUri!!)
            mProductListAdapter.addPhoto(model)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCustomerBinding.inflate(inflater,container,false)

        // Init view model
        customerVM = ViewModelProvider(requireActivity()).get(CustomerViewModel::class.java)

        // init database
        db = DataBaseHandler(requireContext(), SharedPreferenceFunctions().getUsername(requireContext()).toString())
        // get user name from share pref
        username = SharedPreferenceFunctions().getUsername(requireContext()).toString()

        return binding.root
    }



    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoList = binding.photoRV
        mProductListAdapter = PhotosAdapter(mOnPhotosClickListener = mOnProductClickListener)
        photoList.adapter = mProductListAdapter




        val customer = customerVM.getCustomer()

        binding.customerVM = customerVM

        /* set imageId ->
        1                                               when no customers in the database
        customerVM.getCustomer().id                                             when we want update saved customer
        customerList[customerList.lastIndex].id+1       when we want to add image and some customers are in database*/

        if (customerVM.getCustomer().id!=0){
            existingCustomer = true
            customerID = customerVM.getCustomer().id

            val splitters = Splitters()
            val photosList = splitters.splitStringComma(customer.photos)
            Log.d("test","x"+ customer.photos)
            for (item in photosList){
                val id = mProductListAdapter.getNextItemId()
                val photo = Photo(id, photoFilesFunc.loadUriImageFromInternalStorage(requireContext(), item))
                mProductListAdapter.addPhoto(photo)
            }
        }
        else {
            customerID = getIdForNewCustomer()
        }




        // Set foot image
        val myImage = binding.footIV
        if (photoFilesFunc.existImageInInternalStorage(requireContext(), "$username$customerID.jpg")) {
            val footBmp = photoFilesFunc.loadImageFromInternalStorage(requireContext(), "$username$customerID.jpg")
            myImage.setImageBitmap(footBmp)
        }
        else{
            myImage.setImageResource(R.drawable.foot)
        }

        addCustomerFunc.otherCheckBoxListeners(view)





        // Open foot drawing fragment
        binding.footIV.setOnClickListener{
            openFootDrawingFragment()
        }

        // Open gallery for adding photos
        binding.editPhotosBTN.setOnClickListener{
            openGallery()
        }

        // save new or update saved customer
        binding.addCustomerBTN.setOnClickListener {
            val lastName = binding.lastNameEV.text.toString()
            val firstName = binding.firstNameEV.text.toString()

            if (lastName=="" || firstName==""){
                Toast.makeText(
                    requireContext(),
                    "Jméno a příjmení musí být vždy vyplněné!!",
                    Toast.LENGTH_LONG
                ).show()
            }
            else {
                val customerX = getCustomerDataFromView(view)
                val comFunc = CommunicationFunction(requireContext())
                // if we want update saved customer
                if (existingCustomer){
                    db.updateCustomer(customerX)
                    comFunc.updateCustomerInServer(customerX,requireContext())
                }
                // if we want a save new customer
                else {
                    db.insertData(customerX)
                    comFunc.addCustomerToServer(customerX,requireContext())
                }
                // save foot image to storage
                val footBmp = photoFilesFunc.loadImageFromInternalStorage(requireContext(), "$username$customerID.jpg")
                comFunc.uploadImage(footBmp,"$username$customerID",requireContext())

                customerVM.clearActualCustomer()
                findNavController().navigate(R.id.action_addCustomerFragment_to_customersFragment)
            }

        }
    }

    private fun getIdForNewCustomer ():Int{
        val customerList = db.readData()
        return if (customerList.isEmpty()){
            if (db.getSequenceOfCustomers()==""){
                1
            } else {
                db.getSequenceOfCustomers().toInt() + 1
            }

        } else {
            if (db.getSequenceOfCustomers().toInt()>customerList[customerList.lastIndex].id+1){
                db.getSequenceOfCustomers().toInt()+1
            } else{
                customerList[customerList.lastIndex].id+1
            }
        }
    }

    private fun getCustomerDataFromView(view:View): Customer {
        var photosString =""
        for (i in mProductListAdapter.getPhotos().indices) {

            @Suppress("DEPRECATION")
            val bmp = MediaStore.Images.Media.getBitmap(
                activity?.contentResolver,
                mProductListAdapter.getPhotos()[i].imageUri
            )
            photosString = photosString +photoFilesFunc.saveImageToInternalStorage(
                bmp,
                requireContext(),
                customerVM.getCustomer().id * 1000 + mProductListAdapter.getPhotos()[i].idPhoto
            )+","
            Log.i("test", mProductListAdapter.getPhotos()[i].imageUri.toString())
        }

        if (!photoFilesFunc.existImageInInternalStorage(requireContext(), "$username$customerID.jpg")) {
            val opt = BitmapFactory.Options()
            opt.inScaled = true
            opt.inMutable = true
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.foot, opt)
            photoFilesFunc.saveImageToInternalStorage(bitmap,requireContext(),customerVM.getCustomer().id)
        }

        return Customer(
            customerVM.getCustomer().id,
            binding.lastNameEV.text.toString(),
            binding.firstNameEV.text.toString(),
            binding.ageEV.text.toString(),
            binding.professionEV.text.toString(),
            binding.contactEV.text.toString(),
            binding.addressEV.text.toString(),
            binding.lastVisitEV.text.toString(),
            addCustomerFunc.getCheckedProblems(view, resources),
            binding.problemsOtherET.text.toString(),
            addCustomerFunc.getCheckedTreatment(view, resources),
            binding.treatmentOthersET.text.toString(),
            binding.notesET.text.toString(),
            "$username$customerID.jpg",
            binding.recommendationET.text.toString(),
            photosString
        )
    }

    override fun onResume() {
        super.onResume()

        val myImage = binding.footIV

        if (photoFilesFunc.existImageInInternalStorage(requireContext(), "$username$customerID.jpg")) {
            val footBmp = photoFilesFunc.loadImageFromInternalStorage(requireContext(), "$username$customerID.jpg")
            myImage.setImageBitmap(footBmp)
        }
        else{
            myImage.setImageResource(R.drawable.foot)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        launchGallery.launch(intent)
    }



    private fun openFootDrawingFragment(){
        val alertDialog = AlertDialog.Builder(requireContext())

        alertDialog.apply {
            setTitle("Editace obrázku nohou")
            setMessage("Opravdu chcete změnit poznámky nakreslené na obrázku nohou?")
            setPositiveButton("Ano"){ _, _->
                val bundle = Bundle()
                bundle.putInt("imageId", customerVM.getCustomer().id)
                findNavController().navigate(
                    R.id.action_addCustomerFragment_to_footDrawFragment,
                    bundle
                )
            }
            setNegativeButton("Ne"){ _, _->

            }.create().show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}






