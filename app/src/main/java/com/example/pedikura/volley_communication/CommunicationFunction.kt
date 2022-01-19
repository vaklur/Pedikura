package com.example.pedikura.volley_communication

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.pedikura.*
import org.json.JSONException
import java.io.ByteArrayOutputStream

/**
 * Functions for communication with server.
 */
class CommunicationFunction {

    val c1 = "klarka"


    interface VolleyStringResponse {
        fun onSuccess()
        fun onError()
    }

    fun createUserInServer(user: String) {
        val stringRequest = object : StringRequest(
                Method.POST, getServerAddress("createcustomer"),
                Response.Listener<String> { response ->
                    try {
                        Log.d("teest",user)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },

                Response.ErrorListener { Log.d("problem", "no send"+user) }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["customer"] = user
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }


    fun addCustomerToServer(customer:Customer,context: Context) {
        val db = DataBaseHandler(context)

            Log.i("problem", customer.lname)
            val stringRequest = object : StringRequest(
                    Method.POST, getServerAddress("addcustomer"),
                    Response.Listener<String> { response ->
                        try {

                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Log.i("problem", response)
                        }
                    },

                    Response.ErrorListener {
                        val operation = Operation(customer.id.toString(),"add")
                        db.insertData(operation)
                        Log.i("problem", "no send") }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["customer"] = c1
                    params["id"] = customer.id.toString()
                    params["lname"] = customer.lname
                    params["fname"] = customer.fname
                    params["age"] = customer.age
                    params["profession"] = customer.profession
                    params["contact"] = customer.contact
                    params["address"] = customer.address
                    params["problems"] = customer.problems
                    params["problemsOther"] = customer.problems_other
                    params["treatment"] = customer.treatment
                    params["treatmentOther"] = customer.treatment_other
                    params["recommendation"] = customer.recommendation
                    params["footImage"] = customer.foot_image
                    params["notes"] = customer.notes
                    return params
                }
            }

            VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    fun updateCustomerInServer(customer: Customer,context: Context) {
        val db = DataBaseHandler(context)

        val stringRequest = object : StringRequest(
                Method.POST, getServerAddress("updatecustomer"),
                Response.Listener<String> { response ->
                    try {
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.i("problem", response)
                    }
                },

                Response.ErrorListener { Log.i("problem", "no send")
                    val operation = Operation(customer.id.toString(),"update")
                    db.insertData(operation)
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["customer"] = c1
                params["id"] = customer.id.toString()
                params["lname"] = customer.lname
                params["fname"] = customer.fname
                params["age"] = customer.age
                params["profession"] = customer.profession
                params["contact"] = customer.contact
                params["address"] = customer.address
                params["problems"] = customer.problems
                params["problemsOther"] = customer.problems_other
                params["treatment"] = customer.treatment
                params["treatmentOther"] = customer.treatment_other
                params["recommendation"] = customer.recommendation
                params["footImage"] = customer.foot_image
                params["notes"] = customer.notes
                return params
            }
        }


        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }


    fun deleteCustomerInServer(customerId: String,context: Context) {
        val db = DataBaseHandler(context)

        val stringRequest = object : StringRequest(
                Method.POST, getServerAddress("deletecustomer"),
                Response.Listener<String> { response ->
                    try {
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.d("problem", response)
                    }
                },

                Response.ErrorListener { Log.d("problem", "no send")
                    val operation = Operation(customerId,"delete")
                    db.insertData(operation)
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["user"] = c1
                params["customername"] = customerId
                return params
            }
        }


        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }


    fun getServerAddress(urlType: String): String {
        //val urlRoot = "http://192.168.56.1/pedicure"
        val urlRoot = "http://unsecureapp.tode.cz/pedicure"
        return when (urlType) {
            "addcustomer" -> "$urlRoot/v1/?op=addcustomer"
            "createuser" -> "$urlRoot/v1/?op=createcustomer"
            "uploadphoto" -> "$urlRoot/v1/?op=uploadphoto"
            "updatecustomer" -> "$urlRoot/v1/?op=updatecustomer"
            "deletecustomer" -> "$urlRoot/v1/?op=deletecustomer"
            "serverstate" -> "$urlRoot/v1/?op=serverstate"
            else -> urlRoot
        }
    }

    fun uploadImage(bitmap: Bitmap, photoName: String) {
        val stringRequest = object : StringRequest(
                Method.POST, getServerAddress("uploadphoto"),
                Response.Listener<String> { response ->
                    try {
                        Log.d("problem", response)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },

                Response.ErrorListener { Log.d("problem", "no send") }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                Log.d("problem", "Upload image")
                params["photoname"] = photoName
                params["photo"] = imageToString(bitmap)
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    fun imageToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream)
        val imgBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imgBytes, Base64.DEFAULT)
    }


    /**
     * Test connection to server with Volley library.
     *
     * @param testAddress Address of server.
     * @param volleyResponse Response of Volley communication.
     */
    private fun testConnectionToServer( volleyResponse: VolleyStringResponse) {
        val stringRequest = object : StringRequest(
                Method.GET, getServerAddress("serverstate"),
                Response.Listener<String> { response ->
                    try {
                        volleyResponse.onSuccess()


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },

                Response.ErrorListener {
                    volleyResponse.onError()

                }) {


        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }




    /**
     * Test connection to server.
     *
     * @param activity Activity for access to [SettingsSharPref].
     * @param view View for display [serverAlertDialog].
     */
    fun connectionToServer(context: Context) {
        testConnectionToServer(object : VolleyStringResponse {
            override fun onSuccess() {
                Log.d(c1, "Connect to server")
                val db = DataBaseHandler(context)
                val comFunc = CommunicationFunction()
                val photoFilesFunc = PhotoFilesFunctions()
                val list = db.readDataOperation()
                for (i in 0 until list.size){
                    when (list[i].operation) {
                        "add" -> {
                            val customer = db.searchCustomer(list[i].id.toInt())
                            comFunc.addCustomerToServer(customer,context)
                            val footBmp = photoFilesFunc.loadImageFromInternalStorage(context, "foot"+list[i].id+".jpg")
                            comFunc.uploadImage(footBmp,"foot"+list[i].id)
                            db.deleteOperation(list[i].id)
                        }
                        "update" -> {
                            val customer = db.searchCustomer(list[i].id.toInt())
                            comFunc.updateCustomerInServer(customer,context)
                            val footBmp = photoFilesFunc.loadImageFromInternalStorage(context, "foot"+list[i].id+".jpg")
                            comFunc.uploadImage(footBmp,"foot"+list[i].id)
                            db.deleteOperation(list[i].id)
                        }
                        "delete" -> {
                            comFunc.deleteCustomerInServer(list[i].id,context)
                            db.deleteOperation(list[i].id)
                        }
                    }
                    //Log.d(c1,list[i].id+list[i].operation)
                }

            }

            override fun onError(){
                Log.d(c1, "Not connect to server")

            }
        })
    }

}



