package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id:Int = 0,
    val name:String,
    val mark:Float,
)
