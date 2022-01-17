package com.example.pedikura

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

const val DATABASENAME = "MY P CUSTOMERS"
const val TABLENAME = "Customers"
const val COL_ID = "id"
const val COL_LNAME = "lname"
const val COL_FNAME = "fname"
const val COL_AGE = "age"
const val COL_PROFESSION = "profession"
const val COL_CONTACT = "contact"
const val COL_ADDRESS = "address"
const val COL_PROBLEMS = "problems"
const val COL_PROBLEMS_OTHER = "problemsnext"
const val COL_TREATMENT = "treatment"
const val COL_TREATMENT_NEXT = "TREATMENT_NEXT"
const val COL_NOTES = "notes"
const val COL_FOOT_IMAGE = "foot_image"
const val COL_RECOMMENDATION = "recommendation"
const val COL_PHOTOS = "photos"


class DataBaseHandler(val context: Context) : SQLiteOpenHelper(context, DATABASENAME, null,
        1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
        "CREATE TABLE " + TABLENAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_LNAME + " TEXT," + COL_FNAME + " TEXT,"+ COL_AGE + " TEXT,"+ COL_PROFESSION + " TEXT,"+ COL_CONTACT+ " TEXT,"+ COL_ADDRESS + " TEXT,"+ COL_PROBLEMS + " TEXT,"+ COL_PROBLEMS_OTHER + " TEXT,"+ COL_TREATMENT + " TEXT,"+ COL_TREATMENT_NEXT + " TEXT,"+ COL_NOTES + " TEXT,"+ COL_FOOT_IMAGE + " TEXT,"+ COL_RECOMMENDATION + " TEXT,"+ COL_PHOTOS + " TEXT)"

            db?.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //onCreate(db);
    }
    fun insertData(customer:Customer) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_LNAME, customer.lname)
        contentValues.put(COL_FNAME, customer.fname)
        contentValues.put(COL_AGE, customer.age)
        contentValues.put(COL_PROFESSION, customer.profession)
        contentValues.put(COL_CONTACT, customer.contact)
        contentValues.put(COL_ADDRESS, customer.address)
        contentValues.put(COL_PROBLEMS, customer.problems)
        contentValues.put(COL_PROBLEMS_OTHER, customer.problems_other)
        contentValues.put(COL_TREATMENT, customer.treatment)
        contentValues.put(COL_TREATMENT_NEXT, customer.treatment_other)
        contentValues.put(COL_NOTES, customer.notes)
        contentValues.put(COL_FOOT_IMAGE, customer.foot_image)
        contentValues.put(COL_RECOMMENDATION, customer.recommendation)
        contentValues.put(COL_PHOTOS,customer.photos)
        val result = database.insert(TABLENAME, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("Range")
    fun readData(): MutableList<Customer> {
        val list: MutableList<Customer> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val customer = Customer(
                        result.getInt(result.getColumnIndex(COL_ID)),
                        result.getString(result.getColumnIndex(COL_LNAME)),
                        result.getString(result.getColumnIndex(COL_FNAME)),
                        result.getString(result.getColumnIndex(COL_AGE)),
                        result.getString(result.getColumnIndex(COL_PROFESSION)),
                        result.getString(result.getColumnIndex(COL_CONTACT)),
                        result.getString(result.getColumnIndex(COL_ADDRESS)),
                        result.getString(result.getColumnIndex(COL_PROBLEMS)),
                        result.getString(result.getColumnIndex(COL_PROBLEMS_OTHER)),
                        result.getString(result.getColumnIndex(COL_TREATMENT)),
                        result.getString(result.getColumnIndex(COL_TREATMENT_NEXT)),
                        result.getString(result.getColumnIndex(COL_NOTES)),
                        result.getString(result.getColumnIndex(COL_FOOT_IMAGE)),
                        result.getString(result.getColumnIndex(COL_RECOMMENDATION)),
                        result.getString(result.getColumnIndex(COL_PHOTOS))
                        )
                list.add(customer)
            }
            while (result.moveToNext())
        }
        return list
    }

    @SuppressLint("Range")
    fun searchCustomer(id:Int):Customer{
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME where id like '%$id%'"
        val result = db.rawQuery(query,null)
        val customer = Customer()

        if (result.moveToFirst()) {
            do {
                customer.id = result.getInt(result.getColumnIndex(COL_ID))
                customer.lname = result.getString(result.getColumnIndex(COL_LNAME))
                customer.fname = result.getString(result.getColumnIndex(COL_FNAME))
                customer.age = result.getString(result.getColumnIndex(COL_AGE))
                customer.profession = result.getString(result.getColumnIndex(COL_PROFESSION))
                customer.contact = result.getString(result.getColumnIndex(COL_CONTACT))
                customer.address = result.getString(result.getColumnIndex(COL_ADDRESS))
                customer.problems = result.getString(result.getColumnIndex(COL_PROBLEMS))
                customer.problems_other = result.getString(result.getColumnIndex(COL_PROBLEMS_OTHER))
                customer.treatment = result.getString(result.getColumnIndex(COL_TREATMENT))
                customer.treatment_other = result.getString(result.getColumnIndex(COL_TREATMENT_NEXT))
                customer.notes = result.getString(result.getColumnIndex(COL_NOTES))
                customer.foot_image = result.getString(result.getColumnIndex(COL_FOOT_IMAGE))
                customer.recommendation = result.getString(result.getColumnIndex(COL_RECOMMENDATION))
                customer.photos = result.getString(result.getColumnIndex(COL_PHOTOS))

            }
            while (result.moveToNext())
        }
        return customer
    }

    fun deleteCustomer(id:Int){
        val database = this.writableDatabase
        val result = database.delete(TABLENAME, "$COL_ID=?", arrayOf(id.toString()))
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateCustomer (customer: Customer){
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_LNAME, customer.lname)
        contentValues.put(COL_FNAME, customer.fname)
        contentValues.put(COL_AGE, customer.age)
        contentValues.put(COL_PROFESSION, customer.profession)
        contentValues.put(COL_CONTACT, customer.contact)
        contentValues.put(COL_ADDRESS, customer.address)
        contentValues.put(COL_PROBLEMS, customer.problems)
        contentValues.put(COL_PROBLEMS_OTHER, customer.problems_other)
        contentValues.put(COL_TREATMENT, customer.treatment)
        contentValues.put(COL_TREATMENT_NEXT, customer.treatment_other)
        contentValues.put(COL_NOTES, customer.notes)
        contentValues.put(COL_FOOT_IMAGE, customer.foot_image)
        contentValues.put(COL_RECOMMENDATION, customer.recommendation)
        contentValues.put(COL_PHOTOS, customer.photos)
        val whereclause = "$COL_ID=?"
        val whereargs = arrayOf(customer.id.toString())
        val result = database.update(TABLENAME, contentValues,whereclause,whereargs)
        if (result == (-1)) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    fun loadCustomers():ArrayList<Customer>{
        val db = DataBaseHandler(context)
        //db.insertData(Customer(1,"s","s","s","s","s","s","s","s","s","s","s","s","s"))
        val data = db.readData()
        val customersList = ArrayList<Customer>()
         for (i in 0 until data.size) {
            customersList.add(Customer(data[i].id,data[i].lname,data[i].fname,data[i].age,data[i].profession,data[i].contact,data[i].address,data[i].problems,data[i].problems_other,data[i].treatment,data[i].treatment_other,data[i].notes,data[i].foot_image,data[i].recommendation,data[i].photos))
        }
        return customersList
    }
}