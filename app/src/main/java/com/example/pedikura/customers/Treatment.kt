package com.example.pedikura.customers

data class Treatment(
    var treatmentA:Boolean,
    var treatmentCO:Boolean,
    var treatmentD:Boolean,
    var treatmentKZ:Boolean,
    var treatmentL:Boolean,
    var treatmentH:Boolean,
    var treatmentN:Boolean,
    var treatmentO:Boolean

){
    constructor():this(false,false,false,false,false,false,false,false)
}
