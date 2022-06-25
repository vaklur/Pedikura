package com.example.pedikura.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_table")
data class Customer (
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var last_name:String,
    var first_name:String,
    var age:String,
    var profession:String,
    var contact:String,
    var address:String,
    var last_visit:String,
    var problems:String,
    var problems_other:String,
    var treatment:String,
    var treatment_other:String,
    var notes:String,
    var foot_image:String,
    var recommendation:String,
    var photos:String)
{
    constructor():this(0 ,"","","","","","","","","","","","","","","")
}
