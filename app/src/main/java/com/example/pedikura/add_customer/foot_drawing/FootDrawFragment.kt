package com.example.pedikura.add_customer.foot_drawing

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pedikura.PhotoFilesFunctions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class FootDrawFragment : Fragment() {

    private lateinit var myCanvasView: MyCanvasView

    private val photoFilesFunc: PhotoFilesFunctions = PhotoFilesFunctions()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        myCanvasView = MyCanvasView(requireContext())
        return myCanvasView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val imageId = requireArguments().getInt("imageId")
        Log.i("test","DestroyView")
        val saveBmp = myCanvasView.getBitmap()
        if (saveBmp != null) {
            val text = photoFilesFunc.saveImageToInternalStorage(saveBmp,requireContext(),imageId)
            Log.i("test",text)
        }

    }

}