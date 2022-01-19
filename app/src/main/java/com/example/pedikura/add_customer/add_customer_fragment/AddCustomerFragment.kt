package com.example.pedikura.add_customer.add_customer_fragment

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
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pedikura.*
import com.example.pedikura.PhotoFilesFunctions
import com.example.pedikura.add_customer.photos.OnPhotosClickListener
import com.example.pedikura.add_customer.photos.Photo
import com.example.pedikura.add_customer.photos.PhotosAdapter
import com.example.pedikura.databinding.FragmentAddCustomerBinding
import com.example.pedikura.volley_communication.CommunicationFunction


class AddCustomerFragment : Fragment() {

    private var _binding:FragmentAddCustomerBinding? = null
    private val binding get() = _binding!!

    private var customerId:Int = 0
    private var existingCustomer:Boolean = false
    private lateinit var photoList: RecyclerView
    private lateinit var mProductListAdapter: PhotosAdapter

    private val addCustomerFunc:AddCustomerFunctions = AddCustomerFunctions()
    private val photoFilesFunc: PhotoFilesFunctions = PhotoFilesFunctions()


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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = DataBaseHandler(requireContext())

        photoList = binding.photoRV
        mProductListAdapter = PhotosAdapter(mOnPhotosClickListener = mOnProductClickListener)
        photoList.adapter = mProductListAdapter

        customerId = requireArguments().getInt("id")
        val customerList = db.readData()

        /* set imageId ->
        1                                               when no customers in the database
        customerId                                             when we want update saved customer
        customerList[customerList.lastIndex].id+1       when we want to add image and some customers are in database*/
        when {
            customerId != -1 -> {
                existingCustomer = true
                val customer = db.searchCustomer(customerId)
                binding.lastNameEV.setText(customer.lname)
                binding.firstNameEV.setText(customer.fname)
                binding.ageEV.setText(customer.age)
                binding.professionEV.setText(customer.profession)
                binding.contactEV.setText(customer.contact)
                binding.addressEV.setText(customer.address)

                binding.problemsOtherET.setText(customer.problems_other)
                binding.treatmentOthersET.setText(customer.treatment_other)
                binding.notesET.setText(customer.notes)
                binding.recommendationET.setText(customer.recommendation)
                val splitters = Splitters()
                val photosList = splitters.splitStringComma(customer.photos)
                for (item in photosList){
                    val id = mProductListAdapter.getNextItemId()
                    val photo = Photo(id, photoFilesFunc.loadUriImageFromInternalStorage(requireContext(), item))
                    mProductListAdapter.addPhoto(photo)
                }

                addCustomerFunc.setCheckedProblems(view, customer.problems, resources)
                addCustomerFunc.setCheckedTreatment(view, customer.treatment, resources)
            }
            customerList.isEmpty() -> {
                customerId = db.getSequenceOfCustomers().toInt()+1

            }
            else -> {
                customerId = customerList[customerList.lastIndex].id+1
            }
        }

        // Set foot image
        val myImage = binding.footIV
        if (photoFilesFunc.existImageInInternalStorage(requireContext(), "foot$customerId.jpg")) {
            val footBmp = photoFilesFunc.loadImageFromInternalStorage(requireContext(), "foot$customerId.jpg")
            myImage.setImageBitmap(footBmp)
        }
        else{
            myImage.setImageResource(R.drawable.foot_bitmap)
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
            val age = binding.ageEV.text.toString()
            val profession = binding.professionEV.text.toString()
            val contact = binding.contactEV.text.toString()
            val address = binding.addressEV.text.toString()

            val problems = addCustomerFunc.getCheckedProblems(view, resources)
            val problemsOther = binding.problemsOtherET.text.toString()
            val treatment = addCustomerFunc.getCheckedTreatment(view, resources)
            val treatmentOther = binding.treatmentOthersET.text.toString()
            val notes = binding.notesET.text.toString()
            val recommendation = binding.recommendationET.text.toString()

            val footImage = "foot$customerId.jpg"
            var photosString =""

            if (lastName=="" || firstName==""){
                Toast.makeText(
                    requireContext(),
                    "Jméno a příjmení musí být vždy vyplněné!!",
                    Toast.LENGTH_LONG
                ).show()
            }
            else {
                for (i in mProductListAdapter.getPhotos().indices) {

                    @Suppress("DEPRECATION") val bmp = MediaStore.Images.Media.getBitmap(
                        activity?.contentResolver,
                        mProductListAdapter.getPhotos()[i].imageUri
                    )
                    photosString = photosString +photoFilesFunc.saveImageToInternalStorage(
                        bmp,
                        requireContext(),
                        customerId * 1000 + mProductListAdapter.getPhotos()[i].idPhoto
                    )+","
                    Log.i("test", mProductListAdapter.getPhotos()[i].imageUri.toString())
                }

                if (!photoFilesFunc.existImageInInternalStorage(requireContext(), "foot$customerId.jpg")) {
                    val opt = BitmapFactory.Options()
                    opt.inScaled = true
                    opt.inMutable = true
                    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.foot, opt)
                    photoFilesFunc.saveImageToInternalStorage(bitmap,requireContext(),customerId)
                }

                val customer = Customer(
                    customerId,
                    lastName,
                    firstName,
                    age,
                    profession,
                    contact,
                    address,
                    problems,
                    problemsOther,
                    treatment,
                    treatmentOther,
                    notes,
                    footImage,
                    recommendation,
                    photosString
                )


                val comFunc = CommunicationFunction()
                if (existingCustomer){
                    db.updateCustomer(customer)
                    comFunc.updateCustomerInServer(customer,requireContext())
                }
                else {
                    db.insertData(customer)
                    comFunc.addCustomerToServer(customer,requireContext())
                }
                val footBmp = photoFilesFunc.loadImageFromInternalStorage(requireContext(), "foot$customerId.jpg")
                comFunc.uploadImage(footBmp,"foot$customerId")

                findNavController().navigate(R.id.action_addCustomerFragment_to_customersFragment)
            }

        }
    }

    override fun onResume() {
        super.onResume()


        val myImage = binding.footIV

        if (photoFilesFunc.existImageInInternalStorage(requireContext(), "foot$customerId.jpg")) {
            val footBmp = photoFilesFunc.loadImageFromInternalStorage(requireContext(), "foot$customerId.jpg")
            myImage.setImageBitmap(footBmp)
        }
        else{
            myImage.setImageResource(R.drawable.foot_bitmap)
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
                bundle.putInt("imageId", customerId)
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






