package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var userId:Int =0,
    val userName:String,
    val userPassword:String
)
