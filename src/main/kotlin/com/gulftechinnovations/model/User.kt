package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: Int = 0,
    val userPassword: String,
    val userName:String? = null,
)
