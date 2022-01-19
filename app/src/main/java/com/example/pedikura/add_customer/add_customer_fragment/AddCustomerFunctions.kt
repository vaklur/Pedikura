package com.example.pedikura.add_customer.add_customer_fragment

import android.content.res.Resources
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.example.pedikura.R

class AddCustomerFunctions {
     fun getCheckedProblems(view: View,resources: Resources):String{
        var problems = ""
        if(view.findViewById<CheckBox>(R.id.problems_bv_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_bv)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_b_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_b)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_k_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_k)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_kp_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_kp)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_ko_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_ko)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_mn_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_mn)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_mp_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_mp)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_pc_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_pc)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_pn_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_pn)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_p_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_p)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_sp_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_sp)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_zn_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_zn)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_k_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_k)+"."
        }
        if(view.findViewById<CheckBox>(R.id.problems_h_CHB).isChecked){
            problems = problems+resources.getString(R.string.problems_h)+"."
        }
         if(view.findViewById<CheckBox>(R.id.problems_other_CHB).isChecked){
             problems = problems+resources.getString(R.string.problems_others)+"."
         }
        return  problems
    }

     fun setCheckedProblems(view: View, problems:String,resources: Resources){
        val problemsList:List<String> = problems.split(".")
        if (problemsList.contains(resources.getString(R.string.problems_bv))){
            view.findViewById<CheckBox>(R.id.problems_bv_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_b))){
            view.findViewById<CheckBox>(R.id.problems_b_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_k))){
            view.findViewById<CheckBox>(R.id.problems_k_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_kp))){
            view.findViewById<CheckBox>(R.id.problems_kp_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_ko))){
            view.findViewById<CheckBox>(R.id.problems_ko_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_mn))){
            view.findViewById<CheckBox>(R.id.problems_mn_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_mp))){
            view.findViewById<CheckBox>(R.id.problems_mp_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_pc))){
            view.findViewById<CheckBox>(R.id.problems_pc_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_pn))){
            view.findViewById<CheckBox>(R.id.problems_pn_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_p))){
            view.findViewById<CheckBox>(R.id.problems_p_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_sp))){
            view.findViewById<CheckBox>(R.id.problems_sp_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_zn))){
            view.findViewById<CheckBox>(R.id.problems_zn_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_k))){
            view.findViewById<CheckBox>(R.id.problems_k_CHB).isChecked = true
        }
        if(problemsList.contains(resources.getString(R.string.problems_h))){
            view.findViewById<CheckBox>(R.id.problems_h_CHB).isChecked = true
        }
         if(problemsList.contains(resources.getString(R.string.problems_others))){
             view.findViewById<CheckBox>(R.id.problems_other_CHB).isChecked = true
             view.findViewById<EditText>(R.id.problems_other_ET).visibility = View.VISIBLE
         }
    }

     fun getCheckedTreatment(view: View,resources: Resources):String{
        var treatment = ""
        if(view.findViewById<CheckBox>(R.id.treatment_a_CHB).isChecked){
            treatment = treatment+resources.getString(R.string.treatment_a)+"."
        }
        if(view.findViewById<CheckBox>(R.id.treatment_co_CHB).isChecked){
            treatment = treatment+resources.getString(R.string.treatment_co)+"."
        }
        if(view.findViewById<CheckBox>(R.id.treatment_d_CHB).isChecked){
            treatment = treatment+resources.getString(R.string.treatment_d)+"."
        }
        if(view.findViewById<CheckBox>(R.id.treatment_kz_CHB).isChecked){
            treatment = treatment+resources.getString(R.string.treatment_kz)+"."
        }
        if(view.findViewById<CheckBox>(R.id.treatment_l_CHB).isChecked){
            treatment = treatment+resources.getString(R.string.treatment_l)+"."
        }
        if(view.findViewById<CheckBox>(R.id.treatment_h_CHB).isChecked){
            treatment = treatment+resources.getString(R.string.treatment_h)+"."
        }
         if(view.findViewById<CheckBox>(R.id.treatment_n_CHB).isChecked){
             treatment = treatment+resources.getString(R.string.problems_n)+"."
         }
         if(view.findViewById<CheckBox>(R.id.treatment_others_CHB).isChecked){
             treatment = treatment+resources.getString(R.string.problems_others)+"."
         }

        return  treatment
    }

     fun setCheckedTreatment(view: View,problems:String,resources: Resources) {
        val problemsList: List<String> = problems.split(".")
        if (problemsList.contains(resources.getString(R.string.treatment_a))) {
            view.findViewById<CheckBox>(R.id.treatment_a_CHB).isChecked = true
        }
        if (problemsList.contains(resources.getString(R.string.treatment_co))) {
            view.findViewById<CheckBox>(R.id.treatment_co_CHB).isChecked = true
        }
        if (problemsList.contains(resources.getString(R.string.treatment_d))) {
            view.findViewById<CheckBox>(R.id.treatment_d_CHB).isChecked = true
        }
        if (problemsList.contains(resources.getString(R.string.treatment_kz))) {
            view.findViewById<CheckBox>(R.id.treatment_kz_CHB).isChecked = true
        }
        if (problemsList.contains(resources.getString(R.string.treatment_l))) {
            view.findViewById<CheckBox>(R.id.treatment_l_CHB).isChecked = true
        }
        if (problemsList.contains(resources.getString(R.string.treatment_h))) {
            view.findViewById<CheckBox>(R.id.treatment_h_CHB).isChecked = true
        }
         if(problemsList.contains(resources.getString(R.string.problems_n))){
             view.findViewById<CheckBox>(R.id.treatment_n_CHB).isChecked = true
         }
         if(problemsList.contains(resources.getString(R.string.treatment_others))){
             view.findViewById<CheckBox>(R.id.treatment_others_CHB).isChecked = true
             view.findViewById<EditText>(R.id.treatment_others_ET).visibility = View.VISIBLE
         }
    }

    fun otherCheckBoxListeners(view: View){
        val problemsOtherCheckBox = view.findViewById<CheckBox>(R.id.problems_other_CHB)
        val problemsOtherEditText = view.findViewById<EditText>(R.id.problems_other_ET)
        problemsOtherCheckBox.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                problemsOtherEditText.visibility = View.VISIBLE
            }
            else{
                problemsOtherEditText.visibility = View.GONE
            }
        }

        val treatmentOtherCheckBox = view.findViewById<CheckBox>(R.id.treatment_others_CHB)
        val treatmentOtherEditText = view.findViewById<EditText>(R.id.treatment_others_ET)
        treatmentOtherCheckBox.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                treatmentOtherEditText.visibility = View.VISIBLE
            }
            else{
                treatmentOtherEditText.visibility = View.GONE
            }
        }
    }
}