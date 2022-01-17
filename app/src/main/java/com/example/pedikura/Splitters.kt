package com.example.pedikura

import android.util.Log

class Splitters {
     fun splitStringDots (string: String):String{

        val stringList = string.split(".")
        stringList.toMutableList().removeLastOrNull()
        var outString = ""
        for (problem in stringList){
            if(problem!=""){
                outString = "$outString$problem,"
            }
        }
        Log.i("test",outString)
        outString = outString.dropLast(1)
        return outString
    }

     fun splitStringComma (string: String):List<String>{
        Log.i("test",string)
        val stringList = string.split(",").toMutableList()
        stringList.removeLastOrNull()

        return stringList
    }
}