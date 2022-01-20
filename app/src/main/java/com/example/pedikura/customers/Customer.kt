package com.example.pedikura.customers

data class Customer(
    var id:Int,
    var lname:String,
    var fname:String,
    var age:String,
    var profession:String,
    var contact:String,
    var address:String,
    var problems:String,
    var problems_other:String,
    var treatment:String,
    var treatment_other:String,
    var notes:String,
    var foot_image:String,
    var recommendation:String,
    var photos:String)
{
    constructor():this(0 ,"","","","","","","","","","","","","","")
}
