package com.example.pedikura.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCustomer (customer: Customer)

    @Update
    suspend fun updateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("DELETE FROM customer_table")
    suspend fun deleteAllCustomers ()

    @Query("SELECT * FROM customer_table WHERE id = :id")
    fun readCustomerById(id:Int):LiveData<Customer>

    @Query("SELECT * FROM customer_table ORDER BY id ASC")
    fun readAllCustomers():LiveData<List<Customer>>
}