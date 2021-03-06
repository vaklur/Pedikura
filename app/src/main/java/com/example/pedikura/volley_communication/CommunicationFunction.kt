package com.example.pedikura.volley_communication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.pedikura.Cryptography
import com.example.pedikura.DataBaseHandler
import com.example.pedikura.backup.Operation
import com.example.pedikura.R
import com.example.pedikura.backup.BackupDialog
import com.example.pedikura.data.Customer
import com.example.pedikura.functions.PhotoFilesFunctions
import com.example.pedikura.functions.SharedPreferenceFunctions
import com.example.pedikura.logs.Logs
import org.json.JSONArray
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Functions for communication with server.
 */

const val urlRoot = "http://192.168.56.1/pedicure"
//const val urlRoot = "https://vaklur.cz/pedicure"
class CommunicationFunction(context: Context) {

    val actualUsername = SharedPreferenceFunctions().getUsername(context)
    val crypto = Cryptography()

    interface VolleyStringResponse {
        fun onSuccess()
        fun onError()
    }

    /*fun createUserInServer(user: String) {
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
    }*/

    fun logInToServer(username:String,password:String,view:View, context: Context) {
        val stringRequest = object : StringRequest(
            Method.POST, getServerAddress("login"),
            Response.Listener { response ->
                try {
                    when (response.trim()) {
                        "Login Success" -> {
                            SharedPreferenceFunctions().saveSP(username,true,context)
                            view.findNavController().navigate(R.id.action_logIn_to_customersFragment)
                        }
                        "Username or Password wrong" -> {
                            Toast.makeText(context,"P??ezd??vka nebo heslo je nespr??vn??",Toast.LENGTH_LONG).show()
                        }
                        else -> {
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {Toast.makeText(context,"Nelze se p??ihl??sit - probl??m s p??ipojen??m k serveru",Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = crypto.encryptData(username,context)
                params["password"] = crypto.encryptData(password,context)
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    fun signUpToServer(fullname:String,email:String,username:String,password:String,view: View,context: Context) {
        val stringRequest = object : StringRequest(
            Method.POST, getServerAddress("signup"),
            Response.Listener { response ->
                try {
                    Log.d("test",response)
                    when (response.trim()) {
                        "SignSuccess" -> {
                            view.findNavController().navigate(R.id.action_signUp_to_logIn)
                        }
                        "SignFailed" -> {
                            Toast.makeText(context,"Registrace se nezda??ila - zkuste zm??nit jm??no ??i email.",Toast.LENGTH_LONG).show()
                        }
                        else -> {
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { Toast.makeText(context,"Nelze se p??ihl??sit - probl??m s p??ipojen??m k serveru",Toast.LENGTH_LONG).show()
                Log.d("problem", "no send") }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["fullname"] = crypto.encryptData(fullname,context)
                params["email"] = crypto.encryptData(email,context)
                params["username"] = crypto.encryptData(username,context)
                params["password"] = crypto.encryptData(password,context)
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    fun addCustomerToServer(customer: Customer, context: Context) {
        val db = DataBaseHandler(context,SharedPreferenceFunctions().getUsername(context).toString())
            val stringRequest = object : StringRequest(
                    Method.POST, getServerAddress("addcustomer"),
                    Response.Listener {response ->
                        try {
                            val log = Logs(getCurrentTime(),"Z??kazn??k s ID: "+customer.id+" byl p??id??n do datab??ze na serveru.","green")
                            db.insertData(log)
                            Log.d("test",response)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                            val log = Logs(getCurrentTime(),"P??i p??id??n?? z??kazn??ka s ID: "+customer.id+" nastala chyba serveru","red")
                            db.insertData(log)
                        }
                    },
                    Response.ErrorListener {
                        val operation = Operation(customer.id.toString(),"add")
                        db.insertData(operation)
                        val log = Logs(getCurrentTime(),"Z??kazn??k s ID: "+customer.id+" byl p??id??n do datab??ze k odesl??n?? na server.","yellow")
                        db.insertData(log)
                    }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["customer"] = crypto.encryptData(actualUsername.toString(),context)
                    params["id"] = crypto.encryptData(customer.id.toString(),context)
                    params["lname"] = crypto.encryptData(customer.last_name,context)
                    params["fname"] = crypto.encryptData(customer.first_name,context)
                    params["age"] = crypto.encryptData(customer.age,context)
                    params["profession"] = crypto.encryptData(customer.profession,context)
                    params["contact"] = crypto.encryptData(customer.contact,context)
                    params["address"] = crypto.encryptData(customer.address,context)
                    params["lastVisit"] = crypto.encryptData(customer.last_visit,context)
                    params["problems"] = crypto.encryptData(customer.problems,context)
                    params["problemsOther"] = crypto.encryptData(customer.problems_other,context)
                    params["treatment"] = crypto.encryptData(customer.treatment,context)
                    params["treatmentOther"] = crypto.encryptData(customer.treatment_other,context)
                    params["recommendation"] = crypto.encryptData(customer.recommendation,context)
                    params["footImage"] = crypto.encryptData(customer.foot_image,context)
                    params["notes"] = crypto.encryptData(customer.notes,context)
                    return params
                }
            }
            VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    fun updateCustomerInServer(customer: Customer, context: Context) {
        val db = DataBaseHandler(context,SharedPreferenceFunctions().getUsername(context).toString())

        val stringRequest = object : StringRequest(
                Method.POST, getServerAddress("updatecustomer"),
                Response.Listener {
                    try {
                        val log = Logs(getCurrentTime(),"Z??kazn??k s ID: "+customer.id+" byl aktualizov??n v datab??zi na serveru.","green")
                        db.insertData(log)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        val log = Logs(getCurrentTime(),"P??i aktualizaci z??kazn??ka s ID: "+customer.id+" nastala chyba serveru","red")
                        db.insertData(log)
                    }
                },
                Response.ErrorListener { Log.i("problem", "no send")
                    val operation = Operation(customer.id.toString(),"update")
                    db.insertData(operation)
                    val log = Logs(getCurrentTime(),"Z??kazn??k s ID: "+customer.id+" byl p??id??n do datab??ze k aktualizaci na serveru.","yellow")
                    db.insertData(log)
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["customer"] = crypto.encryptData(actualUsername.toString(),context)
                params["id"] = crypto.encryptData(customer.id.toString(),context)
                params["lname"] = crypto.encryptData(customer.last_name,context)
                params["fname"] = crypto.encryptData(customer.first_name,context)
                params["age"] = crypto.encryptData(customer.age,context)
                params["profession"] = crypto.encryptData(customer.profession,context)
                params["contact"] = crypto.encryptData(customer.contact,context)
                params["address"] = crypto.encryptData(customer.address,context)
                params["lastVisit"] = crypto.encryptData(customer.last_visit,context)
                params["problems"] = crypto.encryptData(customer.problems,context)
                params["problemsOther"] = crypto.encryptData(customer.problems_other,context)
                params["treatment"] = crypto.encryptData(customer.treatment,context)
                params["treatmentOther"] = crypto.encryptData(customer.treatment_other,context)
                params["recommendation"] = crypto.encryptData(customer.recommendation,context)
                params["footImage"] = crypto.encryptData(customer.foot_image,context)
                params["notes"] = crypto.encryptData(customer.notes,context)
                return params
            }
        }


        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }


    fun deleteCustomerInServer(customerId: String,context: Context) {
        val db = DataBaseHandler(context,SharedPreferenceFunctions().getUsername(context).toString())
        val stringRequest = object : StringRequest(
                Method.POST, getServerAddress("deletecustomer"),
                Response.Listener {response ->
                    try {
                        val log = Logs(getCurrentTime(), "Z??kazn??k s ID: $customerId byl smaz??n z datab??ze na serveru.","green")
                        db.insertData(log)
                        Log.d("test",response)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        val log = Logs(getCurrentTime(), "P??i smaz??n?? z??kazn??ka s ID: $customerId nastala chyba serveru","red")
                        db.insertData(log)
                    }
                },
                Response.ErrorListener { Log.d("problem", "no send")
                    val operation = Operation(customerId,"delete")
                    db.insertData(operation)
                    val log = Logs(getCurrentTime(), "Z??kazn??k s ID: $customerId byl p??id??n do datab??ze ke smaz??n?? ze serveru.","yellow")
                    db.insertData(log)
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = crypto.encryptData(actualUsername.toString(),context)
                params["id"] = crypto.encryptData(customerId,context)
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }


    fun getServerAddress(urlType: String): String {
        return when (urlType) {
            "addcustomer" -> "$urlRoot/?op=addcustomer"
            "createuser" -> "$urlRoot/?op=createcustomer"
            "uploadphoto" -> "$urlRoot/?op=uploadphoto"
            "downloadphoto" -> "$urlRoot/getphoto.php"
            "updatecustomer" -> "$urlRoot/?op=updatecustomer"
            "deletecustomer" -> "$urlRoot/?op=deletecustomer"
            "serverstate" -> "$urlRoot/?op=serverstate"
            "login" -> "$urlRoot/login.php"
            "signup" -> "$urlRoot/signup.php"
            "getdata" -> "$urlRoot/getdata.php"
            else -> urlRoot
        }
    }

    fun uploadImage(bitmap: Bitmap, photoName: String, context: Context) {
        Log.d("test","Upload Image")
        val db = DataBaseHandler(context,SharedPreferenceFunctions().getUsername(context).toString())
        val stringRequest = object : StringRequest(
                Method.POST, getServerAddress("uploadphoto"),
                Response.Listener {
                    try {
                        val log = Logs(getCurrentTime(), "Obr??zek - $photoName byl p??id??n na server.","green")
                        db.insertData(log)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        val log = Logs(getCurrentTime(), "P??i smaz??n?? obr??zku - $photoName nastala chyba serveru","red")
                        db.insertData(log)
                    }
                },
                Response.ErrorListener {}) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = crypto.encryptData(actualUsername.toString(),context)
                params["photoname"] = crypto.encryptData(photoName,context)
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

    fun stringToImage(imageString: String): Bitmap {
        val imageBytes = Base64.decode(imageString, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }


    /**
     * Test connection to server with Volley library.
     *
     */
    private fun testConnectionToServer( volleyResponse: VolleyStringResponse) {
        val stringRequest = object : StringRequest(
                Method.GET, getServerAddress("serverstate"),
                Response.Listener {
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
     */
    fun connectionToServer(context: Context) {
        testConnectionToServer(object : VolleyStringResponse {
            override fun onSuccess() {
                Log.d(actualUsername, "Connect to server")
                val db = DataBaseHandler(context,SharedPreferenceFunctions().getUsername(context).toString())
                val comFunc = CommunicationFunction(context)
                val photoFilesFunc = PhotoFilesFunctions()
                val list = db.readDataOperation()
                for (i in 0 until list.size){
                    when (list[i].operation) {
                        "add" -> {
                            val customer = db.searchCustomer(list[i].id.toInt())
                            comFunc.addCustomerToServer(customer,context)
                            val footBmp = photoFilesFunc.loadImageFromInternalStorage(context, actualUsername.toString()+list[i].id+".jpg")
                            comFunc.uploadImage(footBmp,actualUsername.toString()+list[i].id,context)
                            db.deleteOperation(list[i].id)
                        }
                        "update" -> {
                            val customer = db.searchCustomer(list[i].id.toInt())
                            comFunc.updateCustomerInServer(customer,context)
                            val footBmp = photoFilesFunc.loadImageFromInternalStorage(context, actualUsername.toString()+list[i].id+".jpg")
                            comFunc.uploadImage(footBmp,actualUsername.toString()+list[i].id,context)
                            db.deleteOperation(list[i].id)
                        }
                        "delete" -> {
                            comFunc.deleteCustomerInServer(list[i].id,context)
                            db.deleteOperation(list[i].id)
                        }
                    }
                    Log.d(actualUsername,list[i].id+list[i].operation)
                }

            }

            override fun onError(){
                Log.d(actualUsername, "Not connect to server")

            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime (): String {
        val df = SimpleDateFormat("dd.MM HH:mm:ss")
        return df.format(Calendar.getInstance().time)
    }

    fun getCustomersTableFromServer(username:String,password:String, context: Context,backupDialog: BackupDialog) {
        val db = DataBaseHandler(context,SharedPreferenceFunctions().getUsername(context).toString())
        val stringRequest = object : StringRequest(
                Method.POST, getServerAddress("getdata"),
                Response.Listener { response ->
                    try {
                        when (response.trim()) {
                            "Username or Password wrong" -> {
                                Toast.makeText(context, "P??ezd??vka nebo heslo je nespr??vn??", Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                db.deleteAll()
                                Log.d("test",response)
                                val array = JSONArray(response)
                                for (i in 0 until array.length()) {
                                    val customerJSON = array.getJSONObject(i)
                                    val customer = Customer(
                                            customerJSON.getString("id").toInt(),
                                            customerJSON.getString("lname"),
                                            customerJSON.getString("fname"),
                                            customerJSON.getString("age"),
                                            customerJSON.getString("profession"),
                                            customerJSON.getString("contact"),
                                            customerJSON.getString("address"),
                                            customerJSON.getString("lastVisit"),
                                            customerJSON.getString("problems"),
                                            customerJSON.getString("problemsOther"),
                                            customerJSON.getString("treatment"),
                                            customerJSON.getString("treatmentOther"),
                                            customerJSON.getString("notes"),
                                            customerJSON.getString("footImage"),
                                            customerJSON.getString("recommendation"),
                                            ""

                                    )
                                    db.insertData(customer)
                                    getCustomersPhotoFromServer(username, password, customer.foot_image, customer.id, context)

                                }
                                backupDialog.dismiss()
                            }
                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {Toast.makeText(context,"Nelze se p??ihl??sit - probl??m s p??ipojen??m k serveru",Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    fun getCustomersPhotoFromServer(username:String,password:String,photoName: String,id:Int, context: Context) {
        val photoFunc = PhotoFilesFunctions()
        val stringRequest = object : StringRequest(
                Method.POST, getServerAddress("downloadphoto"),
                Response.Listener {response ->
                    try {
                        val image =stringToImage(response)
                        photoFunc.saveImageToInternalStorage(image,context,id)
                        Log.d("test",photoName)
                    } catch (e: JSONException) {
                        e.printStackTrace()

                    }
                },
                Response.ErrorListener {}) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                params["photoname"] = photoName
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

}



