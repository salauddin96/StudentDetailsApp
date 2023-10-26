package org.techtales.studentdetailsappassessment_04

import com.google.firebase.Timestamp

data class Data(

var id:String?=null,
val studendtid:String?=null,
val name:String?=null,
val email:String?=null,
val subject:String?=null,
val birthdate:String?=null,
    val timestamp: Timestamp?= null

)
