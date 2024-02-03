package com.gulftechinnovations.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val user:User,
    val token:String
)