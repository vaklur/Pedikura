package com.example.pedikura

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class PhotoFilesFunctions {

     fun loadImageFromInternalStorage(context: Context, imageName: String): Bitmap {
        val wrapper = ContextWrapper(context)
        Log.i("test", imageName)
        val file = wrapper.getDir("images", Context.MODE_PRIVATE)
        val imgFile = File(file, imageName)
        return if (imgFile.exists()) {
            BitmapFactory.decodeFile(imgFile.absolutePath)
        } else{
            val bmp: Bitmap? = null
            bmp!!
        }
    }

     fun existImageInInternalStorage(context: Context, imageName: String):Boolean{
        val wrapper = ContextWrapper(context)
        Log.i("test", imageName)
        val file = wrapper.getDir("images", Context.MODE_PRIVATE)
        val imgFile = File(file, imageName)
        return imgFile.exists()
    }


    fun loadUriImageFromInternalStorage(context: Context, imageName: String): Uri {
        val wrapper = ContextWrapper(context)
        Log.i("test", imageName)
        val file = wrapper.getDir("images", Context.MODE_PRIVATE)
        val imgFile = File(file, imageName)
        return if (imgFile.exists()) {
            imgFile.toUri()
        } else{
            val bmp: Uri? = null
            bmp!!
        }
    }

    fun saveImageToInternalStorage(bitmap: Bitmap, context: Context, imageId: Int): String {
        // Get the image from drawable resource as drawable object

        // Get the context wrapper instance
        val wrapper = ContextWrapper(context)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)

        val imageName = "footPhoto$imageId.jpg"
        // Create a file to save the image
        file = File(file, imageName)

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
        return imageName
    }

     fun deleteImageInInternalStorage(context: Context, imageName: String) {
         val wrapper = ContextWrapper(context)
         Log.i("test", imageName)
         val file = wrapper.getDir("images", Context.MODE_PRIVATE)
         val imgFile = File(file, imageName)
         if (imgFile.exists()) {
             Log.i("test", "delete$imageName")
             imgFile.delete()
         }
     }
}