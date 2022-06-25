package com.example.pedikura.customers

import android.app.Application
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pedikura.R
import com.example.pedikura.data.Customer
import com.example.pedikura.data.CustomerDatabase
import com.example.pedikura.data.CustomerRepository
import com.example.pedikura.functions.Splitters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerViewModel(application: Application) :AndroidViewModel(application) {

    // Customers variables
    private var actualCustomer: Customer
    private var problems: Problems
    private var treatment:Treatment
    private lateinit var displayableCustomer: Customer
    private var lastCustomerID:Int

    // Database variables
    val readAllCustomer: LiveData<List<Customer>>
    private val repository: CustomerRepository

    init {
        val customerDao = CustomerDatabase.getDatabase(application).customerDao()
        repository = CustomerRepository(customerDao)
        readAllCustomer = repository.readAllCustomers
        actualCustomer = Customer()
        problems = Problems()
        treatment = Treatment()
        lastCustomerID = 0
    }

    //Database
    fun addCustomer (customer: Customer){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCustomer(customer)
        }
    }

    fun updateCustomer (customer: Customer){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateCustomer(customer)
        }
    }

    fun deleteAllCustomers (){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllCustomers()
        }
    }

    fun deleteCustomer (customer: Customer){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteCustomer(customer)
        }
    }

    fun readCustomerById(id:Int):LiveData<Customer>{
        return repository.readCustomerById(id)
    }

    //Customers

    fun setActualCustomerID(id:Int){
        actualCustomer.id = id
    }

    fun getActualCustomerID():Int{
        return actualCustomer.id
    }

    fun setLastCustomerID (id: Int){
        lastCustomerID = id
    }

    fun getLastCustomerID ():Int{
        return lastCustomerID
    }


    fun setCustomer (customer: Customer,resources: Resources){
        actualCustomer = customer
        setProblems(customer.problems,resources)
        setTreatments(customer.treatment,resources)
    }

    fun getCustomer ():Customer{
        return  actualCustomer
    }


    fun setDisplayableCustomer (customer: Customer){
        displayableCustomer = customer
        displayableCustomer.problems = setProblemsToDisplay(displayableCustomer.problems,displayableCustomer.problems_other)
        displayableCustomer.treatment = setTreatmentToDisplay(displayableCustomer.treatment,displayableCustomer.treatment_other)
    }

    fun getDisplayableCustomer ():Customer{
        return displayableCustomer
    }


    private fun setProblemsToDisplay (problem: String, problemOther:String):String{
        val splitters = Splitters()
        var problemsString = splitters.splitStringDots(problem)
        problemsString = problemsString.replace(",Jiné","")
        problemsString = problemsString.replace("Jiné,","")
        if ((problemOther != "") && (problem.isNotEmpty())){
            problemsString += ",$problemOther"
        }
        else if (problemOther != ""){
            problemsString =problemOther
        }
        return  problemsString
    }

    private  fun setTreatmentToDisplay (treatment:String, treatmentOther:String):String{
        val splitters = Splitters()
        var treatmentsString = splitters.splitStringDots(treatment)
        treatmentsString = treatmentsString.replace(",Jiné","")
        treatmentsString = treatmentsString.replace("Jiné,","")
        if ((treatmentOther != "") && (treatment.isNotEmpty())){
            treatmentsString += ",$treatmentOther"
        }
        else if (treatmentOther != ""){
            treatmentsString =treatmentOther
        }
        return  treatmentsString
    }

     private fun setProblems (problem:String, resources: Resources){
        Log.d("test","setproblems")
        Log.d("test",problem)
        val problemsList:List<String> = problem.split(".")

        if (problemsList.contains(resources.getString(R.string.problems_bv))) problems.problemBV = true
        if (problemsList.contains(resources.getString(R.string.problems_b))) problems.problemB = true
        if (problemsList.contains(resources.getString(R.string.problems_k))) problems.problemK = true
        if (problemsList.contains(resources.getString(R.string.problems_p))) problems.problemKP = true
        if (problemsList.contains(resources.getString(R.string.problems_ko))) problems.problemKO = true
        if (problemsList.contains(resources.getString(R.string.problems_mn))) problems.problemMN = true
        if (problemsList.contains(resources.getString(R.string.problems_mp))) problems.problemMP = true
        if (problemsList.contains(resources.getString(R.string.problems_pc))) problems.problemPC = true
        if (problemsList.contains(resources.getString(R.string.problems_pn))) problems.problemPN = true
        if (problemsList.contains(resources.getString(R.string.problems_p))) problems.problemP = true
        if (problemsList.contains(resources.getString(R.string.problems_sp))) problems.problemSP = true
        if (problemsList.contains(resources.getString(R.string.problems_zn))) problems.problemZN = true
        if (problemsList.contains(resources.getString(R.string.problems_k))) problems.problemK = true
        if (problemsList.contains(resources.getString(R.string.problems_h))) problems.problemH = true
        if (problemsList.contains(resources.getString(R.string.problems_others))) problems.problemO = true
        Log.d("test",problems.toString())
    }

    fun getProblems(): Problems {
        return problems
    }

    private fun setTreatments (treatments:String, resources: Resources){
        val treatmentsList:List<String> = treatments.split(".")

        if (treatmentsList.contains(resources.getString(R.string.treatment_a))) treatment.treatmentA = true
        if (treatmentsList.contains(resources.getString(R.string.treatment_co))) treatment.treatmentCO = true
        if (treatmentsList.contains(resources.getString(R.string.treatment_d))) treatment.treatmentD = true
        if (treatmentsList.contains(resources.getString(R.string.treatment_kz))) treatment.treatmentKZ = true
        if (treatmentsList.contains(resources.getString(R.string.treatment_l))) treatment.treatmentL = true
        if (treatmentsList.contains(resources.getString(R.string.treatment_h))) treatment.treatmentH = true
        if (treatmentsList.contains(resources.getString(R.string.problems_n))) treatment.treatmentN = true
        if (treatmentsList.contains(resources.getString(R.string.treatment_others))) treatment.treatmentO = true
    }

    fun getTreatments():Treatment{
        return  treatment
    }

    fun clearActualCustomer(){
        actualCustomer = Customer()
        problems = Problems()
        treatment = Treatment()
    }

}