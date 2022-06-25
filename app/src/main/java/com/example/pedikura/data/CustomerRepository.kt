package com.example.pedikura.data

import androidx.lifecycle.LiveData

class CustomerRepository(private val customerDao: CustomerDao) {

    val readAllCustomers: LiveData<List<Customer>> = customerDao.readAllCustomers()

    fun readCustomerById (id:Int):LiveData<Customer>{
        return customerDao.readCustomerById(id)
    }

    suspend fun deleteCustomer (customer: Customer){
        customerDao.deleteCustomer(customer)
    }

    suspend fun deleteAllCustomers (){
        customerDao.deleteAllCustomers()
    }
    suspend fun addCustomer(customer: Customer){
        customerDao.addCustomer(customer)
    }

    suspend fun updateCustomer(customer: Customer){
        customerDao.updateCustomer(customer)
    }
}