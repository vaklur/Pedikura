package com.example.pedikura.customers

data class Problems(
    var problemBV:Boolean,
    var problemB:Boolean,
    var problemK:Boolean,
    var problemKP:Boolean,
    var problemKO:Boolean,
    var problemMN:Boolean,
    var problemMP:Boolean,
    var problemN:Boolean,
    var problemPC:Boolean,
    var problemPN:Boolean,
    var problemP:Boolean,
    var problemSP:Boolean,
    var problemZN:Boolean,
    var problemH:Boolean,
    var problemO:Boolean
){
    constructor():this(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false)
}


